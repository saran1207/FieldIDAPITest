package com.n4systems.fieldid.utils;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginInterceptor extends AbstractInterceptor implements StrutsStatics {

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionInvocationWrapper invocationWrapper = new ActionInvocationWrapper(invocation);
		
		WebSessionMap session = invocationWrapper.getSession();
		if (!session.isLoggedIn()) {
			getForwardingUrl(invocationWrapper.getRequest(), session.getHttpSession());
			return "login";
		}

		return invocation.invoke();
	}

	private void getForwardingUrl(HttpServletRequest request, HttpSession session) {
		new UrlArchive("preLoginContext", request, session).storeUrl();
	}

}
