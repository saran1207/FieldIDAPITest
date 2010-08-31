package com.n4systems.subscription.netsuite.model;

public class ManageTenantResponse extends BaseResponse {

	private TenantResponse tenant;

	public TenantResponse getTenant() {
		return tenant;
	}

	public void setTenant(TenantResponse tenant) {
		this.tenant = tenant;
	}
	
}
