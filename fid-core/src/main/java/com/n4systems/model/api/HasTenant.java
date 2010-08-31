package com.n4systems.model.api;


import com.n4systems.model.Tenant;

public interface HasTenant {
	public Tenant getTenant();
	public void setTenant( Tenant tenant);
}
