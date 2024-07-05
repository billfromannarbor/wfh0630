package org.rentalpos;

import org.rentalpos.entities.RentalAgreement;

import java.time.LocalDate;

public interface iRentalPOS {
    RentalAgreement checkout(String toolCode, LocalDate checkoutDate, int rentalDayCount, int discountPercentage);
}
