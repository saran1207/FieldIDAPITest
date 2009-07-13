package com.n4systems.fieldid.actions.helpers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.utils.ActionInvocationWrapper;

public class ActionInvocationTexentContextInitializer extends TenantContextInitializer {

	
	private final ActionInvocationWrapper actionInvocation;
	
	public ActionInvocationTexentContextInitializer(ActionInvocationWrapper actionInvocation, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.actionInvocation = actionInvocation;
	}
	
	@Override
	protected HttpServletRequest getRequest() {
		return actionInvocation.getRequest();
	}

	@Override
	protected HttpServletResponse getResponse() {
		return actionInvocation.getResponse();
	}
 
	@Override
	@SuppressWarnings("unchecked")
	protected Map getSession() {
		return actionInvocation.getSessionMap();
	}
	
}
