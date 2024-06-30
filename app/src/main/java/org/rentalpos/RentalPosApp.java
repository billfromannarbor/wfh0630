package org.rentalpos;

import org.rentalpos.entities.Charge;
import org.rentalpos.entities.Tool;
import org.rentalpos.services.ChargeService;
import org.rentalpos.services.InventoryService;
import org.rentalpos.services.iChargeService;
import org.rentalpos.services.iInventoryService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class RentalPosApp {
    iInventoryService inventoryService;
    iChargeService chargeService;
    iRentalPos rentalPos;

    public RentalPosApp() {
        inventoryService = new InventoryService(Map.of(
                "CHNS", new Tool("CHNS","Chainsaw","Stihl"),
                "LADW", new Tool("LADW","Ladder","Werner"),
                "JAKD", new Tool("JAKD","Jackhammer","DeWalt"),
                "JAKR", new Tool("JAKR","Jackhammer","Ridgid")
        ));

        chargeService = new ChargeService(Map.of(
                "Ladder", new Charge(BigDecimal.valueOf(1.99), true, true, false),
                "Chainsaw", new Charge(BigDecimal.valueOf(1.49), true, false, true),
                "Jackhammer", new Charge(BigDecimal.valueOf(2.99), true, false, false)
        ));

        rentalPos = new RentalPos(inventoryService, chargeService);
    }

    public String getGreeting() {
        return "Bill's Discount Rental Point of Sale system";
    }

    private void run() {
        System.out.println(getGreeting());
        String toolCode = "CHNS";
        LocalDate checkoutDate = LocalDate.now();
        int rentalDayCount = 6;
        int discountPercentage=50;

        //String toolCodePrompt = System.console().readLine();
        //String checkoutDatePrompt = System.console().readLine();
        //String rentalDayCountPrompt = System.console().readLine();
        //String discountPercentagePrompt = System.console().readLine();

        rentalPos.checkout(toolCode, checkoutDate, rentalDayCount, discountPercentage).printToConsole();
    }

    public static void main(String[] args) {
        var rentalPosApp = new RentalPosApp();
        rentalPosApp.run();
    }
}
