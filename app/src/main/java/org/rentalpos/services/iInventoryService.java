package org.rentalpos.services;

import org.rentalpos.entities.Tool;

public interface iInventoryService {
	Tool findTool(String toolCode);

}