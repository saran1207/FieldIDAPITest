package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;

public class CustomerLinkedOrgLoader extends LinkedOrgLoader {

	public CustomerLinkedOrgLoader(SecurityFilter filter) {
		super(filter, OrgConnectionType.CUSTOMER);
	}

	
	
}
