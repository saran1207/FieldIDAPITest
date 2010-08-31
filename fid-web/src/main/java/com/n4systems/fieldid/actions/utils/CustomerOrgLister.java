package com.n4systems.fieldid.actions.utils;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.BaseOrgParentFilterListLoader;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.util.persistence.SimpleListable;

public class CustomerOrgLister {
	private static OrgType[] typesThatIncludeCustomerLists = {OrgType.ALL, OrgType.EXTERNAL, OrgType.CUSTOMER, OrgType.NON_PRIMARY}; 
	
	private final OrgType orgType;
	private final BaseOrgParentFilterListLoader baseOrgParentFilterListLoader;
	
	public CustomerOrgLister(OrgType orgType, BaseOrgParentFilterListLoader baseOrgParentFilterListLoader) {
		this.orgType = orgType;
		this.baseOrgParentFilterListLoader = baseOrgParentFilterListLoader;
	}

	public List<Listable<Long>> getCustomerList(BaseOrg currentlySelectedOrg) {
		
		ArrayList<Listable<Long>> customerList = new ArrayList<Listable<Long>>();
		
		if (includeBlankInList(currentlySelectedOrg)) {
			customerList.add(createBlankValue());
		}
		
		if (includeCustomerList()) {
			customerList.addAll(createCustomerListFor(currentlySelectedOrg));
		}
		
		return customerList;
	}

	private SimpleListable<Long> createBlankValue() {
		return new SimpleListable<Long>(-1L, "");
	}
	
	protected boolean includeCustomerList() {
		for (OrgType includingType : typesThatIncludeCustomerLists) {
			if (orgType == includingType) {
				return true;
			}
		}
		return false;
	}

	protected boolean includeBlankInList(BaseOrg currentlySelectedOrg) {
		if (orgType == OrgType.CUSTOMER || orgType == OrgType.EXTERNAL) 
			return false;
		if (orgType == OrgType.NON_PRIMARY && currentlySelectedOrg.getInternalOrg().isPrimary())
			return false;
		
			
		return includeCustomerList();
	}

	

	private List<Listable<Long>> createCustomerListFor(BaseOrg currentlySelectedOrg) {
		return baseOrgParentFilterListLoader.setClazz(CustomerOrg.class).setParent(currentlySelectedOrg.getInternalOrg()).load();
	}
	

}
