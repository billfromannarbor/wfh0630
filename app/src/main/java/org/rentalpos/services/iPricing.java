package org.rentalpos.services;

import org.rentalpos.entities.Price;

import java.util.Collection;

public interface iPricing {
    Price findCharge(String toolType);

    Collection<Price> getAllCharges();
}