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

public class ProofTests {
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

    @Test(expected = IllegalArgumentException.class)
    public void test1() {
        this.rentalPos.checkout("JAKR",
                LocalDate.of(2015,9, 3),
                5,101);
    }

    @Test
    public void test2() {
        RentalAgreement rentalAgreement = this.rentalPos.checkout("LADW",
                LocalDate.of(2020,7, 2),
                3,10);
        //Ladder is 1.99 with No Holiday Charge
        //7/2(Thursday) - 7/3(Friday),7/4(Saturday),7/5(Sunday)
        //2 chargeable days * 1.99 = 3.98
        assertEquals("LADW", rentalAgreement.toolCode());
        assertEquals(LocalDate.of(2020,7, 2),
                rentalAgreement.checkoutDate());
        assertEquals(3, rentalAgreement.rentalDays());

        assertEquals("Ladder", rentalAgreement.toolType());
        assertEquals("Werner", rentalAgreement.brand());
        assertEquals(LocalDate.of(2020,7, 5)
                ,rentalAgreement.dueDate());
        assertEquals(BigDecimal.valueOf(1.99), rentalAgreement.dailyRentalCharge());
        assertEquals(2, rentalAgreement.chargeDays());
        assertEquals(BigDecimal.valueOf(3.98), rentalAgreement.preDiscountCharge());
        assertEquals(10, rentalAgreement.discountPercentage());
        assertEquals(BigDecimal.valueOf(0.40).setScale(2, RoundingMode.HALF_UP),
                rentalAgreement.discountAmount());
        assertEquals(BigDecimal.valueOf(3.58), rentalAgreement.finalCharge());
    }

    @Test
    public void test3() {
        RentalAgreement rentalAgreement = rentalPos.checkout("CHNS",
                LocalDate.of(2015,7, 2),
                5,25);
        //Chainsaw is 1.49 with No Weekend Charge
        //7/2(Thursday) - 7/3(Friday),7/4(Saturday),7/5(Sunday),7/6(Monday),7/7(Tuesday)
        //Friday is the holiday, so charge for that
        //Saturday and Sunday are weekend days so no charge
        //Monday Tuesday weekdays
        //Total day count 5-2=3
        //3*1.49=4.47
        //4.47*.25=1.1175 1.12
        assertEquals("CHNS", rentalAgreement.toolCode());
        assertEquals(LocalDate.of(2015,7, 2),
                rentalAgreement.checkoutDate());
        assertEquals(5, rentalAgreement.rentalDays());
        assertEquals("Chainsaw", rentalAgreement.toolType());
        assertEquals("Stihl", rentalAgreement.brand());
        assertEquals(LocalDate.of(2015,7, 7)
                ,rentalAgreement.dueDate());
        assertEquals(BigDecimal.valueOf(1.49), rentalAgreement.dailyRentalCharge());
        assertEquals(3, rentalAgreement.chargeDays());
        assertEquals(BigDecimal.valueOf(4.47), rentalAgreement.preDiscountCharge());
        assertEquals(25, rentalAgreement.discountPercentage());
        assertEquals(BigDecimal.valueOf(1.12).setScale(2, RoundingMode.HALF_UP),
                rentalAgreement.discountAmount());
        assertEquals(BigDecimal.valueOf(3.35), rentalAgreement.finalCharge());
    }

