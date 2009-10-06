package com.n4systems.model.orgs;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class FindOwnerByLegacyIds {

	private final PersistenceManager persistenceManager;
	private final Long tenantId;
	private Long legacyCustomerId;
	private Long legacyDivisionId;
	private Long legacyJobSiteId;
	
	public FindOwnerByLegacyIds(PersistenceManager persistenceManager, Long tenantId) {
		this.persistenceManager = persistenceManager;
		this.tenantId = tenantId;
	}

	public BaseOrg retrieveOwner() {
		BaseOrg owner = null;
		
		if (legacyJobSiteId != null) {
			QueryBuilder<CustomerOrg> customerQuery = new QueryBuilder<CustomerOrg>(CustomerOrg.class, new TenantOnlySecurityFilter(tenantId));
			customerQuery.addSimpleWhere("legacyId", legacyJobSiteId);
			
			owner = persistenceManager.find(customerQuery);			
		} else if (legacyDivisionId != null) {
			QueryBuilder<DivisionOrg> divisionQuery = new QueryBuilder<DivisionOrg>(DivisionOrg.class, new TenantOnlySecurityFilter(tenantId));
			divisionQuery.addSimpleWhere("legacyId", legacyDivisionId);
			
			owner = persistenceManager.find(divisionQuery);
		} else if (legacyCustomerId != null) {
			QueryBuilder<CustomerOrg> customerQuery = new QueryBuilder<CustomerOrg>(CustomerOrg.class, new TenantOnlySecurityFilter(tenantId));
			customerQuery.addSimpleWhere("legacyId", legacyCustomerId);
			
			owner = persistenceManager.find(customerQuery);
		} else {
			QueryBuilder<PrimaryOrg> primaryQuery = new QueryBuilder<PrimaryOrg>(PrimaryOrg.class, new OpenSecurityFilter());
			primaryQuery.addSimpleWhere("tenant.id", tenantId);

			owner = persistenceManager.find(primaryQuery);
		}
		
		return owner;
	}
	
	public void setLegacyCustomerId(Long legacyCustomerId) {
		this.legacyCustomerId = legacyCustomerId;
	}

	public void setLegacyDivisionId(Long legacyDivisionId) {
		this.legacyDivisionId = legacyDivisionId;
	}

	public void setLegacyJobSiteId(Long legacyJobSiteId) {
		this.legacyJobSiteId = legacyJobSiteId;
	}
}
