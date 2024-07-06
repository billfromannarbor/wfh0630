/*
 * This source file was generated by the Gradle 'init' task
 */
package org.rentalpos;

import org.rentalpos.entities.PriceRules;
import org.rentalpos.entities.Tool;
import org.rentalpos.services.*;
import org.junit.Test;

import java.math.BigDecimal;
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
    	final iInventory inventoryService = new InventoryFromMap(toolMap);
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
    	final iInventory inventoryService = new InventoryFromMap(toolMap);
    	
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
        iInventory inventoryService = new InventoryFromMap(new HashMap<>());
        Tool tool = inventoryService.getTool("MISSING");
        assertNull(tool);
        inventoryService = new InventoryFromMap(Map.of("CHNS",
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
        final Map<String, PriceRules> chargeMap = Map.of(toolType,
                new PriceRules(toolType, chargeAmount, weekdayCharge, weekendCharge, holidayCharge));
    	final iPricing pricing = new PricingFromMap(chargeMap);
    	
        final PriceRules priceRules = pricing.getPrice(toolType);
        
        assertNotNull(priceRules);
        assertEquals(chargeAmount, priceRules.amount());
        assertEquals(weekdayCharge, priceRules.weekday());
        assertEquals(weekendCharge, priceRules.weekend());
        assertEquals(holidayCharge, priceRules.holiday());
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
        final Map<String, PriceRules> chargeMap = Map.of(
                toolType1, new PriceRules(toolType1, chargeAmount1, weekdayCharge1, weekendCharge1, holidayCharge1),
                toolType2, new PriceRules(toolType2, chargeAmount2, weekdayCharge2, weekendCharge2, holidayCharge2)
        );

        final iPricing pricing = new PricingFromMap(chargeMap);

        final PriceRules priceRules1 = pricing.getPrice(toolType1);
        assertNotNull(priceRules1);
        assertEquals(chargeAmount1, priceRules1.amount());
        assertEquals(weekdayCharge1, priceRules1.weekday());
        assertEquals(weekendCharge1, priceRules1.weekend());
        assertEquals(holidayCharge1, priceRules1.holiday());

        final PriceRules priceRules2 = pricing.getPrice(toolType2);
        assertEquals(chargeAmount2, priceRules2.amount());
        assertEquals(weekdayCharge2, priceRules2.weekday());
        assertEquals(weekendCharge2, priceRules2.weekend());
        assertEquals(holidayCharge2, priceRules2.holiday());
    }
}
