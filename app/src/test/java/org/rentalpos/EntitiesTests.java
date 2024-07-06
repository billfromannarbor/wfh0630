package org.rentalpos;

import org.junit.Test;
import org.rentalpos.entities.PriceRules;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class EntitiesTests {

    @Test
    public void chargeWithNullAmount() {
        final PriceRules priceRules = new PriceRules("",null, false, false, false );
        assertEquals(new BigDecimal("0"), priceRules.amount());
    }

}
