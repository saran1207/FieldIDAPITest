package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;

public class VendorLinkedOrgLoader extends LinkedOrgLoader {

	public VendorLinkedOrgLoader(SecurityFilter filter) {
		super(filter, OrgConnectionType.VENDOR);
	}

	

}
