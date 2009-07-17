package com.n4systems.model.eula;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.TenantOrganization;
import com.n4systems.persistence.loaders.legacy.EntityLoader;
import com.n4systems.persistence.loaders.legacy.SecuredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;

public class LatestEulaAcceptanceLoader extends SecuredLoader<EulaAcceptance> {

	
	public LatestEulaAcceptanceLoader(SecurityFilter filter) {
		super(filter);
	}

	public LatestEulaAcceptanceLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}
	
	
	@Override
	protected EulaAcceptance load(PersistenceManager pm, SecurityFilter filter) {
		
		QueryBuilder<EulaAcceptance> query = new QueryBuilder<EulaAcceptance>(EulaAcceptance.class, filter.prepareFor(EulaAcceptance.class));
		query.addOrder("date", false);
		
		List<EulaAcceptance> lastAcceptances = pm.findAll(query, 0, 1);
		return (lastAcceptances.isEmpty()) ? null : lastAcceptances.get(0); 
	}


}
