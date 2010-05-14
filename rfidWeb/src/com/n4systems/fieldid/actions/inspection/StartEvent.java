package com.n4systems.fieldid.actions.inspection;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.SimpleAction;
import com.n4systems.util.ConfigEntry;

public class StartEvent extends SimpleAction {

	public StartEvent(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	
	public Long getMaxAssetsFromMassEvent() {
		return getConfigContext().getLong(ConfigEntry.MAX_SIZE_FOR_MULTI_INSPECT, getTenantId());
	}

}
