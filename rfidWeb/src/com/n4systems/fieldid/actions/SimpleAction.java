package com.n4systems.fieldid.actions;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;



public class SimpleAction extends AbstractAction {
	
	
	public SimpleAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	private static final long serialVersionUID = 1L;

	public String execute() {
		return SUCCESS;
	}

}
