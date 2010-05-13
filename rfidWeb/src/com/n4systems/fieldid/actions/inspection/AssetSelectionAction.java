package com.n4systems.fieldid.actions.inspection;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.util.ConfigEntry;

public class AssetSelectionAction extends AbstractAction {

	public AssetSelectionAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public String doSelection() {
		return SUCCESS;
	}
	
	public Long getListLimitSize() {
		return getConfigContext().getLong(ConfigEntry.MAX_SIZE_FOR_MULTI_INSPECT, getTenantId());
	}
}
