package com.n4systems.handlers.creator;

import com.n4systems.model.Tenant;

public interface BaseSystemTenantStructureCreateHandler extends CreateHandler {
	
	public BaseSystemTenantStructureCreateHandler forTenant(Tenant tenant);
}
