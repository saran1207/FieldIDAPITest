package com.n4systems.model.security;

import javax.persistence.Query;

import com.n4systems.model.safetynetwork.OrgConnectionType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereClause;
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
		builder.addWhere(createWhereClause());
	}

	public WhereClause<?> createWhereClause() {
		return createWhereClause(ChainOp.AND); 
	}
	
	public WhereClause<?> createWhereClause(ChainOp op) {
		WhereParameterGroup whereGroup = new WhereParameterGroup("org_security");
		whereGroup.setChainOperator(op);
		
		String orgTarget = orgTarget();
		
		if (filter.getOwner().isPrimary()) {
			// primary can see everything
			whereGroup.addClause(WhereClauseFactory.create(orgTarget + ".tenant.id", filter.getTenantId()));
		} else if (filter.getOwner().isSecondary()) {
			// secondaries can see themselves + primary
			whereGroup.addClause(WhereClauseFactory.create("connection_filter_" + orgTarget + "_secondary", orgTarget + ".id", filter.getOwner().getId()));
			whereGroup.addClause(WhereClauseFactory.create(orgTarget + ".id", filter.getOwner().getPrimaryOrg().getId(), ChainOp.OR, "connection_filter_" + orgTarget + "_primary"));
		} else {
			// customers/divisions are locked to their parent internal org
			whereGroup.addClause(WhereClauseFactory.create(orgTarget + ".id", filter.getOwner().getInternalOrg().getId()));
		}
		return whereGroup;
	}

	public void applyParameters(Query query) {
		String orgTarget = orgTarget();
		
		if (filter.getOwner().isPrimary()) {
			// primary can see everything
			query.setParameter("connection_filter_" + orgTarget + "_tenant", filter.getTenantId()); 
		} else if (filter.getOwner().isSecondary()) {
			query.setParameter("connection_filter_" + orgTarget + "_owner", filter.getOwner().getId()); 
			query.setParameter("connection_filter_" + orgTarget + "_secondary", filter.getOwner().getPrimaryOrg().getId()); 		
		} else {
			// customers/divisions are locked to their parent internal org
			query.setParameter("connection_filter_" + orgTarget + "_owner", filter.getOwner().getInternalOrg().getId());
		}
		
	}

	private String orgTarget() {
		return (filterTarget.isCustomer()) ? "customer" : "vendor";
	}


	public String produceWhereClause(Class<?> queryClass, String tableAlias) {
		String orgTarget = orgTarget();
		String clause = "";
		if (filter.getOwner().isPrimary()) {
			// primary can see everything
			clause += tableAlias + "." + orgTarget + ".tenant.id = :connection_filter_" + orgTarget + "_tenant";
		} else if (filter.getOwner().isSecondary()) {
			// secondaries can see themselves + primary
			clause += "( " + tableAlias + "." + orgTarget + ".id = :connection_filter_" + orgTarget + "_owner"; 
			clause += " OR " + tableAlias + "." + orgTarget + ".id = :connection_filter_" + orgTarget + "_secondary )"; 		
		} else {
			// customers/divisions are locked to their parent internal org
			clause += tableAlias + "." + orgTarget + ".id = :connection_filter_" + orgTarget + "_owner";
		}
		return clause;
	}
}
