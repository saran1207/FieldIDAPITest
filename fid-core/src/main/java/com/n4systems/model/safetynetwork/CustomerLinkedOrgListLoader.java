package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;

public class CustomerLinkedOrgListLoader extends LinkedOrgListLoader {

	public CustomerLinkedOrgListLoader(SecurityFilter filter) {
		super(filter, OrgConnectionType.CUSTOMER);
	}

}
