# Rental Point of Sale System

This is a java code example for a simple Rental Point of Sale system

[iRentalPOS](./app/src/main/java/org/rentalpos/iRentalPOS.java)
implements a single method: checkout which returns a
[RentalAgreement](./app/src/main/java/org/rentalpos/entities/RentalAgreement.java).

The class [RentalPOS](./app/src/main/java/org/rentalpos/RentalPOS.java)
uses [iChargeDaysStrategy](./app/src/main/java/org/rentalpos/strategies/iChargeDaysStrategy.java)
and it's implementing class
[SimpleChargeDaysStrategy](./app/src/main/java/org/rentalpos/strategies/SimpleChargeDaysStrategy.java)
to calculate the number of days to charge for the rental.

The RentalPos::checkout method uses
[iInventory](./app/src/main/java/org/rentalpos/services/iInventory.java)
to find
the specified tool.
It uses
[iPricing](./app/src/main/java/org/rentalpos/services/iPricing.java)
to retrieve the pricing rules for the type of tool.

For the
[test cases](./app/src/test/java/org/rentalpos)
the interfaces
[iInventory](./app/src/main/java/org/rentalpos/services/iInventory.java)
and
[iPricing](./app/src/main/java/org/rentalpos/services/iPricing.java) 
are implemented using maps: 
[InventoryFromMap](./app/src/main/java/org/rentalpos/services/InventoryFromMap.java)
and
[PricingFromMap](./app/src/main/java/org/rentalpos/services/PricingFromMap.java)


For the
[cli](./app/src/main/java/org/rentalpos/RentalPOSApp.java),
the 
[iInventory](./app/src/main/java/org/rentalpos/services/iInventory.java)
and
[iPricing](./app/src/main/java/org/rentalpos/services/iPricing.java)
interfaces are implemented using a file reader:
[InventoryFromFile](./app/src/main/java/org/rentalpos/services/InventoryFromFile.java)
and
[PricingFromFile](./app/src/main/java/org/rentalpos/services/PricingFromFile.java)

To run the tests:
`./gradlew test`

To run the cli:
`./gradlew run --args="-p50 -r5 -d9/3/2015 -tJAKR"`
```
usage: Command line syntax:
 -d,--date <arg>                 Checkout Date
 -h,--help                       Help
 -l,--listtools                  List the available tools
 -p,--percentagediscount <arg>   Percentage Discount
 -r,--rentaldays <arg>           Number of days for rental
 -s,--showprices                 Show Price List
 -t,--tool <arg>                 ToolCode
```

