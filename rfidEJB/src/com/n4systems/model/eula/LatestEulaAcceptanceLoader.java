package com.n4systems.model.eula;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class LatestEulaAcceptanceLoader extends SecurityFilteredLoader<EulaAcceptance> {

	public LatestEulaAcceptanceLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected EulaAcceptance load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<EulaAcceptance> query = new QueryBuilder<EulaAcceptance>(EulaAcceptance.class, filter.prepareFor(EulaAcceptance.class));
		query.addOrder("date", false);
		
		List<EulaAcceptance> lastAcceptances = query.getResultList(em, 0, 1);
		return (lastAcceptances.isEmpty()) ? null : lastAcceptances.get(0); 
	}


}
