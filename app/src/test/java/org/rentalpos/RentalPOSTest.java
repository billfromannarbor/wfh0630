package org.rentalpos;

import org.junit.Before;
import org.junit.Test;
import org.rentalpos.entities.Charge;
import org.rentalpos.entities.Tool;
import org.rentalpos.services.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RentalPOSTest {
    iInventoryService inventoryService;
    iChargeService chargeService;
    private iRentalPos rentalPos;

    @Before
    public void initializeServices() {
        inventoryService = new InventoryService(Map.of("CHNS",
                new Tool("CHNS","Chainsaw","Stihl")));

        chargeService = new ChargeService(Map.of("CHNS",
                new Charge(BigDecimal.valueOf(1.99), true, true, false)));

        rentalPos = new RentalPos(inventoryService);
    }

    @Test
    public void checkoutReturnsRentalAgreement() {
        RentalAgreement rentalAgreement = rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                6,0);
        assertNotNull(rentalAgreement);
    }

    @Test (expected = IllegalArgumentException.class)
    public void throwExceptionIfRentalDayCountIsLessThanOne() {
        rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                0,0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void throwExceptionIfDiscountPercentageLessThanZeroOrGreaterThan100() {
        rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                1,-1);
        rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                1,101);
    }

    @Test
    public void testThatToolIsReturnedFromCheckout() {
        RentalAgreement rentalAgreement =
                rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                        6,0);
        assertEquals("Chainsaw", rentalAgreement.getToolType());
        assertEquals("Stihl", rentalAgreement.getBrand());
    }

    @Test
    public void validateDueDateReturnedFromCheckout() {
        RentalAgreement rentalAgreement =
                rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                        1,0);
        assertEquals(LocalDate.of(2024,6, 29), rentalAgreement.getDueDate());
    }



    //todo: Test complicated date scenerios against ChargeDays
    //todo:  Calculate Rental Charge
    //todo:  Build formatted output method

}
