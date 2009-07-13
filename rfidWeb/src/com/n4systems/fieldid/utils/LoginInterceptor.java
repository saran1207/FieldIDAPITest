package com.n4systems.fieldid.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.StrutsStatics;

import rfid.web.helper.Constants;
import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LoginInterceptor extends AbstractInterceptor implements StrutsStatics {

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		// Get the action context from the invocation so we can access the
		// HttpServletRequest and HttpSession objects.
		final ActionContext context = invocation.getInvocationContext();
		HttpServletRequest request = (HttpServletRequest) context.get(HTTP_REQUEST);
		HttpSession session = request.getSession(true);
		AbstractAction action = (AbstractAction) invocation.getAction();

		SessionUser user = (SessionUser) session.getAttribute(Constants.SESSION_USER);
		if (user == null || !userTenantMatchesSecurityGuard(action, user)) {
			getForwardingUrl(request, session, context, action);
			// User not logged in
			return "login";
		}

		return invocation.invoke();
	}

	private void getForwardingUrl(HttpServletRequest request, HttpSession session, ActionContext context, AbstractAction action) {
		new UrlArchive("preLoginContext", request, session).storeUrl();
	}

	private boolean userTenantMatchesSecurityGuard(AbstractAction action, SessionUser user) {
		return user.getTenant().equals(action.getSecurityGuard().getTenant());
	}

}
