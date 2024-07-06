package org.rentalpos.entities;

import java.math.BigDecimal;

/**
 * Price and pricing rules for a specified toolType
 * @param toolType - tool
 * @param amount - Price
 * @param weekday - Charge on weekdays
 * @param weekend - Charge on weekend days
 * @param holiday - charge on holidays
 */
public record Price(String toolType, BigDecimal amount, boolean weekday, boolean weekend, boolean holiday) {
    /**
     * @param toolType - Key representing a tool
     * @param amount - Tool Rental Price
     * @param weekday - Charge for weekday rentals
     * @param weekend - Charge for weekend rentals
     * @param holiday - Charge for Holiday rentals
     */
    public Price {
        if (null == amount) {
            amount = BigDecimal.ZERO;
        }
    }
}