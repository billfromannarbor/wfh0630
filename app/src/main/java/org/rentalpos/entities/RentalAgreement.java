package org.rentalpos.entities;

import lombok.Builder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
public record RentalAgreement(String toolCode, String toolType, String brand,
                              int rentalDays, LocalDate checkoutDate,
                              LocalDate dueDate, BigDecimal dailyRentalCharge,
                              int chargeDays, BigDecimal preDiscountCharge,
                              int discountPercentage, BigDecimal discountAmount, BigDecimal finalCharge)
{
    public void printToConsole() {
        //Format dates, currency, percent value
        var dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        var percentageFormatter = new DecimalFormat("###");
        var currencyFormatter = new DecimalFormat("$#,###.##");
        System.out.println("Tool code: " + toolCode);
        System.out.println("Tool type: " + toolType);
        System.out.println("Brand: " + brand);
        System.out.println("Rental days: " + rentalDays);
        System.out.println("Checkout date: " + checkoutDate.format(dateFormatter));
        System.out.println("Due date: " + dueDate.format(dateFormatter));
        System.out.println("Daily rental charge: " + currencyFormatter.format(dailyRentalCharge));
        System.out.println("Charge days: " + chargeDays);
        System.out.println("PreDiscount charge: " + currencyFormatter.format(preDiscountCharge));
        System.out.printf("Discount percentage: %s%%\n",percentageFormatter.format(discountPercentage));
        System.out.println("Discount amount: " + currencyFormatter.format(discountAmount));
        System.out.println("Final Charge: " + currencyFormatter.format(finalCharge));
    }
}

