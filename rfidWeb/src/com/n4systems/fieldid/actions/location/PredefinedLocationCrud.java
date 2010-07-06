package com.n4systems.fieldid.actions.location;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;

public class PredefinedLocationCrud extends AbstractCrud {

	public PredefinedLocationCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initMemberFields() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		// TODO Auto-generated method stub

	}
	
	
	public String doList(){
		return SUCCESS;
	}

}
