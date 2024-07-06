/*
 * This source file was generated by the Gradle 'init' task
 */
package org.rentalpos;

import org.rentalpos.entities.Price;
import org.rentalpos.entities.DayCount;
import org.rentalpos.entities.Tool;
import org.rentalpos.services.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ServiceTests {
    @Test
    public void inventoryServiceReturnsTool(){
        final String toolCode = "CHNS";
        final String toolType = "Chainsaw";
        final String brand = "Stihl";
        final Map<String, Tool> toolMap = Map.of(toolCode, new Tool(toolCode,toolType,brand));
    	final iInventory inventoryService = new TestInventory(toolMap);
    	final Tool tool = inventoryService.getTool(toolCode);
    	
    	assertNotNull(tool);	
    	assertEquals(toolCode,tool.toolCode());
    	assertEquals(toolType,tool.toolType());
    	assertEquals(brand,tool.brand());
    }

    @Test
    public void inventoryServiceReturnsTools(){
        final String toolCode1 = "CHNS";
        final String toolType1 = "Chainsaw";
        final String brand1 = "Stihl";
        final String toolCode2 = "LADW";
        final String toolType2 = "Ladder";
        final String brand2 = "Werner";
        final Map<String,Tool> toolMap = Map.of(toolCode1, new Tool(toolCode1,toolType1,brand1),
                toolCode2, new Tool(toolCode2,toolType2,brand2));
    	final iInventory inventoryService = new TestInventory(toolMap);
    	
    	final Tool tool1 = inventoryService.getTool(toolCode1);
    	assertNotNull(tool1);	
    	assertEquals(toolCode1,tool1.toolCode());
    	assertEquals(toolType1,tool1.toolType());
    	assertEquals(brand1,tool1.brand());
    	
    	final Tool tool2 = inventoryService.getTool(toolCode2);
    	assertNotNull(tool2);	
    	assertEquals(toolCode2,tool2.toolCode());
    	assertEquals(toolType2,tool2.toolType());
    	assertEquals(brand2,tool2.brand());
    }

    @Test
    public void inventoryServiceDoesNotReturnTool() {
        iInventory inventoryService = new TestInventory(new HashMap<>());
        Tool tool = inventoryService.getTool("MISSING");
        assertNull(tool);
        inventoryService = new TestInventory(Map.of("CHNS",
                new Tool("CHNS","Chainsaw","Stihl")));
        tool = inventoryService.getTool("MISSING");
        assertNull(tool);
    }

    @Test
    public void chargeServiceReturnsCharge() {
        final String toolType = "Ladder";
        final BigDecimal chargeAmount = BigDecimal.valueOf(1.99);
        final boolean weekdayCharge = true;
        final boolean weekendCharge = true;
        final boolean holidayCharge = false;
        final Map<String, Price> chargeMap = Map.of(toolType,
                new Price(toolType, chargeAmount, weekdayCharge, weekendCharge, holidayCharge));
    	final iPricing pricing = new TestPricing(chargeMap);
    	
        final Price price = pricing.getPrice(toolType);
        
        assertNotNull(price);
        assertEquals(chargeAmount, price.amount());
        assertEquals(weekdayCharge, price.weekday());
        assertEquals(weekendCharge, price.weekend());
        assertEquals(holidayCharge, price.holiday());
    }

    @Test
    public void chargeServiceReturnsCharges() {
        final String toolType1 = "Ladder";
        final BigDecimal chargeAmount1 = BigDecimal.valueOf(1.99);
        final boolean weekdayCharge1 = true;
        final boolean weekendCharge1 = true;
        final boolean holidayCharge1 = false;
        final String toolType2 = "Chainsaw";
        final BigDecimal chargeAmount2 = BigDecimal.valueOf(1.49);
        final boolean weekdayCharge2 = true;
        final boolean weekendCharge2 = false;
        final boolean holidayCharge2 = true;
        final Map<String, Price> chargeMap = Map.of(
                toolType1, new Price(toolType1, chargeAmount1, weekdayCharge1, weekendCharge1, holidayCharge1),
                toolType2, new Price(toolType2, chargeAmount2, weekdayCharge2, weekendCharge2, holidayCharge2)
        );

        final iPricing pricing = new TestPricing(chargeMap);

        final Price price1 = pricing.getPrice(toolType1);
        assertNotNull(price1);
        assertEquals(chargeAmount1, price1.amount());
        assertEquals(weekdayCharge1, price1.weekday());
        assertEquals(weekendCharge1, price1.weekend());
        assertEquals(holidayCharge1, price1.holiday());

        final Price price2 = pricing.getPrice(toolType2);
        assertEquals(chargeAmount2, price2.amount());
        assertEquals(weekdayCharge2, price2.weekday());
        assertEquals(weekendCharge2, price2.weekend());
        assertEquals(holidayCharge2, price2.holiday());
    }

    @Test
    public void rentalDaysReturnsNumberOfWeekdaysWeekendsAndHolidays() {
        //Holiday List here
        final LocalDate checkoutDate = LocalDate.of(2024,6, 28);
        final int rentalDayCount = 6;

        final DayCount dayCounter = new DayCounter(checkoutDate, rentalDayCount).getDayCount();

        assertEquals(3, dayCounter.weekdays());
        assertEquals(2, dayCounter.weekendDays());
        assertEquals(1, dayCounter.holidays());
    }

    //July 5th and a monday should be a holiday
    @Test
    public void rentalSundayJuly4th() {
        //Holiday List here
        final LocalDate checkoutDate = LocalDate.of(2027,7, 4);
        final int rentalDayCount = 1;

        final DayCount dayCounter = new DayCounter(checkoutDate, rentalDayCount).getDayCount();
        //assertEquals(0, dayCounter.);
        assertEquals(0, dayCounter.weekdays());
        assertEquals(0, dayCounter.weekendDays());
        assertEquals(1, dayCounter.holidays());
    }

    //July 4th and during the week
    @Test
    public void rentalWednesdayJuly3rd() {
        //Holiday List here
        final LocalDate checkoutDate = LocalDate.of(2024,7, 3);
        final int rentalDayCount = 1;

        final DayCount dayCounter = new DayCounter(checkoutDate, rentalDayCount).getDayCount();

        assertEquals(0, dayCounter.weekdays());
        assertEquals(0, dayCounter.weekendDays());
        assertEquals(1, dayCounter.holidays());
    }

    //Rental on a weekday
    @Test
    public void rentalOnAWeekday() {
        final LocalDate checkoutDate = LocalDate.of(2024,7, 10);
        final int rentalDayCount = 1;

        final DayCount dayCounter = new DayCounter(checkoutDate, rentalDayCount).getDayCount();

        assertEquals(1, dayCounter.weekdays());
        assertEquals(0, dayCounter.weekendDays());
        assertEquals(0, dayCounter.holidays());
    }

    //July 4th as a Saturday
    //It's a weekend but not a Holiday
    @Test
    public void rentalFridayJulyThird() {
        //Holiday List here
        final LocalDate checkoutDate = LocalDate.of(2026,7, 3);
        final int rentalDayCount = 1;

        final DayCount dayCounter = new DayCounter(checkoutDate, rentalDayCount).getDayCount();
        //assertEquals(0, dayCounter.);
        assertEquals(0, dayCounter.weekdays());
        assertEquals(1, dayCounter.weekendDays());
        assertEquals(0, dayCounter.holidays());
    }

    @Test
    public void rentalSaturdayJuly3rd() {
        //Holiday List here
        final LocalDate checkoutDate = LocalDate.of(2027,7, 3);
        final int rentalDayCount = 1;

        final DayCount dayCounter = new DayCounter(checkoutDate, rentalDayCount).getDayCount();
        //assertEquals(0, dayCounter.);
        assertEquals(0, dayCounter.weekdays());
        assertEquals(1, dayCounter.weekendDays());
        assertEquals(0, dayCounter.holidays());
    }


}
