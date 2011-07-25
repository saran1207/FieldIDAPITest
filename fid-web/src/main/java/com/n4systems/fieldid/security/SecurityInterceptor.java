package com.n4systems.fieldid.security;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.utils.ActionInvocationWrapper;
import com.n4systems.security.AuditLoggingContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * A Struts 2 interceptor used to initialize and clear the SecurityContext.  Should be run immediately after the
 * LoginInterceptor.
 */
public class SecurityInterceptor extends AbstractInterceptor implements StrutsStatics {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(SecurityInterceptor.class);
	
	@Override
	public String intercept(ActionInvocation action) throws Exception {
		ActionInvocationWrapper invokeWrapper = new ActionInvocationWrapper(action);
		
		String sessionId = invokeWrapper.getSession().getId();
		SessionUser user = invokeWrapper.getSessionUser();
		
		String actionResult;
		try {
			// initialize the context and invoke the action
			AuditLoggingContext.initialize(user.getUniqueID(), user.getTenant().getId(), sessionId);
			actionResult = action.invoke();
			
		} catch(SecurityException e) {
			// log the exception and return BAD_SECURITY_REQUEST
			logger.error("SecurityException caught during action", e);
			actionResult = AbstractAction.INVALID_SECURITY;
		} finally {
			// reset the context after the action has run
			AuditLoggingContext.clear();
		}
		
		return actionResult;
	}

}
