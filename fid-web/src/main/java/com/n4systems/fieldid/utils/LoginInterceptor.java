package com.n4systems.fieldid.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsStatics;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.actions.utils.WebSession;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LoginInterceptor extends AbstractInterceptor implements StrutsStatics {

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionInvocationWrapper invocationWrapper = new ActionInvocationWrapper(invocation);
		
		SessionUser user = invocationWrapper.getSessionUser();
		if (isUserLoggedIn(invocationWrapper, user)) {
			getForwardingUrl(invocationWrapper.getRequest(), invocationWrapper.getSession().getHttpSession(), invocation.getInvocationContext());
			return "login";
		}

		return invocation.invoke();
	}

	private boolean isUserLoggedIn(ActionInvocationWrapper invocationWrapper, SessionUser user) {
		return user == null || !userTenantMatchesSecurityGuard(invocationWrapper.getSession(), user);
	}

	private void getForwardingUrl(HttpServletRequest request, HttpSession session, ActionContext context) {
		new UrlArchive("preLoginContext", request, session).storeUrl();
	}

	private boolean userTenantMatchesSecurityGuard(WebSession session, SessionUser user) {
		return user.getTenant().equals(session.getSecurityGuard().getTenant());
	}

}
