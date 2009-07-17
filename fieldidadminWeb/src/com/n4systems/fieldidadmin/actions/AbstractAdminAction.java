package com.n4systems.fieldidadmin.actions;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.n4systems.ejb.PersistenceManager;
import com.opensymphony.xwork2.ActionSupport;

public class AbstractAdminAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	
	protected  PersistenceManager persistenceManager;
	
	private Map<String, Object> session;
	
	@SuppressWarnings("unchecked")
	public void setSession(Map session) {
		this.session = session;
		
	}

	protected Map<String, Object> getSession() {
		return session;
	}

	public void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

}
