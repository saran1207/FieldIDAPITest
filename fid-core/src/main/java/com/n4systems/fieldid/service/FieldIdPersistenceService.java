package com.n4systems.fieldid.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.model.Tenant;
import com.n4systems.model.user.User;

public class FieldIdPersistenceService extends FieldIdService {

	@Autowired
    protected PersistenceService persistenceService;

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	protected Tenant getCurrentTenant() {
		return persistenceService.findNonSecure(Tenant.class, securityContext.getTenantSecurityFilter().getTenantId());
	}
	
	protected User getCurrentUser() {
		return persistenceService.find(User.class, securityContext.getUserSecurityFilter().getUserId());
	}
	
}
