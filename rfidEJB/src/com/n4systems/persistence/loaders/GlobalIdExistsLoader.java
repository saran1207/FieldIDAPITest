package com.n4systems.persistence.loaders;

import javax.persistence.EntityManager;

import com.n4systems.model.api.Exportable;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class GlobalIdExistsLoader extends SecurityFilteredLoader<Boolean> {
	private Class<? extends Exportable> entityClass;
	private String globalId;
	
	public GlobalIdExistsLoader(SecurityFilter filter, Class<? extends Exportable> entityClass) {
		super(filter);
		this.entityClass = entityClass;
	}

	@Override
	protected Boolean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<InternalOrg> builder = new QueryBuilder<InternalOrg>(entityClass, filter);
		builder.addWhere(WhereClauseFactory.create("globalId", globalId));
		
		boolean exists = builder.entityExists(em);
		return exists;
	}
	
	public GlobalIdExistsLoader setGlobalId(String globalId) {
		this.globalId = globalId;
		return this;
	}



	
}
