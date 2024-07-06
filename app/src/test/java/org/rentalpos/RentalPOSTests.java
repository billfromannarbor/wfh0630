package org.rentalpos;

import org.junit.Before;
import org.junit.Test;
import org.rentalpos.entities.PriceRules;
import org.rentalpos.entities.RentalAgreement;
import org.rentalpos.entities.Tool;
import org.rentalpos.services.*;
import org.rentalpos.strategies.SimpleChargeDaysStrategy;
import org.rentalpos.strategies.iChargeDaysStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RentalPOSTests {
    iInventory inventoryService;
    iPricing pricing;
    private iRentalPOS rentalPos;

    @Before
    public void initializeServices() {
        this.inventoryService = new TestInventory(Map.of(
                "CHNS", new Tool("CHNS","Chainsaw","Stihl"),
                "LADW", new Tool("LADW","Ladder","Werner"),
                "JAKD", new Tool("JAKD","Jackhammer","DeWalt"),
                "JAKR", new Tool("JAKR","Jackhammer","Ridgid")
        ));

        this.pricing = new TestPricing(Map.of(
                "Ladder", new PriceRules("Ladder", BigDecimal.valueOf(1.99), true, true, false),
                "Chainsaw", new PriceRules("Chainsaw", BigDecimal.valueOf(1.49), true, false, true),
                "Jackhammer", new PriceRules("Jackhammer", BigDecimal.valueOf(2.99), true, false, false)
        ));

        final iChargeDaysStrategy chargeDaysStrategy = new SimpleChargeDaysStrategy();
        this.rentalPos = new RentalPOS(this.inventoryService, this.pricing, chargeDaysStrategy);
    }

    @Test
    public void checkoutReturnsRentalAgreement() {
        final RentalAgreement rentalAgreement = this.rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                6,0);
        assertNotNull(rentalAgreement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionIfRentalDayCountIsLessThanOne() {
        this.rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                0,0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionIfDiscountPercentageLessThanZeroOrGreaterThan100() {
        this.rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                1,-1);
        this.rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                1,101);
    }

    @Test
    public void testThatToolIsReturnedFromCheckout() {
        final RentalAgreement rentalAgreement =
                this.rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                        6,0);
        assertEquals("Chainsaw", rentalAgreement.toolType());
        assertEquals("Stihl", rentalAgreement.brand());
    }

    @Test
    public void validateDueDateReturnedFromCheckout() {
        final RentalAgreement rentalAgreement =
                this.rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                        1,0);
        assertEquals(LocalDate.of(2024,6, 29), rentalAgreement.dueDate());
    }

    @Test
    public void validateDailyRentalChargeReturnedFromCheckout() {
        RentalAgreement rentalAgreement =
                this.rentalPos.checkout("CHNS", LocalDate.of(2024,6, 28),
                        1,0);
        assertEquals(new BigDecimal("1.49"), rentalAgreement.dailyRentalCharge());

        rentalAgreement =
                this.rentalPos.checkout("LADW", LocalDate.of(2024,6, 28),
                        1,0);
        assertEquals(new BigDecimal("1.99"), rentalAgreement.dailyRentalCharge());

        rentalAgreement =
                this.rentalPos.checkout("JAKD", LocalDate.of(2024,6, 28),
                        1,0);
        assertEquals(new BigDecimal("2.99"), rentalAgreement.dailyRentalCharge());

        rentalAgreement =
                this.rentalPos.checkout("JAKR", LocalDate.of(2024,6, 28),
                        1,0);
        assertEquals(new BigDecimal("2.99"), rentalAgreement.dailyRentalCharge());
    }

    //For a Ladder, there will be no holiday charge, but all other days so the result should be 5
    @Test
    public void validateChargeDaysReturnedFromCheckoutNoHolidayCharge() {
        final RentalAgreement rentalAgreement = this.rentalPos.checkout("LADW", LocalDate.of(2024, 6, 28),
                6, 0);
        assertEquals(5, rentalAgreement.chargeDays());
    }

    //For a Chainsaw, there will no weekend charge, so the result should be 4
    @Test
    public void validateChargeDaysReturnedFromCheckoutNoWeekendCharge() {
        final RentalAgreement rentalAgreement = this.rentalPos.checkout("CHNS", LocalDate.of(2024, 6, 28),
                6, 0);
        assertEquals(4, rentalAgreement.chargeDays());
    }

    //For a Chainsaw, there will no weekend charge, so the result should be 0
    @Test
    public void chargeDaysReturnedFromOneDayFridayCheckoutNoWeekendCharge() {
        final RentalAgreement rentalAgreement =
                this.rentalPos.checkout("CHNS",
                        LocalDate.of(2024, 7, 5),
                1, 0);
        assertEquals(0, rentalAgreement.chargeDays());
    }

    @Test
    public void chargeDaysReturnedFromOneDaySaturdayCheckoutNoweekendCharge() {
        final RentalAgreement rentalAgreement =
                this.rentalPos.checkout("CHNS",
                        LocalDate.of(2024, 7, 6),
                        1, 0);
        assertEquals(0, rentalAgreement.chargeDays());
    }

    @Test
    public void chargeDaysReturnedFromOneDaySundayCheckoutNoweekendCharge() {
        final RentalAgreement rentalAgreement =
                this.rentalPos.checkout("CHNS",
                        LocalDate.of(2024, 7, 7),
                        1, 0);
        assertEquals(1, rentalAgreement.chargeDays());
    }

    //For a Jackhammer, there will be no weekend charge and no Holiday charge so result should be 3
    @Test
    public void chargeDaysReturnedFromCheckoutNoHolidayAndNoWeekendCharge() {
        final RentalAgreement rentalAgreement = this.rentalPos.checkout("JAKD", LocalDate.of(2024, 6, 28),
                6, 0);
        assertEquals(3, rentalAgreement.chargeDays());
    }

    //Prediscount Charge
    //Jackhammer - 3 x 2.99 = 8.97
    @Test
    public void prediscountChargeReturnedFromCheckout() {
        final RentalAgreement rentalAgreement =
                this.rentalPos.checkout("JAKD", LocalDate.of(2024,6, 28),
                        6,0);
        assertEquals(BigDecimal.valueOf(8.97), rentalAgreement.preDiscountCharge());
    }

    //Prediscount Charge
    //Chainsaw - 4 x 1.49 = 5.96
    @Test
    public void prediscountChargeReturnedFromCheckoutChainsaw() {
        final RentalAgreement rentalAgreement = this.rentalPos.checkout("CHNS", LocalDate.of(2024, 6, 28),
                6, 0);
        assertEquals(BigDecimal.valueOf(5.96), rentalAgreement.preDiscountCharge());
    }

    //Discount amount - calculated from discount % and pre-discount charge. Resulting amount
    //rounded half up to cents.

    //5.96*.50 = 2.98
    @Test
    public void discountAmountReturnedFromCheckoutChainsaw() {
        final RentalAgreement rentalAgreement = this.rentalPos.checkout("CHNS", LocalDate.of(2024, 6, 28),
                6, 50);
        assertEquals(BigDecimal.valueOf(5.96), rentalAgreement.preDiscountCharge());
        assertEquals(BigDecimal.valueOf(2.98), rentalAgreement.discountAmount());
    }

    //5.96*.47 = 2.8012 2.80
    @Test
    public void discountAmountReturnedFromCheckoutChainsaw47() {
        final RentalAgreement rentalAgreement = this.rentalPos.checkout("CHNS", LocalDate.of(2024, 6, 28),
                6, 47);
        assertEquals(BigDecimal.valueOf(5.96), rentalAgreement.preDiscountCharge());
        assertEquals(BigDecimal.valueOf(2.80).setScale(2, RoundingMode.HALF_UP), rentalAgreement.discountAmount());
    }

    //finalCharge
    //For a Ladder
    //no holiday charge so chargeable days = 5
    //1.99
    //1.99X5=9.95*.58=5.771 final is 9.95-5.77=4.18
    @Test
    public void finalChargeReturnedFromCheckoutLadder58() {
        final RentalAgreement rentalAgreement = this.rentalPos.checkout("LADW", LocalDate.of(2024, 6, 28),
                6, 58);
        assertEquals(BigDecimal.valueOf(9.95), rentalAgreement.preDiscountCharge());
        assertEquals(BigDecimal.valueOf(5.77), rentalAgreement.discountAmount());
        assertEquals(BigDecimal.valueOf(4.18), rentalAgreement.finalCharge());
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingTool() {
        final RentalAgreement rentalAgreement = this.rentalPos.checkout("LADD",
                LocalDate.of(2024, 6, 28),
                6, 58);
        assertEquals(BigDecimal.valueOf(9.95), rentalAgreement.preDiscountCharge());
        assertEquals(BigDecimal.valueOf(5.77), rentalAgreement.discountAmount());
        assertEquals(BigDecimal.valueOf(4.18), rentalAgreement.finalCharge());
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingPrice() {
        this.pricing = new TestPricing(Map.of(
                "Chainsaw", new PriceRules("Chainsaw", BigDecimal.valueOf(1.49), true, false, true),
                "Jackhammer", new PriceRules("Jackhammer", BigDecimal.valueOf(2.99), true, false, false)
        ));

        final iChargeDaysStrategy chargeDaysStrategy = new SimpleChargeDaysStrategy();
        this.rentalPos = new RentalPOS(this.inventoryService, this.pricing, chargeDaysStrategy);
        this.rentalPos.checkout("LADW",
                LocalDate.of(2024, 6, 28),
                6, 58);
    }

    //Rental on a weekday and weekday is free
    @Test
    public void rentalOnAFreeWeekday() {
        this.pricing = new TestPricing(Map.of(
                "Chainsaw", new PriceRules("Chainsaw", BigDecimal.valueOf(1.49), false, false, true)
        ));
        final iChargeDaysStrategy chargeDaysStrategy = new SimpleChargeDaysStrategy();

        this.rentalPos = new RentalPOS(this.inventoryService, this.pricing,chargeDaysStrategy );

        final RentalAgreement rentalAgreement = this.rentalPos.checkout("CHNS",
                LocalDate.of(2024, 7, 10),
                1, 0);

        assertEquals(rentalAgreement.chargeDays(),0);
    }


}
