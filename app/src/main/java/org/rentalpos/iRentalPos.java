package org.rentalpos;

import org.rentalpos.services.RentalAgreement;

import java.time.LocalDate;

public interface iRentalPos {
    RentalAgreement checkout(String toolCode, LocalDate checkoutDate, int rentalDayCount, int discountPercentage);
}
