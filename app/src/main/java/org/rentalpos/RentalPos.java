package org.rentalpos;

import lombok.AllArgsConstructor;
import org.rentalpos.entities.Charge;
import org.rentalpos.services.RentalAgreement;
import org.rentalpos.services.iChargeService;
import org.rentalpos.services.iInventoryService;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@AllArgsConstructor
public class RentalPos implements iRentalPos {
    final iInventoryService inventoryService;
    final iChargeService chargeService;

    @Override
    public RentalAgreement checkout(@Nonnull String toolCode, @Nonnull LocalDate checkoutDate,
                                    int rentalDayCount, int discountPercentage) {
        if ( rentalDayCount<1 )
            throw new IllegalArgumentException("Rental Day Count must be greater than 0");
        if ( discountPercentage<0 || discountPercentage>100 )
            throw new IllegalArgumentException("Discount Percentage must be between 0 and 100");

        RentalAgreement rentalAgreement = new RentalAgreement();

        rentalAgreement.setToolCode(toolCode);
        rentalAgreement.setCheckoutDate(checkoutDate);
        rentalAgreement.setRentalDays(rentalDayCount);
        rentalAgreement.setDiscountPercentage(discountPercentage);

        rentalAgreement.setToolType(inventoryService.findTool(toolCode).toolType());
        rentalAgreement.setBrand(inventoryService.findTool(toolCode).brand());

        rentalAgreement.setDueDate(checkoutDate.plusDays(rentalDayCount));

        Charge charge = chargeService.findCharge(rentalAgreement.getToolType());
        System.out.println(charge);

        rentalAgreement.setDailyRentalCharge(charge.amount());

        iRentalDays rentalDays = new RentalDays(checkoutDate, rentalDayCount);
        System.out.println(rentalDays);

        int chargeDays = rentalDayCount;
        //Are there any holidays?
        if (!charge.holiday())
            chargeDays-=rentalDays.getHolidayCount();
        if (!charge.weekend())
            chargeDays-=rentalDays.getWeekendCount();
        if (!charge.weekday())
            chargeDays-=rentalDays.getWeekdayCount();

        rentalAgreement.setChargeDays(chargeDays);

        rentalAgreement.setPrediscountCharge(charge.amount().multiply(BigDecimal.valueOf(chargeDays)));

        //Discount amount - calculated from discount % and pre-discount charge. Resulting amount
        //rounded half up to cents.
        BigDecimal discountAmount = rentalAgreement.getPrediscountCharge().
                multiply(BigDecimal.valueOf(rentalAgreement.getDiscountPercentage()).movePointLeft(2));

        //rounded half up to cents.
        BigDecimal roundedDiscountAmount = discountAmount.setScale(2, RoundingMode.HALF_UP);

        rentalAgreement.setDiscountAmount(roundedDiscountAmount);

        //Final charge - Calculated as pre-discount charge - discount amount.
        BigDecimal finalCharge = rentalAgreement.getPrediscountCharge().
                subtract(rentalAgreement.getDiscountAmount());

        rentalAgreement.setFinalCharge(finalCharge);

        return rentalAgreement;
    }
}
