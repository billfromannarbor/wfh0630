package org.rentalpos;

import org.apache.commons.cli.*;
import org.rentalpos.entities.PriceRules;
import org.rentalpos.entities.RentalAgreement;
import org.rentalpos.services.*;
import org.rentalpos.strategies.SimpleChargeDaysStrategy;
import org.rentalpos.strategies.iChargeDaysStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Represents a very basic CLI using {@link RentalPOS}
 * The toolcode, date, rentalDays and percentageDiscount are passed in on the command line
 * the parameters are then validated and {@link RentalPOS#checkout} is called.
 */
public class RentalPOSApp {
    iInventory inventory;
    iPricing pricing;
    iRentalPOS rentalPos;
    Options options = new Options();
    CommandLineParser parser;

    public RentalPOSApp() {
        this.inventory = new InventoryFromFile("src/test/resources/inventory.csv");

        this.pricing = new PricingFromFile("src/test/resources/pricing.csv");

        final iChargeDaysStrategy chargeDaysStrategy = new SimpleChargeDaysStrategy();
        this.rentalPos = new RentalPOS(this.inventory, this.pricing, chargeDaysStrategy);

        this.options.addOption(new Option("h", "help", false, "Help"));
        this.options.addOption(new Option("l", "listtools", false, "List the available tools"));
        this.options.addOption(new Option("s", "showprices", false, "Show PriceRules List"));
        this.options.addOption(new Option("t", "tool", true, "ToolCode"));
        this.options.addOption(new Option("d", "date", true, "Checkout Date"));
        this.options.addOption(new Option("r", "rentaldays", true, "Number of days for rental"));
        this.options.addOption(new Option("p", "percentagediscount", true, "Percentage Discount"));

        this.parser = new DefaultParser();
    }

    /**
     * run the console Rental Point of Sale
     * @param args - arguments passed in from the console.
     */
    public void runCLI(final String... args) {
        System.out.println(this.getGreeting());
        if (0 == args.length) {
            this.printHelp();
        }
        else {
            final CommandLine commandLine = this.parseCommandLine(args);
            if (commandLine.hasOption("Help")) {
                this.printHelp();
            } else if (commandLine.hasOption("showprices")) {
                System.out.println(this.pricing.getAllPrices());
            } else if (commandLine.hasOption("listtools")) {
                System.out.println("" + this.inventory.getAllTools());
            }
            else if (commandLine.hasOption("tool") &&
                    commandLine.hasOption("rentaldays") &&
                    commandLine.hasOption("date") &&
                    commandLine.hasOption("percentagediscount")) {
                final int rentalDays = Integer.parseInt(commandLine.getOptionValue("rentaldays"));
                final LocalDate checkoutDate = LocalDate.parse(commandLine.getOptionValue("date"),
                        DateTimeFormatter.ofPattern("M/d/yyyy"));
                final int percentageDiscount = Integer.parseInt(commandLine.getOptionValue("percentagediscount"));

                final RentalAgreement rentalAgreement =
                        this.rentalPos.checkout(commandLine.getOptionValue("tool"), checkoutDate, rentalDays, percentageDiscount);
                rentalAgreement.printToConsole();
            }
        }
    }

    private void printHelp() {
        final HelpFormatter formatter = HelpFormatter.builder().get();
        formatter.printHelp("Command line syntax:",
                this.options);
    }

    private String getGreeting() {
        return "Bill's Discount Rental Point of Sale system";
    }

    CommandLine parseCommandLine(final String[] args) {
        try {
            return this.parser.parse(this.options, args);
        } catch (final ParseException exp) {
            throw new RuntimeException("Parsing failed.  Reason: " + exp.getMessage());
        }
    }

    public static void main(final String[] args) {
        final var rentalPosApp = new RentalPOSApp();
        try {
            rentalPosApp.runCLI(args);
        } catch (final RuntimeException e) {
            System.err.println(e.getMessage());
            rentalPosApp.printHelp();
        }
    }
}
