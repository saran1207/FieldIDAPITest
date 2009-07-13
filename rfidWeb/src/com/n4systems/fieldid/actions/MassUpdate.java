package com.n4systems.fieldid.actions;

import java.util.HashMap;
import java.util.Map;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;

public class MassUpdate extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	protected String searchId;
	private Long currentPage;
	
	protected CustomerManager customerManager;
	protected MassUpdateManager massUpdateManager;
	
	protected Map<String,Boolean> select = new HashMap<String,Boolean>();
	
	public MassUpdate(CustomerManager customerManager, MassUpdateManager massUpdateManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		
		this.customerManager = customerManager;
		this.massUpdateManager = massUpdateManager;
	}
	
	public String getSearchId() {
		return searchId;
	}
	
	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}
	
	public Map<String,Boolean> getSelect() {
		return select;
	}

	@CustomValidator( type="selectCheckListValidator", message="", key="error.onefieldmustbeselected")
	public void setSelect( Map<String,Boolean> select) {
		this.select = select;
	}

	public Long getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Long pageNumber) {
		this.currentPage = pageNumber;
	}
}
