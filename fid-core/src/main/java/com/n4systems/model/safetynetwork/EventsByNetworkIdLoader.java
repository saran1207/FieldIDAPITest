package com.n4systems.model.safetynetwork;

import com.n4systems.model.Event;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SubSelectInClause;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;

import java.util.List;

import javax.persistence.EntityManager;

public class EventsByNetworkIdLoader extends ListLoader<Event> {

	private Long networkId;
	
	private String order;
	
	private boolean ascending;
	
	public EventsByNetworkIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Event> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<Tenant> connectedTenantsQuery = new QueryBuilder<Tenant>(TypedOrgConnection.class, filter);
		connectedTenantsQuery.setSimpleSelect("connectedOrg.tenant", true);

        SubSelectInClause insideSafetyNetworkSubClause = new SubSelectInClause("asset.owner.tenant", connectedTenantsQuery);

        WhereParameterGroup wpg = new WhereParameterGroup();
        wpg.addClause(insideSafetyNetworkSubClause);
        wpg.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "asset.owner.tenant.id", filter.getTenantId(), WhereClause.ChainOp.OR));
        
		QueryBuilder<Event> builder = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter());
		builder.addWhere(WhereClauseFactory.create("asset.networkId", networkId));
        builder.addWhere(wpg);
        
        if (order != null) {
        	if(order.equalsIgnoreCase("performedByFullName")) {
        		builder.addOrder("performedBy.firstName", ascending);
        		builder.addOrder("performedBy.lastName", ascending);
        	}else {
        		builder.addOrder(order, ascending);
        	}
        }

		List<Event> unsecuredEvents = builder.getResultList(em);
		
		PersistenceManager.setSessionReadOnly(em);
		
		List<Event> enhancedEvents = EntitySecurityEnhancer.enhanceList(unsecuredEvents, filter);
		
		return enhancedEvents;
	}

	public EventsByNetworkIdLoader setNetworkId(Long networkId) {
		this.networkId = networkId;
		return this;
	}
	
	public EventsByNetworkIdLoader setOrder(String order, boolean ascending) {
		this.order = order;
		this.ascending = ascending;
		return this;
	}
}
