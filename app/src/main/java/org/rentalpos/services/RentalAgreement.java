package org.rentalpos.services;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RentalAgreement
{
    String toolCode;
    String toolType;
    String brand;
    int rentalDays;
    LocalDate checkoutDate;
    LocalDate dueDate;
    BigDecimal dailyRentalCharge;
    int chargeDays;
    BigDecimal prediscountCharge;
    int discountPercentage;
    BigDecimal discountAmount;
    BigDecimal finalCharge;
}

