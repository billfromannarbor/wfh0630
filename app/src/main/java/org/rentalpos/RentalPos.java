package org.rentalpos;

import lombok.AllArgsConstructor;
import org.rentalpos.entities.Charge;
import org.rentalpos.entities.GroupedDays;
import org.rentalpos.entities.RentalAgreement;
import org.rentalpos.services.DayGrouper;
import org.rentalpos.services.iChargeService;
import org.rentalpos.services.iDayGrouper;
import org.rentalpos.services.iInventory;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@AllArgsConstructor
public class RentalPos implements iRentalPos {
    final iInventory inventoryService;
    final iChargeService chargeService;

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

        var tool = inventoryService.getTool(toolCode);
        if (tool == null)
            throw new IllegalArgumentException("Tool: " + toolCode +" not found");

        builder.toolType(tool.toolType());
        builder.brand(tool.brand());
        builder.dueDate(checkoutDate.plusDays(rentalDayCount));

        Charge charge = chargeService.findCharge(tool.toolType());
        if (charge == null)
            throw new IllegalArgumentException("Charge not found for tool: " + tool);

        builder.dailyRentalCharge(charge.amount());

        iDayGrouper dayGrouper = new DayGrouper(checkoutDate, rentalDayCount);
        int numberOfDaysWithoutCharge = determineNumberOfDaysWithoutCharge(charge, dayGrouper.getGroupedDays());
        int chargeDays = rentalDayCount - numberOfDaysWithoutCharge;
        builder.chargeDays(chargeDays);

        BigDecimal preDiscountCharge = charge.amount().multiply(BigDecimal.valueOf(chargeDays));
        builder.preDiscountCharge(preDiscountCharge);

        BigDecimal discountAmount =
                preDiscountCharge.multiply(BigDecimal.valueOf(discountPercentage).movePointLeft(2)).
                        setScale(2, RoundingMode.HALF_UP);
        builder.discountAmount(discountAmount);

        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);
        builder.finalCharge(finalCharge);

        return builder.build();
    }

    private int determineNumberOfDaysWithoutCharge(Charge charge, GroupedDays dayCounter) {
        int daysWithoutCharge = 0;
        if (!charge.holiday())
            daysWithoutCharge+=dayCounter.holidays();
        if (!charge.weekend())
            daysWithoutCharge+=dayCounter.weekendDays();
        if (!charge.weekday())
            daysWithoutCharge+=dayCounter.weekdays();

        return daysWithoutCharge;
    }
}
