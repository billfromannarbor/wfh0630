package org.rentalpos.services;

import org.rentalpos.entities.DayCount;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class DayCounter implements iDayCounter {
    private final LocalDate checkoutDate;
    private final int dayCount;
    private int weekdayCount;
    private int weekendCount;
    private int holidayCount;

    public DayCounter(LocalDate checkoutDate, int rentalDayCount) {
        this.checkoutDate = checkoutDate;
        this.dayCount = rentalDayCount;
    }

    @Override
    public DayCount getDayCount() {
        LocalDate start = checkoutDate.plusDays(1);
        LocalDate end = start.plusDays(dayCount);

        start.datesUntil(end)
                .forEach(date -> {
                    if (isHoliday(date))
                        holidayCount++;
                    else if (isWeekend(date))
                        weekendCount++;
                    else
                        weekdayCount++;
                });

        return new DayCount(weekdayCount,weekendCount,holidayCount);
    }

    boolean isHoliday(LocalDate date) {
        boolean isHoliday = false;
        //July 4th Holiday
        if ( date.getMonth()== Month.JULY )
        {
            //July 3rd and is a Monday
            if (date.getDayOfMonth()==3 && date.getDayOfWeek()== DayOfWeek.FRIDAY)
               isHoliday=true;
           //July 4th and during the week
           else if (date.getDayOfMonth()==4
                    && date.getDayOfWeek()!= DayOfWeek.SATURDAY
                   && date.getDayOfWeek()!= DayOfWeek.SUNDAY)
               isHoliday=true;
           //July 5th and is a Monday
           else if (date.getDayOfMonth()==5 && date.getDayOfWeek()== DayOfWeek.MONDAY)
               isHoliday=true;
        }

        //Labor Day - First Monday in September
        //If this is September and a Monday and the day of the month is less than or equal to 7
        if (date.getDayOfMonth()<=7 &&
                date.getMonth()== Month.SEPTEMBER && date.getDayOfWeek()!= DayOfWeek.MONDAY)
            isHoliday=true;

        return isHoliday;
    }

    boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
