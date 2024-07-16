package org.rentalpos.strategies;

import org.rentalpos.entities.PriceRules;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Figure out the number of charge days by counting the number of weekdays, weekends and sundays and then
 * excluding those days from the count if the priceRules say the day type isn't chargeable
 */
public class SimpleChargeDaysStrategy implements iChargeDaysStrategy {
    @Override
    public int getNumberOfChargeDays(final LocalDate checkoutDate, final int rentalDayCount,
                                     final PriceRules priceRules) {
        final LocalDate start = checkoutDate.plusDays(1L);
        final LocalDate end = start.plusDays((long) rentalDayCount);
        final AtomicInteger weekdayCount = new AtomicInteger();
        final AtomicInteger weekendCount = new AtomicInteger();
        final AtomicInteger holidayCount = new AtomicInteger();

        Stream<LocalDate> localDateStream = start.datesUntil(end);

        localDateStream.forEach(date -> {
            if (this.isHoliday(date))
                holidayCount.getAndIncrement();
            else if (this.isWeekend(date))
                weekendCount.getAndIncrement();
            else
                weekdayCount.getAndIncrement();
        });
        final int daysWithoutCharge = getDaysWithoutCharge(priceRules, weekdayCount.get(), weekendCount.get(), holidayCount.get());
        return rentalDayCount - daysWithoutCharge;
    }

    int getDaysWithoutCharge(final PriceRules priceRules, final int weekdayCount,
                             final int weekendCount, final int holidayCount) {
        int daysWithoutCharge = 0;
        if (!priceRules.holiday())
            daysWithoutCharge+=holidayCount;
        if (!priceRules.weekend())
            daysWithoutCharge+=weekendCount;
        if (!priceRules.weekday())
            daysWithoutCharge+=weekdayCount;

        return daysWithoutCharge;
    }

    /**
     * A holiday is the fourth of july or labor day
     * If the fourth of july is on a weekend, it's celbrated on closest weekday
     * @param date date to check
     * @return true if it's a holiday
     */
    private boolean isHoliday(final LocalDate date) {
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
                Month.SEPTEMBER == date.getMonth() && DayOfWeek.MONDAY == date.getDayOfWeek()) {
            isHoliday = true;

        }

        return isHoliday;
    }

    boolean isWeekend(final LocalDate date) {
        return DayOfWeek.SATURDAY == date.getDayOfWeek() || DayOfWeek.SUNDAY == date.getDayOfWeek();
    }
}
