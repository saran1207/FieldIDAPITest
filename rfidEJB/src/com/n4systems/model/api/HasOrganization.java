package com.n4systems.model.api;

import com.n4systems.model.Organization;

public interface HasOrganization {
	public Organization getOrganization();
	public void setOrganization(Organization organization);
}
