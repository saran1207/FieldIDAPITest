package com.n4systems.model.api;


import com.n4systems.model.TenantOrganization;

public interface HasTenant {
	public TenantOrganization getTenant();
	public void setTenant( TenantOrganization tenant);
}
