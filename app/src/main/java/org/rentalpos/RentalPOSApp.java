package org.rentalpos;

import org.apache.commons.cli.*;
import org.rentalpos.entities.Price;
import org.rentalpos.entities.RentalAgreement;
import org.rentalpos.entities.Tool;
import org.rentalpos.services.TestPricing;
import org.rentalpos.services.TestInventory;
import org.rentalpos.services.iPricing;
import org.rentalpos.services.iInventory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RentalPOSApp {
    iInventory inventoryService;
    iPricing pricing;
    iRentalPOS rentalPos;
    Options options = new Options();
    CommandLineParser parser;

    public RentalPOSApp() {
        inventoryService = new TestInventory(Map.of(
                "CHNS", new Tool("CHNS", "Chainsaw", "Stihl"),
                "LADW", new Tool("LADW", "Ladder", "Werner"),
                "JAKD", new Tool("JAKD", "Jackhammer", "DeWalt"),
                "JAKR", new Tool("JAKR", "Jackhammer", "Ridgid")
        ));

        pricing = new TestPricing(Map.of(
                "Ladder", new Price("Ladder", BigDecimal.valueOf(1.99), true, true, false),
                "Chainsaw", new Price("Chainsaw",BigDecimal.valueOf(1.49), true, false, true),
                "Jackhammer", new Price("Jackhammer", BigDecimal.valueOf(2.99), true, false, false)
        ));

        rentalPos = new RentalPOS(inventoryService, pricing);

        options.addOption(new Option("h", "help", false, "Help"));
        options.addOption(new Option("l", "listtools", false, "Percentage Discount"));
        options.addOption(new Option("s", "showprices", false, "Show Price List"));
        options.addOption(new Option("t", "tool", true, "ToolCode"));
        options.addOption(new Option("d", "date", true, "Checkout Date"));
        options.addOption(new Option("r", "rentaldays", true, "Number of days for rental"));
        options.addOption(new Option("p", "percentagediscount", true, "Percentage Discount"));

        parser = new DefaultParser();
    }

    public void runCLI(String... args) {
        System.out.println(getGreeting());
        if (args.length == 0) {
            printHelp();
        }
        else {
            var commandLine = parseCommandLine(args);

            if (commandLine.hasOption("Help")) {
                printHelp();
            } else if (commandLine.hasOption("showprices")) {
                System.out.println(pricing.getAllCharges());
            } else if (commandLine.hasOption("listtools")) {
                System.out.println("" + inventoryService.getAllTools());
            }
            else if (commandLine.hasOption("tool") &&
                    commandLine.hasOption("rentaldays") &&
                    commandLine.hasOption("date") &&
                    commandLine.hasOption("percentagediscount")) {
                int rentalDays = Integer.parseInt(commandLine.getOptionValue("rentaldays"));
                LocalDate checkoutDate = LocalDate.parse(commandLine.getOptionValue("date"),
                        DateTimeFormatter.ofPattern("M/d/yyyy"));
                int percentageDiscount = Integer.parseInt(commandLine.getOptionValue("percentagediscount"));

                RentalAgreement rentalAgreement =
                        rentalPos.checkout(commandLine.getOptionValue("tool"), checkoutDate, rentalDays, percentageDiscount);
                rentalAgreement.printToConsole();
            }
        }




        //rentalPos.checkout(toolCode, checkoutDate, rentalDayCount, discountPercentage).printToConsole();
    }

    private void printHelp() {
        HelpFormatter formatter = HelpFormatter.builder().get();
        formatter.printHelp("Command line syntax:",
                options);
    }

    public String getGreeting() {
        return "Bill's Discount Rental Point of Sale system";
    }

    CommandLine parseCommandLine(String[] args) {
        try {
            return parser.parse(options, args);
        } catch (ParseException exp) {
            throw new RuntimeException("Parsing failed.  Reason: " + exp.getMessage());
        }
    }

    public static void main(String[] args) {
        var rentalPosApp = new RentalPOSApp();
        try {
            rentalPosApp.runCLI(args);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            rentalPosApp.printHelp();
        }
    }


}
