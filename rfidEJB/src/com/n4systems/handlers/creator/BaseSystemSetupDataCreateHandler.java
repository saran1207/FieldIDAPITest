package com.n4systems.handlers.creator;

import com.n4systems.model.Tenant;

public interface BaseSystemSetupDataCreateHandler extends CreateHandler {
	public BaseSystemSetupDataCreateHandler forTenant(Tenant tenant);
}