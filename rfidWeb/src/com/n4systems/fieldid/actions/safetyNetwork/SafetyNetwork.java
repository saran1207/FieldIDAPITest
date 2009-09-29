package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;

public class SafetyNetwork extends AbstractAction {

	public SafetyNetwork(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	public String doShow() {
		return SUCCESS;
	}

}
