package com.n4systems.model.orgs;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

@Deprecated
public class LegacyFindOrCreateCustomerOrgHandler extends FindOrCreateCustomerOrgHandler {
	private final PersistenceManager persistenceManager;
	
	public LegacyFindOrCreateCustomerOrgHandler(PersistenceManager persistenceManager) {
		super(null, null);
		this.persistenceManager = persistenceManager;
	}

	@Override
	protected List<CustomerOrg> loadOrgList() {
		QueryBuilder<CustomerOrg> allCustomersBuilder = new QueryBuilder<CustomerOrg>(CustomerOrg.class, new TenantOnlySecurityFilter(getParent().getTenant()));
		
		return persistenceManager.findAll(allCustomersBuilder);
	}

	@Override
	protected void saveOrg(CustomerOrg customer) {
		persistenceManager.save(customer);
	}

}
