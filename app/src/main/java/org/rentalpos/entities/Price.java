package org.rentalpos.entities;

import java.math.BigDecimal;

public record Price(String toolType, BigDecimal amount, boolean weekday, boolean weekend, boolean holiday) {
    public Price {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
    }
}