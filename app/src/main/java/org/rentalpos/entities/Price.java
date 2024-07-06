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
    public Price {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
    }
}