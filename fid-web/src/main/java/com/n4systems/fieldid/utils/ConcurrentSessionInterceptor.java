package com.n4systems.fieldid.utils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ConcurrentSessionInterceptor extends AbstractInterceptor {

	@Override
	public String intercept(ActionInvocation object) throws Exception {
		ActionInvocationWrapper invocation = new ActionInvocationWrapper(object);
		return invocation.getSession().isBooted() ? "sessionBooted" : object.invoke();
	}

}
