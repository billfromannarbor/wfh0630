package org.rentalpos.services;

import org.rentalpos.entities.PriceRules;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

/**
 * Map based pricing {@link PriceRules} service for tools
 */
public class TestPricing implements iPricing {
    final Map<String, PriceRules> priceMap;

    public TestPricing(@Nonnull final Map<String, PriceRules> priceMap) {
        this.priceMap = priceMap;
    }

    /**
     * Returns a price for the passed in toolType
     * @param toolType - type of tool
     * @return {@link PriceRules}
     */
    public PriceRules getPrice(@Nonnull final String toolType) {
        return this.priceMap.get(toolType);
    }

    /**
     * Return all Prices in the map as a Collection
     * @return Collection
     */
    @Override
    public Collection<PriceRules> getAllPrices() {
        return this.priceMap.values();
    }
}