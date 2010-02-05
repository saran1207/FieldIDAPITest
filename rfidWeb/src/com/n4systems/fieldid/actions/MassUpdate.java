package com.n4systems.fieldid.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.SearchPerformerWithReadOnlyTransactionManagement;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.search.BaseSearchDefiner;
import com.n4systems.util.persistence.search.ImmutableBaseSearchDefiner;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;

public class MassUpdate extends AbstractAction  {
	private static final long serialVersionUID = 1L;
	
	protected String searchId;
	private Long currentPage;
	
	protected MassUpdateManager massUpdateManager;
	
	protected Map<String,Boolean> select = new HashMap<String,Boolean>();
	
	public MassUpdate(MassUpdateManager massUpdateManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		
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

	protected List<Long> getSearchIds(BaseSearchDefiner searchDefiner, SecurityFilter securityFilter) {
		return new SearchPerformerWithReadOnlyTransactionManagement().idSearch(new ImmutableBaseSearchDefiner(searchDefiner), securityFilter);
	}
}
