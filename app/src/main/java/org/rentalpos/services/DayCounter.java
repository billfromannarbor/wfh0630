package org.rentalpos.services;

import org.rentalpos.entities.DayCount;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

/**
 * Uses checkoutDate and rentalDayCount to group days in weekdays, weekends or holidays
 */
public class DayCounter implements iDayCounter {
    private final LocalDate checkoutDate;
    private final int dayCount;
    private int weekdayCount;
    private int weekendCount;
    private int holidayCount;

    public DayCounter(final LocalDate checkoutDate, final int rentalDayCount) {
        this.checkoutDate = checkoutDate;
        dayCount = rentalDayCount;
    }

    /**
     * Starting the day after checkout, group all the dates into
     * weekdays, weekends or holidays
     * @return {@link DayCount}
     */
    @Override
    public DayCount getDayCount() {
        final LocalDate start = this.checkoutDate.plusDays(1);
        final LocalDate end = start.plusDays((long) this.dayCount);

        start.datesUntil(end)
                .forEach(date -> {
                    if (this.isHoliday(date))
                        this.holidayCount++;
                    else if (this.isWeekend(date))
                        this.weekendCount++;
                    else
                        this.weekdayCount++;
                });

        return new DayCount(this.weekdayCount, this.weekendCount, this.holidayCount);
    }

    /**
     * A holiday is the fourth of july or labor day
     * If the fourth of july is on a weekend, it's celbrated on closest weekday
     * @param date date to check
     * @return true if it's a holiday
     */
    boolean isHoliday(final LocalDate date) {
        boolean isHoliday = false;
        //July 4th Holiday
        if (Month.JULY == date.getMonth())
        {
            //July 3rd and is a Monday
            if (3 == date.getDayOfMonth() && DayOfWeek.FRIDAY == date.getDayOfWeek())
               isHoliday=true;
           //July 4th and during the week
           else if (4 == date.getDayOfMonth()
                    && DayOfWeek.SATURDAY != date.getDayOfWeek()
                   && DayOfWeek.SUNDAY != date.getDayOfWeek())
               isHoliday=true;
           //July 5th and is a Monday
           else if (5 == date.getDayOfMonth() && DayOfWeek.MONDAY == date.getDayOfWeek())
               isHoliday=true;
        }

        //Labor Day - First Monday in September
        //If this is September and a Monday and the day of the month is less than or equal to 7
        if (7 >= date.getDayOfMonth() &&
                Month.SEPTEMBER == date.getMonth() && DayOfWeek.MONDAY != date.getDayOfWeek())
            isHoliday=true;

        return isHoliday;
    }

    boolean isWeekend(final LocalDate date) {
        return DayOfWeek.SATURDAY == date.getDayOfWeek() || DayOfWeek.SUNDAY == date.getDayOfWeek();
    }
}
