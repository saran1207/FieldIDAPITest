package com.n4systems.services.safetyNetwork;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.NoAccessToTenantException;
import com.n4systems.model.TenantOrganization;

public class SafetyNetworkAccessService {

	private PersistenceManager persistenceManager;
	private TenantOrganization tenant;
	
	
	public SafetyNetworkAccessService(PersistenceManager persistenceManager, TenantOrganization tenant) {
		super();
		this.persistenceManager = persistenceManager;
		this.tenant = persistenceManager.find(TenantOrganization.class, tenant.getId(), "linkedTenants");
		
	}
	
	
	public CatalogService getCatalogAccess(TenantOrganization linkedTenant) throws NoAccessToTenantException {
		if (hasAccessToLinkedTenant(linkedTenant)) {
			return new CatalogServiceImpl(persistenceManager, linkedTenant);
		} 
		throw new NoAccessToTenantException();
	}


	private boolean hasAccessToLinkedTenant(TenantOrganization linkedTenant) {
		return tenant.getLinkedTenants().contains(linkedTenant);
	}
	
	
	
	
}
