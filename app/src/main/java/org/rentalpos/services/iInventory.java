package org.rentalpos.services;

import org.rentalpos.entities.RentalAgreement;
import org.rentalpos.entities.Tool;

import java.util.Collection;

/**
 * Inventory of Tools {@link Tool}
 */
public interface iInventory {
	Tool getTool(String toolCode);
    Collection<Tool> getAllTools();
}