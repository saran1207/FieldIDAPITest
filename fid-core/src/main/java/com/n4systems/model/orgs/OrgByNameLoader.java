package com.n4systems.model.orgs;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.services.TenantFinder;
import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class OrgByNameLoader extends SecurityFilteredLoader<BaseOrg> {
	private final TenantFinder tenantFinder;
	
	protected String organizationName;
	protected String customerName;
	protected String divisionName;
	
	private OrgByNameLoader(SecurityFilter filter, TenantFinder tenantFinder) {
		super(filter);
		this.tenantFinder = tenantFinder;
	}

	public OrgByNameLoader(SecurityFilter filter) {
		this(filter, TenantFinder.getInstance());
	}
	
	@Override
	protected BaseOrg load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<BaseOrg> builder = new QueryBuilder<BaseOrg>(BaseOrg.class, filter);
		createQueryForBuilder(builder, filter.getTenantId());		
		BaseOrg org = builder.getSingleResult(em);
		return org;
	}

	public <V> QueryBuilder<V> createQueryForBuilder(QueryBuilder<V> builder, Long tenantId) {
		/*
		 * It's going to make this query a lot easier if we know if the 
		 * organization is a primary or a secondary.  We'll start by checking 
		 * the cached primary org.
		 */
		PrimaryOrg primaryOrg = tenantFinder.findPrimaryOrg(tenantId);
		boolean isUnderPrimary = primaryOrg.getName().equals(organizationName);
				
		if (divisionName != null) {
			addExternalOrgSecondaryClause(isUnderPrimary, builder);
			builder.addWhere(WhereClauseFactory.createNotNull("customerOrg"));			
			builder.addWhere(WhereClauseFactory.createNotNull("divisionOrg"));			
			builder.addWhere(WhereClauseFactory.create("customerOrg.name", customerName));
			builder.addWhere(WhereClauseFactory.create("name", divisionName));
		} else if (customerName != null) {
			addExternalOrgSecondaryClause(isUnderPrimary, builder);
			builder.addWhere(WhereClauseFactory.createNotNull("customerOrg"));			
			builder.addWhere(WhereClauseFactory.create("name", customerName));
			builder.addWhere(WhereClauseFactory.createIsNull("divisionOrg"));
		} else {
			builder.addWhere(WhereClauseFactory.create("name", organizationName));
			builder.addWhere(WhereClauseFactory.createIsNull("customerOrg"));
			builder.addWhere(WhereClauseFactory.createIsNull("divisionOrg"));
		}
		return builder;
	}

	private <V> void addExternalOrgSecondaryClause(boolean isUnderPrimary, QueryBuilder<V> builder) {
		if (isUnderPrimary) {
			builder.addWhere(WhereClauseFactory.createIsNull("secondaryOrg"));
		} else {
			builder.addWhere(WhereClauseFactory.create("secondaryOrg.name", organizationName));
		}
	}

	public OrgByNameLoader setOrganizationName(String organizationName) {		
		this.organizationName = StringUtils.clean(organizationName);
		return this;
	}
	
	public OrgByNameLoader setCustomerName(String customerName) {
		this.customerName = StringUtils.clean(customerName);
		return this;
	}
	
	public OrgByNameLoader setDivision(String divisionName) {
		this.divisionName = StringUtils.clean(divisionName);
		return this;
	}
}
