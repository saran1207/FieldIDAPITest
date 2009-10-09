package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Inspection;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class InspectionsByNetworkIdLoader extends ListLoader<Inspection> {

	private Long networkId;
	
	public InspectionsByNetworkIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Inspection> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Inspection> builder = new QueryBuilder<Inspection>(Inspection.class, new OpenSecurityFilter());
		builder.addWhere(WhereClauseFactory.create("product.networkId", networkId));
		
		List<Inspection> unsecuredInspections = builder.getResultList(em);
		
		PersistenceManager.setSessionReadOnly(em);
		
		List<Inspection> enhancedInspections = EntitySecurityEnhancer.enhanceList(unsecuredInspections, filter);
		
		return enhancedInspections;
	}

	public InspectionsByNetworkIdLoader setNetworkId(Long networkId) {
		this.networkId = networkId;
		return this;
	}
}
