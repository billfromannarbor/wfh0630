package org.rentalpos.entities;

import java.math.BigDecimal;

public record Charge(BigDecimal amount, boolean weekday, boolean weekend, boolean holiday) {
    public Charge {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
    }
}