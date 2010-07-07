package com.n4systems.fieldid.actions.location;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;

public class PredefinedLocationCrud extends AbstractCrud {

	
	
	public PredefinedLocationCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	//Need to query DB for data. For now use helper to generate tree.
	protected void loadMemberFields(Long uniqueId) {
	}

	public String doList() {
		return SUCCESS;
	}

	

}
