package com.n4systems.fieldid.utils;

import com.n4systems.fieldid.ui.seenit.SeenItRegistry;
import com.n4systems.model.ui.seenit.SeenItItem;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import rfid.web.helper.SessionUser;

public class SetupWizardFirstTimeInterceptor extends AbstractInterceptor {


	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionInvocationWrapper invocationWrapper = new ActionInvocationWrapper(invocation);
		
		SessionUser user = invocationWrapper.getSessionUser();
		
		if (forceUserToSetupWizard(user, invocationWrapper.getSession().getSeenItRegistry()) ) {
			return "startSetupWizard";
		} 
		
		return invocation.invoke();
	}

	protected boolean forceUserToSetupWizard(SessionUser user, SeenItRegistry seenItRegistry) {
		return user.isAdmin() && !seenItRegistry.haveISeen(SeenItItem.SetupWizard);
	}
}
