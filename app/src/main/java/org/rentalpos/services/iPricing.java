package org.rentalpos.services;

import org.rentalpos.entities.PriceRules;

import java.util.Collection;

/**
 * Pricing {@link PriceRules} service for tools
 */
public interface iPricing {
    PriceRules getPrice(String toolType);
    Collection<PriceRules> getAllPrices();
}