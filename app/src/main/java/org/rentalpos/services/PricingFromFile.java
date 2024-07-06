package org.rentalpos.services;

import org.rentalpos.entities.PriceRules;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Get Pricing Rules from a CSV File
 */
public class PricingFromFile implements iPricing {
    private iPricing pricingMap;

    /**
     * Opens file at Filepath a fills in pricingMap
     * @param filePath location of csv containing pricing rules
     */
    public PricingFromFile(String filePath) {
        Map<String,PriceRules> priceRulesMap = new HashMap<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                String[] keyValuePair = line.split(",", 5);
                if (keyValuePair.length >= 5) {
                    String toolType = keyValuePair[0];
                    String amount = keyValuePair[1];
                    String chargeOnWeekday = keyValuePair[2];
                    String chargeOnWeekend = keyValuePair[3];
                    String chargeOnHoliday = keyValuePair[4];

                    priceRulesMap.putIfAbsent(toolType, new PriceRules(toolType, new BigDecimal(amount),
                            Boolean.parseBoolean(chargeOnWeekday), Boolean.parseBoolean(chargeOnWeekend),
                            Boolean.parseBoolean(chargeOnHoliday)));
                } else {
                    throw new RuntimeException("Price Rules not found on line, ignoring all lines after: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pricingMap = new PricingFromMap(priceRulesMap);
    }

    @Override
    public PriceRules getPrice(String toolType) {
        return pricingMap.getPrice(toolType);
    }

    @Override
    public Collection<PriceRules> getAllPrices() {
        return pricingMap.getAllPrices();
    }
}
