package com.n4systems.handlers.creator;

import com.n4systems.model.Tenant;

public interface BaseSystemStructureCreateHandler extends CreateHandler {

	public abstract BaseSystemStructureCreateHandler forTenant(Tenant tenant);

}