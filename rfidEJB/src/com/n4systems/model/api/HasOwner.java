package com.n4systems.model.api;

import com.n4systems.model.orgs.BaseOrg;

public interface HasOwner extends HasTenant {
	public BaseOrg getOwner();
	public void setOwner(BaseOrg owner);
}
