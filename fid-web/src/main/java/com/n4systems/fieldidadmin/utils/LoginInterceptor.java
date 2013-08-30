package com.n4systems.fieldidadmin.utils;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletRequest;

public class LoginInterceptor extends AbstractInterceptor implements StrutsStatics {


	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		// Get the action context from the invocation so we can access the
		// HttpServletRequest and HttpSession objects.
		HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get(HTTP_REQUEST);
		boolean authenticated = new WebSessionMap(request.getSession(true)).isAdminAuthenticated();
		return authenticated ? invocation.invoke() : "signIn";
	}
	
}
