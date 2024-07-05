package org.rentalpos;

import org.junit.Test;
import org.rentalpos.entities.Price;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class EntitiesTests {

    @Test
    public void chargeWithNullAmount() {
        Price price = new Price("",null, false, false, false );
        assertEquals(new BigDecimal("0"), price.amount());
    }

}
