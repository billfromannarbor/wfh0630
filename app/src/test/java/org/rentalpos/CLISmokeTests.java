package org.rentalpos;

import org.junit.Test;

//These tests don't check the console output, they call simple call the App and insure it doesn't throw an exception
public class CLISmokeTests {
    //No Args should show help
    @Test
    public void testNoArgumentsPassedIn() {
        RentalPOSApp rentalPosApp = new RentalPOSApp();
        rentalPosApp.runCLI();
    }

    @Test
    public void testShowHelpStatement() {
        RentalPOSApp rentalPosApp = new RentalPOSApp();
        rentalPosApp.runCLI("-h");
        rentalPosApp.runCLI("--help");
    }

    @Test
    public void testShowToolCodes() {
        RentalPOSApp rentalPosApp = new RentalPOSApp();
        rentalPosApp.runCLI("-l");
        rentalPosApp.runCLI("--listtools");
    }

    @Test
    public void testShowPrices() {
        RentalPOSApp rentalPosApp = new RentalPOSApp();
        rentalPosApp.runCLI("-s");
        rentalPosApp.runCLI("--showprices");
    }

    @Test
    public void rental() {
        RentalPOSApp rentalPosApp = new RentalPOSApp();
        rentalPosApp.runCLI("-p50", "-r5", "-d9/3/2015", "-tJAKR");
    }

    @Test(expected = IllegalArgumentException.class)
    public void rentalBadToolCode() {
        RentalPOSApp rentalPosApp = new RentalPOSApp();
        rentalPosApp.runCLI("-p50", "-r5", "-d9/3/2015", "-tJAKE");
    }

    @Test(expected = IllegalArgumentException.class)
    public void rentalPercentageTooHigh() {
        RentalPOSApp rentalPosApp = new RentalPOSApp();
        rentalPosApp.runCLI("-p101", "-r5", "-d9/3/2015", "-tJAKR");
    }




}
