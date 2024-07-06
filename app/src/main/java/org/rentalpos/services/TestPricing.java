package org.rentalpos.services;

import org.rentalpos.entities.Price;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

/**
 * Map based pricing {@link Price} service for tools
 */
public class TestPricing implements iPricing {
    final Map<String, Price> priceMap;

    public TestPricing(@Nonnull final Map<String, Price> priceMap) {
        this.priceMap = priceMap;
    }

    /**
     * Returns a price for the passed in toolType
     * @param toolType - type of tool
     * @return {@link Price}
     */
    public Price getPrice(@Nonnull final String toolType) {
        return this.priceMap.get(toolType);
    }

    /**
     * Return all Prices in the map as a Collection
     * @return Collection
     */
    @Override
    public Collection<Price> getAllPrices() {
        return this.priceMap.values();
    }
}