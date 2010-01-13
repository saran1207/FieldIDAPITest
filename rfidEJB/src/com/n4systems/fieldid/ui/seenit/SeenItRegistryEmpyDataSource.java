package com.n4systems.fieldid.ui.seenit;

import java.util.HashMap;
import java.util.Map;

import com.n4systems.model.ui.seenit.SeenItItem;


public class SeenItRegistryEmpyDataSource implements SeenItRegistryDataSource {
	public Map<SeenItItem, Boolean> loadExisting() {
		return new HashMap<SeenItItem, Boolean>();
	}

	public void updateItem(SeenItItem item, boolean seenIt) {
	}
	
	
}