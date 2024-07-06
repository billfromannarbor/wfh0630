package org.rentalpos.entities;

import lombok.Builder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a rental agreement
 * Implements a builder to allow the RentalAgreement to be created one parameter at a time
 * @param toolCode - represents a unique tool in inventory
 * @param toolType - Type of tool, used as index for Pricing
 * @param brand - Brand of the tool
 * @param rentalDays - Number of days for rental
 * @param checkoutDate - Day of checkout
 * @param dueDate - When the rental is due
 * @param dailyRentalCharge - The rental charge determined from Pricing
 * @param chargeDays - The number of charge days based determined from Pricing
 * @param preDiscountCharge - The charge before appling discount
 * @param discountPercentage - Percentage discount
 * @param discountAmount - Discount amount
 * @param finalCharge - Final Charge
 */
@Builder
public record RentalAgreement(String toolCode, String toolType, String brand,
                              int rentalDays, LocalDate checkoutDate,
                              LocalDate dueDate, BigDecimal dailyRentalCharge,
                              int chargeDays, BigDecimal preDiscountCharge,
                              int discountPercentage, BigDecimal discountAmount, BigDecimal finalCharge)
{
    /**
     * prints a formatted version of the RentalAgreement to the console
     */
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

