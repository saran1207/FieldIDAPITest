package com.n4systems.fieldid.utils;

import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class MissingEntityInterceptor extends AbstractInterceptor  {

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept( ActionInvocation invocation ) throws Exception {
		
		try {
			return invocation.invoke();
		} catch ( MissingEntityException e ) {
			return AbstractAction.MISSING;
		}
	}

}
