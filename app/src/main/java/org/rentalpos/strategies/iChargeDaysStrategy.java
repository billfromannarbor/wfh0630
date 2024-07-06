package org.rentalpos.strategies;

import org.rentalpos.entities.PriceRules;

import java.time.LocalDate;

public interface iChargeDaysStrategy {
    int getNumberOfChargeDays(LocalDate checkoutDate, int rentalDayCount, PriceRules priceRules);
}
