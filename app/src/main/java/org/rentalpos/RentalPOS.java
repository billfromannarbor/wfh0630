package org.rentalpos;

import lombok.AllArgsConstructor;
import org.rentalpos.entities.Price;
import org.rentalpos.entities.DayCount;
import org.rentalpos.entities.RentalAgreement;
import org.rentalpos.services.DayCounter;
import org.rentalpos.services.iPricing;
import org.rentalpos.services.iDayCounter;
import org.rentalpos.services.iInventory;

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
    public RentalAgreement checkout(@Nonnull String toolCode, @Nonnull LocalDate checkoutDate,
                                    int rentalDayCount, int discountPercentage) {
        if ( rentalDayCount<1 )
            throw new IllegalArgumentException("Rental Day Count must be greater than 0");
        if ( discountPercentage<0 || discountPercentage>100 )
            throw new IllegalArgumentException("Discount Percentage must be between 0 and 100");

        var builder = RentalAgreement.builder();
        builder.toolCode(toolCode);
        builder.checkoutDate(checkoutDate);
        builder.rentalDays(rentalDayCount);
        builder.discountPercentage(discountPercentage);

        var tool = inventory.getTool(toolCode);
        if (tool == null)
            throw new IllegalArgumentException("Tool: " + toolCode +" not found");

        builder.toolType(tool.toolType());
        builder.brand(tool.brand());
        builder.dueDate(checkoutDate.plusDays(rentalDayCount));

        Price price = pricing.getPrice(tool.toolType());
        if (price == null)
            throw new IllegalArgumentException("Charge not found for tool: " + tool);

        builder.dailyRentalCharge(price.amount());

        iDayCounter dayCounter = new DayCounter(checkoutDate, rentalDayCount);
        int numberOfDaysWithoutCharge = determineNumberOfDaysWithoutCharge(price, dayCounter.getDayCount());
        int chargeDays = rentalDayCount - numberOfDaysWithoutCharge;
        builder.chargeDays(chargeDays);

        BigDecimal preDiscountCharge = price.amount().multiply(BigDecimal.valueOf(chargeDays));
        builder.preDiscountCharge(preDiscountCharge);

        BigDecimal discountAmount =
                preDiscountCharge.multiply(BigDecimal.valueOf(discountPercentage).movePointLeft(2)).
                        setScale(2, RoundingMode.HALF_UP);
        builder.discountAmount(discountAmount);

        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);
        builder.finalCharge(finalCharge);

        return builder.build();
    }

    private int determineNumberOfDaysWithoutCharge(Price price, DayCount dayCounter) {
        int daysWithoutCharge = 0;
        if (!price.holiday())
            daysWithoutCharge+=dayCounter.holidays();
        if (!price.weekend())
            daysWithoutCharge+=dayCounter.weekendDays();
        if (!price.weekday())
            daysWithoutCharge+=dayCounter.weekdays();

        return daysWithoutCharge;
    }
}
