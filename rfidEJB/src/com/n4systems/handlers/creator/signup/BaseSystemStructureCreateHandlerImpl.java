package com.n4systems.handlers.creator.signup;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Tenant;
import com.n4systems.persistence.Transaction;

public class BaseSystemStructureCreateHandlerImpl implements BaseSystemStructureCreateHandler {

	
	private final BaseSystemTenantStructureCreateHandler tenantStructureCreateHandler;
	private final BaseSystemSetupDataCreateHandler setupDataCreateHandler;
	
	private Tenant tenant;
	
		
	public BaseSystemStructureCreateHandlerImpl(BaseSystemTenantStructureCreateHandler tenantStructureCreateHandler, BaseSystemSetupDataCreateHandler setupDataCreateHandler) {
		this.tenantStructureCreateHandler = tenantStructureCreateHandler;
		this.setupDataCreateHandler = setupDataCreateHandler;
		
	}

	public void create(Transaction transaction) {
		if (invalidTenant()) {
			throw new InvalidArgumentException("you must set a saved tenant.");
		}
		
		tenantStructureCreateHandler.forTenant(tenant).create(transaction);
		setupDataCreateHandler.forTenant(tenant).create(transaction);
		
		
	}

	private boolean invalidTenant() {
		return tenant == null || tenant.isNew();
	}

	
	
	
	
	
	
	public BaseSystemStructureCreateHandler forTenant(Tenant tenant) {
		this.tenant = tenant;
		return this;
	}

}
