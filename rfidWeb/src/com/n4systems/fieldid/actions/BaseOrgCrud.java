package com.n4systems.fieldid.actions;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.BaseOrgListLoader;

public class BaseOrgCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	
	private List<BaseOrg> orgs;
	private String searchName;
	
	private String orgTypeFilter = "all";
	
	
	public BaseOrgCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
	}
	
	public String doList() {
		orgs = new BaseOrgListLoader(getSecurityFilter()).setSearchName(searchName).setOrgType(orgTypeFilter).load();
		return SUCCESS;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public List<BaseOrg> getOrgs() {
		return orgs;
	}

	public String getOrgTypeFilter() {
		return orgTypeFilter;
	}

	public void setOrgTypeFilter(String orgTypeFilter) {
		this.orgTypeFilter = orgTypeFilter;
	}

}
