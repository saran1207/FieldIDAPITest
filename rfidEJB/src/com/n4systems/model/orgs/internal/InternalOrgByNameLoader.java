package com.n4systems.model.orgs.internal;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.services.TenantCache;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class InternalOrgByNameLoader extends SecurityFilteredLoader<InternalOrg> {
	private String name;
	
	public InternalOrgByNameLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected InternalOrg load(EntityManager em, SecurityFilter filter) {
		/*
		 * Hibernate won't let us search for InternalOrg's directly, so we'll check first
		 * to see if the name matches the primary org, if not, then we'll query for our 
		 * secondaries
		 */
		PrimaryOrg primaryOrg = TenantCache.getInstance().findPrimaryOrg(filter.getTenantId());
		
		if (primaryOrg.getName().equals(name)) {
			return primaryOrg;
		}
		
		// it wasn't the primary, search for a secondary
		QueryBuilder<SecondaryOrg> builder = new QueryBuilder<SecondaryOrg>(SecondaryOrg.class, filter);
		builder.addWhere(WhereClauseFactory.create("name", name));
		
		SecondaryOrg org = builder.getSingleResult(em);
		return org;
	}

	public InternalOrgByNameLoader setName(String name) {
		this.name = name;
		return this;
	}
}
