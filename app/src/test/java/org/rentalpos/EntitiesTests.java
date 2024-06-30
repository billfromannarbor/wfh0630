package org.rentalpos;

import org.junit.Test;
import org.rentalpos.entities.Charge;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class EntitiesTests {

    @Test
    public void chargeWithNullAmount() {
        Charge charge = new Charge(null, false, false, false );
        assertEquals(new BigDecimal("0"), charge.amount());
    }

}
