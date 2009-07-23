package com.n4systems.services;

import com.n4systems.services.limiters.TenantLimitService;

public class TenantLimitServiceInitializer implements Initializer {

	public void initialize() {
		TenantLimitService.getInstance().updateAll();
	}

	public void uninitialize() {
	}

}
