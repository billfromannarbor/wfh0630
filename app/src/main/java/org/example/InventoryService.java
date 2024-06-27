package org.example;

import org.example.Tool;

import java.util.Map;

public class InventoryService implements iInventoryService {
	Map<String,Tool> tools;

	public InventoryService(Map<String,Tool> tools) {
		this.tools = tools;
	}
	public Tool findTool(String toolCode){
		return tools.get(toolCode);
	}
}