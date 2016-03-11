package com.n4systems.fieldid.utils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import rfid.web.helper.SessionUser;

public class EULAInterceptor extends AbstractInterceptor {

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionInvocationWrapper invocationWrapper = new ActionInvocationWrapper(invocation);
		
		SessionUser user = invocationWrapper.getSessionUser();
		
		if (user.isAdmin() && !invocationWrapper.getSession().getEulaAcceptance().isLatestEulaAccepted()) {
			return "require_eula";
		} 
		return invocation.invoke();
	}

}
