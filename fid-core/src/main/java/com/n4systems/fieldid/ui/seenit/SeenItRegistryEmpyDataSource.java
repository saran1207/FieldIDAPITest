package com.n4systems.fieldid.ui.seenit;

import com.n4systems.model.ui.seenit.SeenItItem;

import java.util.HashMap;
import java.util.Map;


public class SeenItRegistryEmpyDataSource implements SeenItRegistryDataSource {
	public Map<SeenItItem, Boolean> loadExisting() {
		return new HashMap<SeenItItem, Boolean>();
	}

	public void updateItem(SeenItItem item, boolean seenIt) {
	}
	
	
}