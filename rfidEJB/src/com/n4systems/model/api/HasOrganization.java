package com.n4systems.model.api;

import com.n4systems.model.orgs.BaseOrg;

public interface HasOrganization {
	public BaseOrg getOrganization();
	public void setOrganization(BaseOrg organization);
}
