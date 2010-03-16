package com.n4systems.model.orgs;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.services.TenantCache;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class OrgByNameLoader extends SecurityFilteredLoader<BaseOrg> {
	private final TenantCache tenantCache;
	
	protected String organizationName;
	protected String customerName;
	protected String divisionName;
	
	public OrgByNameLoader(SecurityFilter filter, TenantCache tenantCache) {
		super(filter);
		this.tenantCache = tenantCache;
	}

	public OrgByNameLoader(SecurityFilter filter) {
		this(filter, TenantCache.getInstance());
	}
	
	@Override
	protected BaseOrg load(EntityManager em, SecurityFilter filter) {
		/*
		 * It's going to make this query a lot easier if we know if the 
		 * organization is a primary or a secondary.  We'll start by checking 
		 * the cached primary org.
		 */
		PrimaryOrg primaryOrg = tenantCache.findPrimaryOrg(filter.getTenantId());
		boolean isUnderPrimary = primaryOrg.getName().equals(organizationName);
		
		QueryBuilder<BaseOrg> builder = new QueryBuilder<BaseOrg>(BaseOrg.class, filter);
		
		if (divisionName != null) {
			addExternalOrgSecondaryClause(isUnderPrimary, builder);
			builder.addWhere(WhereClauseFactory.create("customerOrg.name", customerName));
			builder.addWhere(WhereClauseFactory.create("name", divisionName));
		} else if (customerName != null) {
			addExternalOrgSecondaryClause(isUnderPrimary, builder);
			builder.addWhere(WhereClauseFactory.create("name", customerName));
			builder.addWhere(WhereClauseFactory.createIsNull("divisionOrg"));
		} else {
			builder.addWhere(WhereClauseFactory.create("name", organizationName));
			builder.addWhere(WhereClauseFactory.createIsNull("customerOrg"));
			builder.addWhere(WhereClauseFactory.createIsNull("divisionOrg"));
		}
		
		BaseOrg org = builder.getSingleResult(em);
		return org;
	}

	private void addExternalOrgSecondaryClause(boolean isUnderPrimary, QueryBuilder<BaseOrg> builder) {
		if (isUnderPrimary) {
			builder.addWhere(WhereClauseFactory.createIsNull("secondaryOrg"));
		} else {
			builder.addWhere(WhereClauseFactory.create("secondaryOrg.name", organizationName));
		}
	}

	public OrgByNameLoader setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
		return this;
	}
	
	public OrgByNameLoader setCustomerName(String customerName) {
		this.customerName = customerName;
		return this;
	}
	
	public OrgByNameLoader setDivision(String divisionName) {
		this.divisionName = divisionName;
		return this;
	}
}
