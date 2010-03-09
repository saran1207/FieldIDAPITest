package com.n4systems.fieldid.utils;

import rfid.web.helper.SessionUser;

import com.n4systems.model.activesession.ActiveSessionLoader;
import com.n4systems.model.activesession.ActiveSessionSaver;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.time.SystemClock;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ConcurrentSessionInterceptor extends AbstractInterceptor {

	@Override
	public String intercept(ActionInvocation object) throws Exception {
		ActionInvocationWrapper invokation = new ActionInvocationWrapper(object);
		
		SessionUser sessionUser = invokation.getSessionUser();
		String sessionId = invokation.getSession().getId();
		
		SessionUserInUse sessionUserInUse = new SessionUserInUse(new ActiveSessionLoader(), ConfigContext.getCurrentContext(), new SystemClock(), new ActiveSessionSaver());
		
		if (! sessionUserInUse.doesActiveSessionBelongTo(sessionUser.getUniqueID(), sessionId)) {
			return "sessionBooted";
		}
			
		return object.invoke();
	}

}
