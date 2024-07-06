package org.rentalpos.services;

import org.rentalpos.entities.Tool;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

/**
 * Map based Inventory Service
 */
public class TestInventory implements iInventory {
	final Map<String,Tool> tools;

	public TestInventory(@Nonnull final Map<String,Tool> tools) {
		this.tools = tools;
	}

	/**
	 * Returns a tool given a specified toolcode
	 * @param toolCode - pass in a string representing a key to the encapsulated map
	 * @return {@link Tool}
	 */
	public Tool getTool(@Nonnull final String toolCode){
		return this.tools.get(toolCode);
	}

	/**
	 * Return all the tools in the map as a Collection
	 * @return Collection
	 */
	@Override
	public Collection<Tool> getAllTools() {
		return this.tools.values();
	}
}