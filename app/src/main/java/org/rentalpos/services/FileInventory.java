package org.rentalpos.services;

import org.rentalpos.entities.Tool;

import javax.annotation.Nonnull;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileReaderInventory implements iInventory {
    iInventory inventoryMap;

    public FileReaderInventory() {
        this.inventoryMap = new MapBasedInventory(Map.of(
                "CHNS", new Tool("CHNS","Chainsaw","Stihl"),
                "LADW", new Tool("LADW","Ladder","Werner"),
                "JAKD", new Tool("JAKD","Jackhammer","DeWalt"),
                "JAKR", new Tool("JAKR","Jackhammer","Ridgid")
        ));
    }

    @Override
    public Tool getTool(String toolCode) {
        return this.inventoryMap.getTool(toolCode);
    }

    @Override
    public Collection<Tool> getAllTools() {
        return this.inventoryMap.getAllTools();
    }
}
