package org.rentalpos.services;

import org.rentalpos.entities.DayCount;

/**
 * Used to implement a strategy for categorizing days
 */
public interface iDayCounter {
   /**
    * Return count of days grouped into weekdays, weekends, or holidays
    * @return {@link DayCount}
    */
   DayCount getDayCount();
}
