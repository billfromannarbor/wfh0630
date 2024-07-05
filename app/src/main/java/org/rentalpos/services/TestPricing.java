package org.rentalpos.services;

import org.rentalpos.entities.Price;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

public class TestPricing implements iPricing {
    final Map<String, Price> chargeMap;

    public TestPricing(@Nonnull Map<String, Price> chargeMap) {
        this.chargeMap = chargeMap;
    }

    public Price findCharge(@Nonnull String toolType) {
        return chargeMap.get(toolType);
    }

    @Override
    public Collection<Price> getAllCharges() {
        return chargeMap.values();
    }
}