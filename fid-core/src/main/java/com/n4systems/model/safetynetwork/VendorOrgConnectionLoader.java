package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;

public class VendorOrgConnectionLoader extends OrgConnectionByLinkedOrgLoader {

	public VendorOrgConnectionLoader(SecurityFilter filter) {
		super(filter, OrgConnectionType.VENDOR);
	}

}
