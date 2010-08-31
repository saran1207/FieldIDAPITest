package com.n4systems.fieldid.ui.seenit;


import java.util.HashMap;
import java.util.Map;

import com.n4systems.model.ui.seenit.SeenItItem;

/**
 * The seen it registry will hold if the current user has seen a one time component in the UI.  
 * This would be good for help messages or warnings, things that only need to happen once, 
 * like first time login help
 *  
 * @author aaitken
 *
 */
public class SeenItRegistryImpl implements SeenItRegistry {

	
	private final Map<SeenItItem, Boolean> registry;
	private final SeenItRegistryDataSource dataSource;

	public SeenItRegistryImpl(SeenItRegistryDataSource dataSource) {
		this.registry = new HashMap<SeenItItem, Boolean>();
		this.registry.putAll(dataSource.loadExisting());
		this.dataSource = dataSource;
		
	}

	public boolean haveISeen(SeenItItem item) {
		return (registry.containsKey(item)) ? registry.get(item) : false;
	}

	public void iHaveSeen(SeenItItem item) {
		setItemValue(item, true);
	}

	private void setItemValue(SeenItItem item, boolean itemSetting) {
		registry.put(item, itemSetting);
		dataSource.updateItem(item, itemSetting);
	}

	public void iHaveNotSeen(SeenItItem item) {
		setItemValue(item, false);
	}

	
}
