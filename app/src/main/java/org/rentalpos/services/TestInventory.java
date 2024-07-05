package org.rentalpos.services;

import org.rentalpos.entities.Tool;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;

public class TestInventory implements iInventory {
	final Map<String,Tool> tools;

	public TestInventory(@Nonnull Map<String,Tool> tools) {
		this.tools = tools;
	}

	public Tool getTool(@Nonnull String toolCode){
		return tools.get(toolCode);
	}

	@Override
	public Collection<Tool> getAllTools() {
		return tools.values();
	}
}