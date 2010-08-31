package com.n4systems.services.safetyNetwork;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.NoAccessToTenantException;
import com.n4systems.model.Tenant;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class SafetyNetworkAccessService {

	private PersistenceManager persistenceManager;
	private QueryFilter filter;
	
	public SafetyNetworkAccessService(PersistenceManager persistenceManager, QueryFilter filter) {
		super();
		this.persistenceManager = persistenceManager;
		this.filter = filter;
	}
	
	
	public CatalogService getCatalogAccess(Tenant linkedTenant) throws NoAccessToTenantException {
		if (hasAccessToLinkedTenant(linkedTenant)) {
			return new CatalogServiceImpl(persistenceManager, linkedTenant);
		} 
		throw new NoAccessToTenantException();
	}


	private boolean hasAccessToLinkedTenant(Tenant linkedTenant) {
		QueryBuilder<TypedOrgConnection> query = new QueryBuilder<TypedOrgConnection>(TypedOrgConnection.class, filter);
		query.addWhere(Comparator.EQ, "otherTenant", "connectedOrg.tenant", linkedTenant);
		return (persistenceManager.findCount(query) > 0);
	}
	
	
}
