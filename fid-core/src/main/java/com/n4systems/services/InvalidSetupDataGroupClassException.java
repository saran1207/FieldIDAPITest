package com.n4systems.services;

import com.n4systems.model.api.CrossTenantEntity;
import com.n4systems.model.api.HasTenant;

public class InvalidSetupDataGroupClassException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidSetupDataGroupClassException(Class<?> clazz) {
		super("Setup data group class [" + clazz.getName() + "] was not an insance of [" + HasTenant.class.getName() + "] or [" + CrossTenantEntity.class.getName() + "]");
	}

}
