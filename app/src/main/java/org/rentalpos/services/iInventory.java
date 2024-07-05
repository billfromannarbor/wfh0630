package org.rentalpos.services;

import org.rentalpos.entities.Tool;

import java.util.Collection;

public interface iInventory {
	Tool getTool(String toolCode);

    Collection<Tool> getAllTools();
}