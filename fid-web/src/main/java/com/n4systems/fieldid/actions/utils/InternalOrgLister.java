package com.n4systems.fieldid.actions.utils;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.loaders.FilteredListableLoader;

public class InternalOrgLister {

	private OrgType orgType;
	private final FilteredListableLoader secondaryOrgLoader;
	private final PrimaryOrg primaryOrg;
	
	public InternalOrgLister(OrgType orgType, FilteredListableLoader secondaryOrgLoader, PrimaryOrg primaryOrg) {
		this.orgType = orgType;
		this.secondaryOrgLoader = secondaryOrgLoader;
		this.primaryOrg = primaryOrg;
	}

	public List<Listable<Long>> getInternalOrgs() {
		List<Listable<Long>> internalOrgs = new ArrayList<Listable<Long>>();
		
		if (includePrimaryOrgInList()) {
			internalOrgs.add(primaryOrg);
		}
		
		if (includeSecondaryOrgsInList()) {
			internalOrgs.addAll(secondaryOrgLoader.load());
		}
		
		return internalOrgs;
	}

	private boolean includeSecondaryOrgsInList() {
		return orgType != OrgType.PRIMARY;
	}

	private boolean includePrimaryOrgInList() {
		return orgType != OrgType.SECONDARY;
	}

	
}
