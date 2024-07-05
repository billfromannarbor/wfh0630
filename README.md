# Rental Point of Sale System

This is a java code example for a simple Rental Point of Sale system

Explore the code either through the [tests](./app/src/test/java/org/rentalpos/RentalPOSTests.java)
or through the Command Line Interface [cli](./app/src/main/java/org/rentalpos/RentalPOSApp.java)

To run the tests:
`./gradlew test`

To run the cli:
`run:./gradlew run --args="-p50 -r5 -d9/3/2015 -tJAKR"`
```
usage: Command line syntax:
-d,--date <arg>                 Checkout Date
-h,--help                       Help
-l,--listtools                  Percentage Discount
-p,--percentagediscount <arg>   Percentage Discount
-r,--rentaldays <arg>           Number of days for rental
-s,--showprices                 Show Price List
-t,--tool <arg>                 ToolCode
```

