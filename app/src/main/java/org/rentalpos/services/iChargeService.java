package org.rentalpos.services;

import org.rentalpos.entities.Charge;

import java.util.Collection;

public interface iChargeService {
    Charge findCharge(String toolType);

    Collection<Charge> getAllCharges();
}