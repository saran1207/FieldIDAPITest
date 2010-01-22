package com.n4systems.fieldid.utils;

import com.n4systems.fieldid.actions.helpers.ActionInvocationTexentContextInitializer;
import com.n4systems.fieldid.actions.helpers.TenantContextInitializer;
import com.n4systems.fieldid.actions.helpers.UnbrandedDomainException;
import com.n4systems.fieldid.permissions.NoValidTenantSelectedException;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class RequiredTenantInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionInvocationWrapper invocationWrapper = new ActionInvocationWrapper(invocation);
		try {
			TenantContextInitializer selectedTenant = new ActionInvocationTexentContextInitializer(invocationWrapper);
			selectedTenant.init();
			return invocation.invoke();
		} catch (NoValidTenantSelectedException e) {
			return "tenant_missing";
		} catch (UnbrandedDomainException e) {
			return "tenant_missing";
		}
		
		
		
	}

}
