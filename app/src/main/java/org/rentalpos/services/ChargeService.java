package org.rentalpos.services;

import org.rentalpos.entities.Charge;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

public class ChargeService implements iChargeService {
    final Map<String, Charge> chargeMap;

    public ChargeService(@Nonnull Map<String, Charge> chargeMap) {
        this.chargeMap = chargeMap;
    }

    public Charge findCharge(@Nonnull String toolType) {
        return chargeMap.get(toolType);
    }

    @Override
    public Collection<Charge> getAllCharges() {
        return chargeMap.values();
    }
}