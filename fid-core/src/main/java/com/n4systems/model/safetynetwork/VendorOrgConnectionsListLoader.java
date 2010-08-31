package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;

public class VendorOrgConnectionsListLoader extends OrgConnectionListLoader {

	public VendorOrgConnectionsListLoader(SecurityFilter filter) {
		super(filter, OrgConnectionType.VENDOR);
	}

}
