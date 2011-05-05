package com.n4systems.fieldidadmin.actions;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.handlers.creator.CreateHandlerFactory;
import com.opensymphony.xwork2.ActionSupport;

public class AbstractAdminAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	
	protected  PersistenceManager persistenceEJBContainer;
	
	private Map<String, Object> session;
	private CreateHandlerFactory createHandlerFactory;
    private String pageName;

	@SuppressWarnings("unchecked")
	public void setSession(Map session) {
		this.session = session;
	}

	protected Map<String, Object> getSession() {
		return session;
	}

	public void setPersistenceEJBContainer(PersistenceManager persistenceManager) {
		this.persistenceEJBContainer = persistenceManager;
	}

	protected CreateHandlerFactory getCreateHandlerFactory() {
		if (createHandlerFactory == null) {
			createHandlerFactory = new CreateHandlerFactory();
		}
		
		return createHandlerFactory;
	}

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public boolean isPageName(String pageName) {
        return pageName.equals(this.pageName);
    }
}
