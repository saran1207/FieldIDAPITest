package com.n4systems.fieldid.utils;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.helpers.ActionInvocationTexentContextInitializer;
import com.n4systems.fieldid.actions.helpers.FieldIdURI;
import com.n4systems.util.ServiceLocator;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class NonBrandedInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionInvocationWrapper invocationWrapper = new ActionInvocationWrapper(invocation);
		String url = new UrlArchive("none", invocationWrapper.getRequest(), invocationWrapper.getHttpSession()).extractCurrentUrl();
		
		FieldIdURI fieldIdURI = new FieldIdURI(invocationWrapper, null);
		if (!fieldIdURI.isNonBrandedUrl()) {
			String brandedUrl = fieldIdURI.baseNonBrandedUrl() + url;
			
			invocationWrapper.getAction().setRedirectUrl(brandedUrl);
						
			return AbstractAction.REDIRECT_TO_URL;
		}

		new ActionInvocationTexentContextInitializer(invocationWrapper, ServiceLocator.getPersistenceManager()).destroyContext();	
		return invocation.invoke();
	}

}
