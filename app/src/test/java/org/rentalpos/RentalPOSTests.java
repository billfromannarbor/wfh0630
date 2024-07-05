package org.rentalpos;

import org.junit.Before;
import org.junit.Test;
import org.rentalpos.entities.Charge;
import org.rentalpos.entities.RentalAgreement;
import org.rentalpos.entities.Tool;
import org.rentalpos.services.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RentalPOSTests {
    iInventory inventoryService;
    iChargeService chargeService;
    private iRentalPos rentalPos;

    @Before
    public void initializeServices() {
        inventoryService = new Inventory(Map.of(
                "CHNS", new Tool("CHNS","Chainsaw","Stihl"),
                "LADW", new Tool("LADW","Ladder","Werner"),
                "JAKD", new Tool("JAKD","Jackhammer","DeWalt"),
                "JAKR", new Tool("JAKR","Jackhammer","Ridgid")
        ));

        chargeService = new ChargeService(Map.of(
                "Ladder", new Charge(BigDecimal.valueOf(1.99), true, true, false),
                "Chainsaw", new Charge(BigDecimal.valueOf(1.49), true, false, true),
                "Jackhammer", new Charge(BigDecimal.valueOf(2.99), true, false, false)
        ));

        new ChargeService(Map.of("Chainsaw",
                new Charge(BigDecimal.valueOf(1.49), true, true, false)));

        rentalPos = new RentalPos(inventoryService, chargeService);
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
        assertEquals("Chainsaw", rentalAgreement.toolType());
        assertEquals("Stihl", rentalAgreement.brand());
    }

    @Test
    public void validateDueDateReturnedFromCheckout() {
        RentalAgreement rentalAgreement =
                rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                        1,0);
        assertEquals(LocalDate.of(2024,6, 29), rentalAgreement.dueDate());
    }

    @Test
    public void validateDailyRentalChargeReturnedFromCheckout() {
        RentalAgreement rentalAgreement =
                rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                        1,0);
        assertEquals(new BigDecimal("1.49"), rentalAgreement.dailyRentalCharge());

        rentalAgreement =
                rentalPos.checkout("LADW", LocalDate.of(2024,6, 28),
                        1,0);
        assertEquals(new BigDecimal("1.99"), rentalAgreement.dailyRentalCharge());

        rentalAgreement =
                rentalPos.checkout("JAKD", LocalDate.of(2024,6, 28),
                        1,0);
        assertEquals(new BigDecimal("2.99"), rentalAgreement.dailyRentalCharge());

        rentalAgreement =
                rentalPos.checkout("JAKR", LocalDate.of(2024,6, 28),
                        1,0);
        assertEquals(new BigDecimal("2.99"), rentalAgreement.dailyRentalCharge());
    }

    /*
    Charge days - Count of chargeable days, from day after checkout through and including due
    date, excluding “no charge” days as specified by the tool type.

    6 days from Friday 6/28/2024 takes us through one weekend and up to the fourth of july holiday
     */

    //For a Ladder, there will be no holiday charge, but all other days so the result should be 5
    @Test
    public void validateChargeDaysReturnedFromCheckoutNoHolidayCharge() {

        RentalAgreement rentalAgreement = rentalPos.checkout("LADW", LocalDate.of(2024, 6, 28),
                6, 0);
        assertEquals(5, rentalAgreement.chargeDays());
    }

    //For a Chainsaw, there will no weekend charge, so the result should be 4
    @Test
    public void validateChargeDaysReturnedFromCheckoutNoWeekendCharge() {
        RentalAgreement rentalAgreement = rentalPos.checkout("CHNS", LocalDate.of(2024, 6, 28),
                6, 0);
        assertEquals(4, rentalAgreement.chargeDays());
    }

    //For a Jackhammer, there will be no weekend charge and no Holiday charge so result should be 3
    @Test
    public void validateChargeDaysReturnedFromCheckoutNoHolidayAndNoWeekendCharge() {
        RentalAgreement rentalAgreement = rentalPos.checkout("JAKD", LocalDate.of(2024, 6, 28),
                6, 0);
        assertEquals(3, rentalAgreement.chargeDays());
    }

    //Prediscount Charge
    //Jackhammer - 3 x 2.99 = 8.97
    @Test
    public void validatePrediscountChargeReturnedFromCheckout() {
        RentalAgreement rentalAgreement =
                rentalPos.checkout("JAKD", LocalDate.of(2024,6, 28),
                        6,0);
        assertEquals(BigDecimal.valueOf(8.97), rentalAgreement.preDiscountCharge());
    }

    //Prediscount Charge
    //Chainsaw - 4 x 1.49 = 5.96
    @Test
    public void validatePrediscountChargeReturnedFromCheckoutChainsaw() {
        RentalAgreement rentalAgreement = rentalPos.checkout("CHNS", LocalDate.of(2024, 6, 28),
                6, 0);
        assertEquals(BigDecimal.valueOf(5.96), rentalAgreement.preDiscountCharge());
    }

    //Discount amount - calculated from discount % and pre-discount charge. Resulting amount
    //rounded half up to cents.

    //5.96*.50 = 2.98
    @Test
    public void validateDiscountAmountReturnedFromCheckoutChainsaw() {
        RentalAgreement rentalAgreement = rentalPos.checkout("CHNS", LocalDate.of(2024, 6, 28),
                6, 50);
        assertEquals(BigDecimal.valueOf(5.96), rentalAgreement.preDiscountCharge());
        assertEquals(BigDecimal.valueOf(2.98), rentalAgreement.discountAmount());
    }

    //5.96*.47 = 2.8012 2.80
    @Test
    public void validateDiscountAmountReturnedFromCheckoutChainsaw47() {
        RentalAgreement rentalAgreement = rentalPos.checkout("CHNS", LocalDate.of(2024, 6, 28),
                6, 47);
        assertEquals(BigDecimal.valueOf(5.96), rentalAgreement.preDiscountCharge());
        assertEquals(BigDecimal.valueOf(2.80).setScale(2), rentalAgreement.discountAmount());
    }

    //finalCharge
    //For a Ladder
    //no holiday charge so chargeable days = 5
    //1.99
    //1.99X5=9.95*.58=5.771 final is 9.95-5.77=4.18
    @Test
    public void validateFinalChargeReturnedFromCheckoutLadder58() {
        RentalAgreement rentalAgreement = rentalPos.checkout("LADW", LocalDate.of(2024, 6, 28),
                6, 58);
        assertEquals(BigDecimal.valueOf(9.95), rentalAgreement.preDiscountCharge());
        assertEquals(BigDecimal.valueOf(5.77), rentalAgreement.discountAmount());
        assertEquals(BigDecimal.valueOf(4.18), rentalAgreement.finalCharge());
    }
}
