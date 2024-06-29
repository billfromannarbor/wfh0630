package org.rentalpos;

import lombok.AllArgsConstructor;
import org.rentalpos.services.RentalAgreement;
import org.rentalpos.services.iInventoryService;

import javax.annotation.Nonnull;
import java.time.LocalDate;

@AllArgsConstructor
public class RentalPos implements iRentalPos {
    final iInventoryService inventoryService;

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

        return rentalAgreement;
    }
}
