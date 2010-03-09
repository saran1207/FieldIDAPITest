package com.n4systems.fieldid.utils;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@SuppressWarnings("serial")
public class FlashScopeInterceptor extends AbstractInterceptor implements StrutsStatics {

	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		ActionInvocationWrapper action = new ActionInvocationWrapper(invocation);
		FlashScopeMarshaller marshaller = new FlashScopeMarshaller(action.getAction(), action.getSession().getHttpSession());
		
		marshaller.storeAndRemovePreviousFlashMessages();
		
		String actionResponse = invocation.invoke();
		
		marshaller.applyStoredFlashMessage();
		
		marshaller.moveCurrentRequestFlashMessagesToFlashScope();

		return actionResponse;
	}

	 
}
