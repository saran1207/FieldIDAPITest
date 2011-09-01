package com.n4systems.fieldid.utils;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;


public class ActionInvocationWrapper implements StrutsStatics {
	private final ActionInvocation action;
	
	public ActionInvocationWrapper(ActionInvocation action) {
		this.action = action;
	}
	
	public ActionContext getActionContext() {
		return action.getInvocationContext();
	}
	
	public HttpServletRequest getRequest() {
		return (HttpServletRequest)getActionContext().get(HTTP_REQUEST);
	}
	
	public HttpServletResponse getResponse() {
		return (HttpServletResponse)getActionContext().get(HTTP_RESPONSE);
	}
	
//	public HttpSession getHttpSession() {
//		return getRequest().getSession(true);
//	}
	
	public AbstractAction getAction() {
		return (AbstractAction)action.getAction();
	}
	
	public ActionProxy getProxy() {
		return action.getProxy();
	}
	
	public WebSessionMap getSession() {
		return new WebSessionMap(getRequest().getSession(true));
	}
	
	public SessionUser getSessionUser() {
		return getSession().getSessionUser();
	}
	
	public Method getMethod() throws SecurityException, NoSuchMethodException {
		return getAction().getClass().getMethod(getMethodName());
	}
	
	public String getMethodName() {
		String methodName = getProxy().getMethod();
		if (!methodName.equals("execute")) {
			return convertNameToDoMethod(methodName);
		}
		return methodName;
	}

	private String convertNameToDoMethod(String methodName) {
		return "do" + String.valueOf(methodName.charAt(0)).toUpperCase() + methodName.substring(1);
	}
	
}