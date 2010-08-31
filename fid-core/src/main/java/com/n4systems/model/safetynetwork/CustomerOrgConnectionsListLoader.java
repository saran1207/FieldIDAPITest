package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;

public class CustomerOrgConnectionsListLoader extends OrgConnectionListLoader {

	public CustomerOrgConnectionsListLoader(SecurityFilter filter) {
		super(filter, OrgConnectionType.CUSTOMER);
	}

}
