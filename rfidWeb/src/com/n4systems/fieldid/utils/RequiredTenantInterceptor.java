package com.n4systems.fieldid.utils;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.helpers.ActionInvocationTexentContextInitializer;
import com.n4systems.fieldid.actions.helpers.FieldIdURI;
import com.n4systems.fieldid.actions.helpers.IncorrectTenantDomain;
import com.n4systems.fieldid.actions.helpers.TenantContextInitializer;
import com.n4systems.fieldid.permissions.NoValidTenantSelectedException;
import com.n4systems.util.ServiceLocator;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class RequiredTenantInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionInvocationWrapper invocationWrapper = new ActionInvocationWrapper(invocation);
		try {
			TenantContextInitializer selectedTenant = new ActionInvocationTexentContextInitializer(invocationWrapper, ServiceLocator.getPersistenceManager());
			selectedTenant.init();
		} catch (NoValidTenantSelectedException e) {
			invocationWrapper.getAction().addFlashErrorText("error.cannot_find_company_id");
			return "tenant_missing";
		} catch (IncorrectTenantDomain e) {
			// extract url and correct to the proper tenant domain.
			
			String tenantName = (String)CookieFactory.findCookieValue("companyID", invocationWrapper.getRequest());
			String url = new UrlArchive("none", invocationWrapper.getRequest(), invocationWrapper.getHttpSession()).extractCurrentUrl();
			String brandedUrl = new FieldIdURI(invocationWrapper, tenantName).baseBrandedUrl() + url;
			invocationWrapper.getAction().setRedirectUrl(brandedUrl);
						
			return AbstractAction.REDIRECT_TO_URL;
		}
		
		return invocation.invoke();
		
	}

}
