package org.rentalpos.strategies;

import org.junit.Assert;
import org.junit.Test;
import org.rentalpos.entities.PriceRules;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class StrategyTests {
    @Test
    public void rentalDaysReturnsNumberOfWeekdaysWeekendsAndHolidays() {
        final LocalDate checkoutDate = LocalDate.of(2024,6, 28);
        final int rentalDayCount = 6;
        PriceRules priceRules = new PriceRules("XXX", BigDecimal.valueOf(1.0),
                true, false, false);
        var chargeDaysStrategy = new SimpleChargeDaysStrategy();
        int numberOfChargeDaysForWeekdays = chargeDaysStrategy.getNumberOfChargeDays(checkoutDate, rentalDayCount, priceRules);
        Assert.assertEquals(3, numberOfChargeDaysForWeekdays);

        priceRules = new PriceRules("XXX", BigDecimal.valueOf(1.0),
                false, true, false);
        int numberOfChargeDaysForWeekenddays = chargeDaysStrategy.getNumberOfChargeDays(checkoutDate, rentalDayCount, priceRules);
        Assert.assertEquals(2, numberOfChargeDaysForWeekenddays);

        priceRules = new PriceRules("XXX", BigDecimal.valueOf(1.0),
                false, false, true);
        int numberOfChargeDaysForHolidays = chargeDaysStrategy.getNumberOfChargeDays(checkoutDate, rentalDayCount, priceRules);
        Assert.assertEquals(1, numberOfChargeDaysForHolidays);
    }

    //July 5th and a monday should be a holiday
    @Test
    public void rentalSundayJuly4th() {
        LocalDate checkoutDate = LocalDate.of(2027,7, 4);
        final int rentalDayCount = 1;
        PriceRules onlyChargeForHolidays = new PriceRules("XXX", BigDecimal.valueOf(1.0),
                false, false, true);
        var chargeDaysStrategy = new SimpleChargeDaysStrategy();
        int numberOfHolidays = chargeDaysStrategy.getNumberOfChargeDays(checkoutDate,
                rentalDayCount, onlyChargeForHolidays);
        Assert.assertEquals(1,numberOfHolidays);
    }

    //July 4th and during the week
    @Test
    public void rentalWednesdayJuly3rd() {
        LocalDate checkoutDate = LocalDate.of(2024,7, 3);
        final int rentalDayCount = 1;
        PriceRules onlyChargeForHolidays = new PriceRules("XXX", BigDecimal.valueOf(1.0),
                false, false, true);
        var chargeDaysStrategy = new SimpleChargeDaysStrategy();
        int numberOfHolidays = chargeDaysStrategy.getNumberOfChargeDays(checkoutDate,
                rentalDayCount, onlyChargeForHolidays);
        Assert.assertEquals(1,numberOfHolidays);
    }

    //Rental on a weekday
    @Test
    public void rentalOnAWeekday() {
        LocalDate checkoutDate = LocalDate.of(2024,7, 10);
        final int rentalDayCount = 1;
        PriceRules onlyChargeForWeekdays = new PriceRules("XXX", BigDecimal.valueOf(1.0),
                true, false, false);
        var chargeDaysStrategy = new SimpleChargeDaysStrategy();
        int numberOfWeekdays = chargeDaysStrategy.getNumberOfChargeDays(checkoutDate,
                rentalDayCount, onlyChargeForWeekdays);
        Assert.assertEquals(1,numberOfWeekdays);
    }

    //July 4th as a Saturday
    //It's a weekend but not a Holiday
    @Test
    public void rentalFridayJulyThird() {
        LocalDate checkoutDate = LocalDate.of(2026,7, 3);
        final int rentalDayCount = 1;
        PriceRules onlyChargeForWeekenddays = new PriceRules("XXX", BigDecimal.valueOf(1.0),
                false, true, false);
        var chargeDaysStrategy = new SimpleChargeDaysStrategy();
        int numberOfWeekenddays = chargeDaysStrategy.getNumberOfChargeDays(checkoutDate,
                rentalDayCount, onlyChargeForWeekenddays);
        assertEquals(1,numberOfWeekenddays);
    }

    @Test
    public void rentalSaturdayJuly3rd() {
        final LocalDate checkoutDate = LocalDate.of(2027,7, 3);
        final int rentalDayCount = 1;
        PriceRules onlyChargeForWeekenddays = new PriceRules("XXX", BigDecimal.valueOf(1.0),
                false, true, false);
        var chargeDaysStrategy = new SimpleChargeDaysStrategy();
        int numberOfWeekenddays = chargeDaysStrategy.getNumberOfChargeDays(checkoutDate,
                rentalDayCount, onlyChargeForWeekenddays);
        assertEquals(1,numberOfWeekenddays);
   }

   //Checkout Date is 9/1/24, which makes the 1 and only rental day Labor Day 9/2
    @Test
    public void rentalLaborDaySeptember2() {
       final LocalDate checkoutDate = LocalDate.of(2024,9, 1);
       final int rentalDayCount = 1;
       PriceRules onlyChargeForHolidays = new PriceRules("XXX", BigDecimal.valueOf(1.0),
               false, false, true);
       var chargeDaysStrategy = new SimpleChargeDaysStrategy();
       System.out.println("Checkout Date: " + checkoutDate);
       int numberOfHolidays = chargeDaysStrategy.getNumberOfChargeDays(checkoutDate,
               rentalDayCount, onlyChargeForHolidays);
       assertEquals(1,numberOfHolidays);
   }

    //Checkout Date is 8/31/2025, which makes the 1 and only rental day Labor Day 9/1/25
    @Test
    public void rentalLaborDaySeptember1() {
        final LocalDate checkoutDate = LocalDate.of(2025,8, 31);
        final int rentalDayCount = 1;
        PriceRules onlyChargeForHolidays = new PriceRules("XXX", BigDecimal.valueOf(1.0),
                false, false, true);
        var chargeDaysStrategy = new SimpleChargeDaysStrategy();
        int numberOfHolidays = chargeDaysStrategy.getNumberOfChargeDays(checkoutDate,
                rentalDayCount, onlyChargeForHolidays);
        assertEquals(1,numberOfHolidays);
    }
}
