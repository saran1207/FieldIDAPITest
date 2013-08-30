package com.n4systems.fieldidadmin.actions;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class AbstractAdminAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	protected  PersistenceManager persistenceEJBContainer;

	private WebSessionMap session;
    private String pageName;

	public WebSessionMap getSession() {
		if( session == null ) {
			session = new WebSessionMap(ServletActionContext.getRequest().getSession(false));
		}
		return session;
	}

	public void setPersistenceEJBContainer(PersistenceManager persistenceManager) {
		this.persistenceEJBContainer = persistenceManager;
	}

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public boolean isPageName(String pageName) {
        return pageName.equals(this.pageName);
    }
}
