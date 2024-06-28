package org.rentalpos.services;

import org.rentalpos.entities.Tool;

import javax.annotation.Nonnull;
import java.util.Map;

public class InventoryService implements iInventoryService {
	final Map<String,Tool> tools;

	public InventoryService(@Nonnull Map<String,Tool> tools) {
		this.tools = tools;
	}

	public Tool findTool(@Nonnull String toolCode){
		return tools.get(toolCode);
	}
}