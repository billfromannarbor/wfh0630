package org.rentalpos.entities;

/**
 * Count days into one of three categories
 * @param weekdays
 * @param weekendDays
 * @param holidays
 */
public record DayCount(int weekdays, int weekendDays, int holidays) {
}
