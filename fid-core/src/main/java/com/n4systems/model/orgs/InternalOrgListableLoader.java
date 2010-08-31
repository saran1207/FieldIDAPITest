package com.n4systems.model.orgs;

import com.n4systems.model.api.Listable;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListableLoader;
import com.n4systems.util.persistence.ListableSelect;
import com.n4systems.util.persistence.QueryBuilder;

public class InternalOrgListableLoader extends ListableLoader {

	public InternalOrgListableLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter) {
		QueryBuilder<Listable<Long>> builder = new QueryBuilder<Listable<Long>>(InternalOrg.class, filter);
		builder.setSelectArgument(new ListableSelect());
		builder.addOrder("name");
		
		return builder;
	}

}
