package com.n4systems.fieldid.actions.helpers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.utils.ActionInvocationWrapper;

public class ActionInvocationTexentContextInitializer extends TenantContextInitializer {
	private final ActionInvocationWrapper actionInvocation;
	
	public ActionInvocationTexentContextInitializer(ActionInvocationWrapper actionInvocation) {
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
	protected WebSessionMap getSession() {
		return actionInvocation.getSession();
	}
	
}
