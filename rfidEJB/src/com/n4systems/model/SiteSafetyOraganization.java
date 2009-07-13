package com.n4systems.model;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "siteSafety")
public class SiteSafetyOraganization extends TenantOrganization {

	private static final long serialVersionUID = 1L;

	@Override
	public List<? extends TenantOrganization> getLinkedTenants() {
		return null;
	}

}
