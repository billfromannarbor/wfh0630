package org.rentalpos;

import lombok.AllArgsConstructor;
import org.rentalpos.entities.PriceRules;
import org.rentalpos.entities.DayCount;
import org.rentalpos.entities.RentalAgreement;
import org.rentalpos.services.DayCounter;
import org.rentalpos.services.iDayCounter;
import org.rentalpos.services.iPricing;
import org.rentalpos.services.iInventory;
import org.rentalpos.strategies.SimpleChargeDaysStrategy;
import org.rentalpos.strategies.iChargeDaysStrategy;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Represents an implementation of a simple Point of Sale system
 * Uses {@link iInventory} to retrieve available tools and {@link iPricing} to look up prices
 */
@AllArgsConstructor
public class RentalPOS implements iRentalPOS {
    final iInventory inventory;
    final iPricing pricing;
    iChargeDaysStrategy chargeDaysStrategy;

    /**
     * The business logic for checking out, returns a {@link RentalAgreement} if successful
     * @param toolCode - the tool being rented (Must be part of inventory and have a price available in pricing)
     * @param checkoutDate - the date of checkout (any date)
     * @param rentalDayCount - The number of days for the rental. (1 or more)
     * @param discountPercentage - Percent discount to give the customer (1-100)
     * @return {@link RentalAgreement}
     * @exception IllegalArgumentException - if rentalDayCount is less than 0
     * @exception IllegalArgumentException - if discountPercentage is outside the range of 0 to 100
     * @exception IllegalArgumentException - if toolCode isn't found in inventory or price isn't found in pricing
     */
    @Override
    public final RentalAgreement checkout(@Nonnull String toolCode, @Nonnull LocalDate checkoutDate,
                                          int rentalDayCount, int discountPercentage) {
        if (1 > rentalDayCount)
            throw new IllegalArgumentException("Rental Day Count must be greater than 0");
        if (0 > discountPercentage || 100 < discountPercentage)
            throw new IllegalArgumentException("Discount Percentage must be between 0 and 100");

        var builder = RentalAgreement.builder();
        builder.toolCode(toolCode);
        builder.checkoutDate(checkoutDate);
        builder.rentalDays(rentalDayCount);
        builder.discountPercentage(discountPercentage);

        var tool = this.inventory.getTool(toolCode);
        if (null == tool)
            throw new IllegalArgumentException("Tool: " + toolCode +" not found");

        builder.toolType(tool.toolType());
        builder.brand(tool.brand());
        builder.dueDate(checkoutDate.plusDays(rentalDayCount));

        final var price = this.pricing.getPrice(tool.toolType());
        if (null == price)
            throw new IllegalArgumentException("Price not found for tool: " + tool);

        builder.dailyRentalCharge(price.amount());

        int numberOfChargeDays = chargeDaysStrategy.getNumberOfChargeDays(checkoutDate, rentalDayCount, price);
        builder.chargeDays(numberOfChargeDays);

        BigDecimal preDiscountCharge = price.amount().multiply(BigDecimal.valueOf(numberOfChargeDays));
        builder.preDiscountCharge(preDiscountCharge);

        BigDecimal discountAmount =
                preDiscountCharge.multiply(BigDecimal.valueOf(discountPercentage).movePointLeft(2)).
                        setScale(2, RoundingMode.HALF_UP);
        builder.discountAmount(discountAmount);

        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);
        builder.finalCharge(finalCharge);

        return builder.build();
    }

    private int determineNumberOfDaysWithoutCharge(final PriceRules priceRules, final DayCount dayCounter) {
        int daysWithoutCharge = 0;
        if (!priceRules.holiday())
            daysWithoutCharge+=dayCounter.holidays();
        if (!priceRules.weekend())
            daysWithoutCharge+=dayCounter.weekendDays();
        if (!priceRules.weekday())
            daysWithoutCharge+=dayCounter.weekdays();

        return daysWithoutCharge;
    }
}
