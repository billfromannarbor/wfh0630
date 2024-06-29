package org.rentalpos;

import java.time.LocalDate;

public interface iRentalDays {
    int getDayCount();

    LocalDate getCheckoutDate();

    int getWeekdayCount();

    int getWeekendCount();

    int getHolidayCount();
}
