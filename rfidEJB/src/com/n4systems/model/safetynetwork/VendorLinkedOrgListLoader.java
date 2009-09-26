package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;

public class VendorLinkedOrgListLoader extends LinkedOrgListLoader {

	public VendorLinkedOrgListLoader(SecurityFilter filter) {
		super(filter, OrgConnectionType.VENDOR);
	}

}
