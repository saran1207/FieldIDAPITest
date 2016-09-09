package com.n4systems.model.orgs.internal;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.services.TenantFinder;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

import javax.persistence.EntityManager;

public class InternalOrgWithNameExistsLoader extends SecurityFilteredLoader<Boolean> {
	protected String name;
	
	public InternalOrgWithNameExistsLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Boolean load(EntityManager em, SecurityFilter filter) {
		// first check to see if the name matches the primary
		PrimaryOrg primaryOrg = TenantFinder.getInstance().findPrimaryOrg(filter.getTenantId());
		
		if (primaryOrg.getName().equals(name)) {
			return true;
		}
		
		// it wasn't the primary, search for a secondary
		QueryBuilder<SecondaryOrg> builder = new QueryBuilder<SecondaryOrg>(SecondaryOrg.class, filter);
		builder.addWhere(WhereClauseFactory.create("name", name));
		
		boolean exists = builder.entityExists(em);
		return exists;
	}

	public InternalOrgWithNameExistsLoader setName(String name) {
		this.name = name;
		return this;
	}
}
