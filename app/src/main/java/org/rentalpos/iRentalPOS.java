package org.rentalpos;

import org.rentalpos.entities.RentalAgreement;
import org.rentalpos.services.iInventory;

import java.time.LocalDate;

/**
 * Used to implement a simple Rental Point of Sale system
 */
public interface iRentalPOS {
    /**
     * check out and return a {@link RentalAgreement} .
     * @param toolCode - the tool being rented
     * @param checkoutDate - the date of checkout
     * @param rentalDayCount - The number of days for the rental.
     * @param discountPercentage - Percent discount to give the customer
     * @return {@link RentalAgreement}
     */
    RentalAgreement checkout(String toolCode, LocalDate checkoutDate, int rentalDayCount, int discountPercentage);
}
