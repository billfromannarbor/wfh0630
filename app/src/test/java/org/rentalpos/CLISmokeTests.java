package org.rentalpos;

import org.junit.Test;

//These tests don't check the console output, they call simple call the App and insure it doesn't throw an exception
public class CLISmokeTests {
    //No Args should show help
    @Test
    public void testNoArgumentsPassedIn() {
        RentalPosApp rentalPosApp = new RentalPosApp();
        rentalPosApp.runCLI();
    }

    @Test
    public void testShowHelpStatement() {
        RentalPosApp rentalPosApp = new RentalPosApp();
        rentalPosApp.runCLI("-h");
        rentalPosApp.runCLI("--help");
    }

    @Test
    public void testShowToolCodes() {
        RentalPosApp rentalPosApp = new RentalPosApp();
        rentalPosApp.runCLI("-l");
        rentalPosApp.runCLI("--listtools");
    }

    @Test
    public void testShowPrices() {
        RentalPosApp rentalPosApp = new RentalPosApp();
        rentalPosApp.runCLI("-s");
        rentalPosApp.runCLI("--showprices");
    }

    @Test
    public void rentalPercentageTooHigh() {
        RentalPosApp rentalPosApp = new RentalPosApp();
        rentalPosApp.runCLI("-p101", "-r5", "-d9/3/2015", "-tJAKR");
    }


}
