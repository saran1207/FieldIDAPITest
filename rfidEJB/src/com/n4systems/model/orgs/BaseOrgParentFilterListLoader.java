package com.n4systems.model.orgs;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.api.Listable;
import com.n4systems.model.security.OwnerFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListableLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class BaseOrgParentFilterListLoader extends ListableLoader {

	private BaseOrg parent;
	private Class<?> clazz;
	
	
	public BaseOrgParentFilterListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter) {
		gaurds();
		
		QueryBuilder<Listable<Long>> builder = new QueryBuilder<Listable<Long>>(clazz, filter);
		new OwnerFilter(parent).applyFilter(builder);
		builder.setOrder("name");
		
		return builder;
	}
	
	private void gaurds() {
		if (clazz == null)
			throw new InvalidArgumentException("you must give an org type class");
		
		if (parent == null) 
			throw new InvalidArgumentException("you must give a parent org");
	}

	public BaseOrgParentFilterListLoader setParent(BaseOrg parent) {
		this.parent = parent;
		return this;
	}

	public BaseOrgParentFilterListLoader setClazz(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}

	

}
