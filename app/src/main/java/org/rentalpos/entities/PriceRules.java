package org.rentalpos.entities;

import java.math.BigDecimal;

/**
 * PriceRules and pricing rules for a specified toolType
 * @param toolType - tool
 * @param amount - PriceRules
 * @param weekday - Charge on weekdays
 * @param weekend - Charge on weekend days
 * @param holiday - charge on holidays
 */
public record PriceRules(String toolType, BigDecimal amount, boolean weekday, boolean weekend, boolean holiday) {
    /**
     * @param toolType - Key representing a tool
     * @param amount - Tool Rental PriceRules
     * @param weekday - Charge for weekday rentals
     * @param weekend - Charge for weekend rentals
     * @param holiday - Charge for Holiday rentals
     */
    public PriceRules {
        if (null == amount) {
            amount = BigDecimal.ZERO;
        }
    }
}