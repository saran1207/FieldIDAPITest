package com.n4systems.model.orgs;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class OrgByNameLoader extends SecurityFilteredLoader<BaseOrg> {
	protected String name;
	
	public OrgByNameLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected BaseOrg load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<BaseOrg> builder = new QueryBuilder<BaseOrg>(BaseOrg.class, filter);
		builder.addWhere(WhereClauseFactory.create("name", name));
		
		BaseOrg org = builder.getSingleResult(em);
		return org;
	}

	public OrgByNameLoader setName(String name) {
		this.name = name;
		return this;
	}
	
}
