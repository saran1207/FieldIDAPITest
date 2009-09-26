package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;

public class VendorOrgConnectionPaginatedLoader extends OrgConnectionPaginatedLoader {

	public VendorOrgConnectionPaginatedLoader(SecurityFilter filter) {
		super(filter, OrgConnectionType.VENDOR);
	}
	
}
