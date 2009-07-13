package com.n4systems.fieldid.actions.helpers;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.utils.ActionInvocationWrapper;

public class AbstractActionTenantContextInitializer extends ActionInvocationTexentContextInitializer {

	public AbstractActionTenantContextInitializer(AbstractAction action, PersistenceManager persistenceManager) {
		super(new ActionInvocationWrapper(action.getActionContext().getActionInvocation()), persistenceManager);
		
	}
}
