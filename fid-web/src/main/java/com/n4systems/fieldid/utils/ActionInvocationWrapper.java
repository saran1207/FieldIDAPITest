package com.n4systems.fieldid.utils;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.struts2.StrutsStatics;
import rfid.web.helper.SessionUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


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
		return getProxy().getMethod();
	}

}