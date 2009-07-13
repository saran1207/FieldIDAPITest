package com.n4systems.fieldidadmin.actions;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class AbstractAdminAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> session;
	
	@SuppressWarnings("unchecked")
	public void setSession(Map session) {
		this.session = session;
		
	}

	protected Map<String, Object> getSession() {
		return session;
	}

}