    @Test
    public void test4() {
        final RentalAgreement rentalAgreement = this.rentalPos.checkout("JAKD",
                LocalDate.of(2015,9, 3),
                6,0);
        //Jackhammer is 2.99 with No Weekend Charge and No Holiday Charge
        //9/3/2015 9/3(Thursday) - 9/4(Friday),9/5(Saturday),9/6(Sunday),9/7(Monday),9/8(Tuesday),9/9(Wednesday)
        //Monday is the holiday, no charge
        //Saturday and Sunday are weekend days so no charge
        //Friday, Tuesday, Wednesdays weekdays
        //Total day count 6-3=3
        //3*2.99=8.97
        assertEquals("JAKD", rentalAgreement.toolCode());
        assertEquals(LocalDate.of(2015,9, 3),
                rentalAgreement.checkoutDate());
        assertEquals(6, rentalAgreement.rentalDays());
        assertEquals("Jackhammer", rentalAgreement.toolType());
        assertEquals("DeWalt", rentalAgreement.brand());
        assertEquals(LocalDate.of(2015,9, 9), rentalAgreement.dueDate());
        assertEquals(BigDecimal.valueOf(2.99), rentalAgreement.dailyRentalCharge());
        assertEquals(3, rentalAgreement.chargeDays());
        assertEquals(BigDecimal.valueOf(8.97), rentalAgreement.preDiscountCharge());
        assertEquals(0, rentalAgreement.discountPercentage());
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP),
                rentalAgreement.discountAmount());
        assertEquals(BigDecimal.valueOf(8.97), rentalAgreement.finalCharge());
    }

    @Test
    public void test5() {
        final RentalAgreement rentalAgreement = this.rentalPos.checkout("JAKR",
                LocalDate.of(2015,7, 2),
                9,0);
        //Jackhammer is 2.99 with No Weekend Charge and No Holiday Charge
        //7/2(Thursday) - 7/3(Friday),7/4(Saturday),7/5(Sunday),7/6(Monday),7/7(Tuesday),7/8(Wednesday),7/9(Thursday),7/10(Friday),7/11(Saturday)
        //Friday is the holiday, no charge
        //Saturday,Sunday,Saturday 3 weekend days  no charge
        //Monday-Friday weekdays
        //Total day count 9-5=5
        //5*2.99=14.95
        //14.95
        assertEquals("JAKR", rentalAgreement.toolCode());
        assertEquals(LocalDate.of(2015,7, 2),
                rentalAgreement.checkoutDate());
        assertEquals(9, rentalAgreement.rentalDays());
        assertEquals("Jackhammer", rentalAgreement.toolType());
        assertEquals("Ridgid", rentalAgreement.brand());
        assertEquals(LocalDate.of(2015,7, 11), rentalAgreement.dueDate());
        assertEquals(BigDecimal.valueOf(2.99), rentalAgreement.dailyRentalCharge());
        assertEquals(5, rentalAgreement.chargeDays());
        assertEquals(BigDecimal.valueOf(14.95), rentalAgreement.preDiscountCharge());
        assertEquals(0, rentalAgreement.discountPercentage());
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP),
                rentalAgreement.discountAmount());
        assertEquals(BigDecimal.valueOf(14.95), rentalAgreement.finalCharge());
    }

    @Test
    public void test6() {
        final RentalAgreement rentalAgreement = this.rentalPos.checkout("JAKR",
                LocalDate.of(2020,7, 2),
                4,50);
        //Jackhammer is 2.99 with No Weekend Charge and No Holiday Charge
        //7/2(Thursday) - 7/3(Friday),7/4(Saturday),7/5(Sunday),7/6(Monday)
        //Friday is the holiday, no charge
        //Saturday,Sunday 2 weekend days  no charge
        //Monday 1 weekdays
        //Total day count 4-3=1
        //1*2.99=2.99
        //2.99*.50=1.495
        //2.99-1.50
        assertEquals("JAKR", rentalAgreement.toolCode());
        assertEquals(LocalDate.of(2020,7, 2),
                rentalAgreement.checkoutDate());
        assertEquals(4, rentalAgreement.rentalDays());
        assertEquals("Jackhammer", rentalAgreement.toolType());
        assertEquals("Ridgid", rentalAgreement.brand());
        assertEquals(LocalDate.of(2020,7, 6), rentalAgreement.dueDate());
        assertEquals(BigDecimal.valueOf(2.99), rentalAgreement.dailyRentalCharge());
        assertEquals(1, rentalAgreement.chargeDays());
        assertEquals(BigDecimal.valueOf(2.99), rentalAgreement.preDiscountCharge());
        assertEquals(50, rentalAgreement.discountPercentage());
        assertEquals(BigDecimal.valueOf(1.50).setScale(2, RoundingMode.HALF_UP),
                rentalAgreement.discountAmount());
        assertEquals(BigDecimal.valueOf(1.49), rentalAgreement.finalCharge());
    }
}
