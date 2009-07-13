package com.n4systems.model.utils;

import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.TenantOrganization;

public class CleanStateSetFactory {

	private StateSet originalStateSet;
	private TenantOrganization targetTenant;
	
	public CleanStateSetFactory(StateSet originalStateSet, TenantOrganization targetTenant) {
		super();
		this.originalStateSet = originalStateSet;
		this.targetTenant = targetTenant;
	}
	
	
	public StateSet clean() {
		originalStateSet.setId(null);
		originalStateSet.setCreated(null);
		originalStateSet.setModified(null);
		originalStateSet.setModifiedBy(null);
		cleanStates();
		
		originalStateSet.setTenant(targetTenant);
		return originalStateSet;
	}
	
	private void cleanStates() {
		originalStateSet.setStates(originalStateSet.getAvailableStates());
		for (State originalState : originalStateSet.getStates()) {
			originalState.setId(null);
			originalState.setCreated(null);
			originalState.setModified(null);
			originalState.setModifiedBy(null);
			originalState.setTenant(targetTenant);
		}
	}
	
}
