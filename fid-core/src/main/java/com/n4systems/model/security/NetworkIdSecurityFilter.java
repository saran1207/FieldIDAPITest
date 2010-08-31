package com.n4systems.model.security;

import javax.persistence.Query;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.Product;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SubSelectInClause;

public class NetworkIdSecurityFilter extends AbstractSecurityFilter {
	private final SecurityFilter standardFilter;
	private final String networkIdPath;
	
	public NetworkIdSecurityFilter(SecurityFilter standardFilter, String networkIdPath) {
		this.standardFilter = standardFilter;
		this.networkIdPath = networkIdPath;
	}
	
	@Override
	protected void applyFilter(QueryBuilder<?> builder, SecurityDefiner definer) throws SecurityException {
		if (definer.isStateFiltered()) {
			addFilterParameter(builder, definer.getStatePath(), EntityState.ACTIVE);
		}
		
		QueryBuilder<Long> networkIdQuery = new QueryBuilder<Long>(Product.class, standardFilter);
		networkIdQuery.setSimpleSelect("networkId", true);
		
		SubSelectInClause subCaluse = new SubSelectInClause(networkIdPath, networkIdQuery);
		builder.addWhere(subCaluse);
	}

	@Override
	protected void applyParameters(Query query, SecurityDefiner definer) throws SecurityException {
		throw new NotImplementedException();
	}

	@Override
	protected String produceWhereClause(String tableAlias, SecurityDefiner definer) throws SecurityException {
		throw new NotImplementedException();
	}

	public BaseOrg getOwner() {
		return standardFilter.getOwner();
	}

	public Long getTenantId() {
		return standardFilter.getTenantId();
	}

	public Long getUserId() {
		return standardFilter.getUserId();
	}

	public boolean hasOwner() {
		return standardFilter.hasOwner();
	}
}
