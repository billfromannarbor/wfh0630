package org.rentalpos.services;

import org.rentalpos.entities.Price;

import java.util.Collection;

/**
 * Pricing {@link Price} service for tools
 */
public interface iPricing {
    Price getPrice(String toolType);
    Collection<Price> getAllPrices();
}