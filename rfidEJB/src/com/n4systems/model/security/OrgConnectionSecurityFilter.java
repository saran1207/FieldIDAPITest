package com.n4systems.model.security;

import com.n4systems.model.safetynetwork.OrgConnectionType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.persistence.WhereClause.ChainOp;

public class OrgConnectionSecurityFilter implements QueryFilter {
	private final OrgConnectionType filterTarget;
	private final SecurityFilter filter;
	
	public OrgConnectionSecurityFilter(OrgConnectionType applyFilteringTo, SecurityFilter filter) {
		this.filterTarget = applyFilteringTo;
		this.filter = filter;
	}
	
	public void applyFilter(QueryBuilder<?> builder) {
		WhereParameterGroup whereGroup = new WhereParameterGroup("org_security");
		
		String orgTarget  = (filterTarget.isCustomer()) ? "customer" : "vendor";
		
		if (filter.getOwner().isPrimary()) {
			// primary can see everything
			whereGroup.addClause(WhereClauseFactory.create(orgTarget + ".tenant.id", filter.getTenantId()));
		} else if (filter.getOwner().isSecondary()) {
			// secondaries can see themselves + primary
			whereGroup.addClause(WhereClauseFactory.create(orgTarget + ".id", filter.getOwner().getId()));
			whereGroup.addClause(WhereClauseFactory.create(orgTarget + ".id", filter.getOwner().getPrimaryOrg().getId(), ChainOp.OR));		
		} else {
			// customers/divisions are locked to their parent internal org
			whereGroup.addClause(WhereClauseFactory.create(orgTarget + ".id", filter.getOwner().getInternalOrg().getId()));
		}
		
		builder.addWhere(whereGroup);
	}
}
