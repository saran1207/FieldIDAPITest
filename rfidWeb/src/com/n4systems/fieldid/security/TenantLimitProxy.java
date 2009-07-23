package com.n4systems.fieldid.security;

import com.n4systems.services.limiters.TenantLimitService;

public class TenantLimitProxy {
	private final TenantLimitService limitService;
	private final Long tenantId;
	
	public TenantLimitProxy(Long tenantId) {
		this.limitService = TenantLimitService.getInstance();
		this.tenantId = tenantId;
	}
	
	public boolean isDiskSpaceMaxed() {
		return limitService.getDiskSpace(tenantId).isMaxed();
	}
	
	public boolean isEmployeeUsersMaxed() {
		return limitService.getEmployeeUsers(tenantId).isMaxed();
	}
}
