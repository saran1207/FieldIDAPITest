package com.n4systems.fieldid.security;

import java.lang.reflect.Method;

import org.apache.struts2.StrutsStatics;

import com.n4systems.fieldid.utils.ActionInvocationWrapper;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SafetyNetworkAwareInterceptor extends AbstractInterceptor implements StrutsStatics {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		ActionInvocationWrapper invocationWrapper = new ActionInvocationWrapper(actionInvocation);

		if (invocationWrapper.getAction() instanceof SafetyNetworkAware) {
			SafetyNetworkAware safetyNetworkAction = (SafetyNetworkAware)invocationWrapper.getAction();
			
			boolean allowNetworkResults;
			try {
				Method method = invocationWrapper.getMethod();

				allowNetworkResults = method.isAnnotationPresent(NetworkAwareAction.class);
			} catch(Exception e) {
				allowNetworkResults = false;
			}
			
			safetyNetworkAction.setAllowNetworkResults(allowNetworkResults);
		}
		
		return actionInvocation.invoke();
	}
	
}
