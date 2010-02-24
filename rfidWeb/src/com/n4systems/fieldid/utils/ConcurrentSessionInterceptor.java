package com.n4systems.fieldid.utils;

import rfid.web.helper.SessionUser;

import com.n4systems.model.activesession.ActiveSessionLoader;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ConcurrentSessionInterceptor extends AbstractInterceptor {

	@Override
	public String intercept(ActionInvocation object) throws Exception {
		ActionInvocationWrapper action = new ActionInvocationWrapper(object);
		
		SessionUser sessionUser = action.getSessionUser();
		String sessionId = action.getSession().getId();
		
		SessionUserInUse sessionUserInUse = new SessionUserInUse(new ActiveSessionLoader());
		
		if (! sessionUserInUse.doesActiveSessionBelongTo(sessionUser.getUniqueID(), sessionId))
			action.getAction().getSession().setSessionBooted();
		
		return object.invoke();
	}

}
