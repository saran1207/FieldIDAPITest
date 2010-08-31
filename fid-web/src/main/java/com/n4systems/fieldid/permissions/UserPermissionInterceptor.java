package com.n4systems.fieldid.permissions;

import org.apache.log4j.Logger;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.utils.ActionInvocationWrapper;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class UserPermissionInterceptor extends AbstractInterceptor {
	private static final Logger logger = Logger.getLogger(UserPermissionInterceptor.class);
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionInvocationWrapper invocationWrapper = new ActionInvocationWrapper(invocation);
		AbstractAction action = invocationWrapper.getAction();
		UserAccessController userAccessController = new UserAccessController(action);
		
		if (userAccessController.userHasAccessToAction(invocationWrapper.getMethodName())) {
			return invocation.invoke();
		} 
		
		handlePermissionDenied(invocationWrapper, action);
		
		return "user_permission_required";
	}

	private void handlePermissionDenied(ActionInvocationWrapper invocationWrapper, AbstractAction action) {
		action.addActionErrorText("error.user_permission_required");
		logger.warn("permission has been denined to action " + action.getClass().toString() + " for method " + invocationWrapper.getMethodName() + " by User " + action.getSessionUser().getUserID());
	}

}
