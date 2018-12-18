package com.n4systems.model.security;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.SecondaryOrg;

import java.util.TimeZone;

public class SecondaryOrgOnlySecurityFilterOLD extends UserSecurityFilter {

	public SecondaryOrgOnlySecurityFilterOLD(SecurityFilter filter) {
		super(filter.getOwner(), null, TimeZone.getDefault());
	}

}
