package com.n4systems.model.orgs;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class OrgWithNameExistsLoader extends SecurityFilteredLoader<Boolean> {
	protected String name;
	
	public OrgWithNameExistsLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Boolean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<BaseOrg> builder = new QueryBuilder<BaseOrg>(BaseOrg.class, filter);
		builder.addWhere(WhereClauseFactory.create("name", name));
		
		Boolean exists = builder.entityExists(em);
		return exists;
	}

	public OrgWithNameExistsLoader setName(String name) {
		this.name = name;
		return this;
	}
	
}
