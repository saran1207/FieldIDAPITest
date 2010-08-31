package com.n4systems.handlers.creator.signup;

import com.n4systems.handlers.creator.CreateHandler;
import com.n4systems.model.Tenant;

public interface BaseSystemTenantStructureCreateHandler extends CreateHandler {
	
	public BaseSystemTenantStructureCreateHandler forTenant(Tenant tenant);
}
