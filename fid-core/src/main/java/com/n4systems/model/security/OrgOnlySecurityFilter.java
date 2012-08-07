package com.n4systems.model.security;

import com.n4systems.model.orgs.BaseOrg;

import java.util.TimeZone;

public class OrgOnlySecurityFilter extends UserSecurityFilter {

	public OrgOnlySecurityFilter(BaseOrg filterOrg) {
		super(filterOrg, null, TimeZone.getDefault());
	}

}
