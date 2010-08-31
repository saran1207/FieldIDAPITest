package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;

public class CustomerOrgConnectionLoader extends OrgConnectionByLinkedOrgLoader {

	public CustomerOrgConnectionLoader(SecurityFilter filter) {
		super(filter, OrgConnectionType.CUSTOMER);
	}

}
