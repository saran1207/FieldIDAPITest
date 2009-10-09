package com.n4systems.services.safetyNetwork;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.NoAccessToTenantException;
import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.Tenant;

public class SafetyNetworkAccessService {

	private PersistenceManager persistenceManager;
	private Tenant tenant;
	
	public SafetyNetworkAccessService(PersistenceManager persistenceManager, Tenant tenant) {
		super();
		this.persistenceManager = persistenceManager;
		this.tenant = tenant;
	}
	
	
	public CatalogService getCatalogAccess(Tenant linkedTenant) throws NoAccessToTenantException {
		if (hasAccessToLinkedTenant(linkedTenant)) {
			return new CatalogServiceImpl(persistenceManager, linkedTenant);
		} 
		throw new NoAccessToTenantException();
	}


	private boolean hasAccessToLinkedTenant(Tenant linkedTenant) {
		throw new NotImplementedException("Disabled for Tenant refactor");
	}
	
	
	
	
}
