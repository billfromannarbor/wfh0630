package org.rentalpos.services;

import org.rentalpos.entities.Tool;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

/**
 * A File based implementation of of Inventory
 */
public class InventoryFromFile implements iInventory {
    iInventory inventoryMap;

    public InventoryFromFile(String filePath) {
        HashMap<String, Tool> map = new HashMap<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                String[] keyValuePair = line.split(",", 3);
                if (keyValuePair.length >= 3) {
                    String toolCode = keyValuePair[0];
                    String toolType = keyValuePair[1];
                    String brand = keyValuePair[2];

                    map.putIfAbsent(toolCode, new Tool(toolCode, toolType, brand));
                } else {
                    System.out.println("Key:Value not found in line, ignoring: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.inventoryMap = new InventoryFromMap(map);
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
