package com.n4systems.fieldid.utils;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("serial")
public class FlashScopeInterceptor extends AbstractInterceptor implements StrutsStatics {
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();

        // for GET requests, we don't get two separate action invocations. This means that the flash scope messages
        // never get cleared up and we were actually displaying them on the next 2 pages. So we verify that this is
        // a post request. If not, we don't throw the flash scope messages into the session, because they will be available already for display
        boolean storeSessionFlashMessages = "POST".equals(request.getMethod());

        ActionInvocationWrapper action = new ActionInvocationWrapper(invocation);
        FlashScopeMarshaller marshaller = new FlashScopeMarshaller(action.getAction(), action.getSession().getHttpSession());

        marshaller.storeAndRemovePreviousFlashMessages();

        String actionResponse = invocation.invoke();

        marshaller.applyStoredFlashMessage();

        if (storeSessionFlashMessages)
            marshaller.moveCurrentRequestFlashMessagesToFlashScope();

        return actionResponse;
	}
	 
}
