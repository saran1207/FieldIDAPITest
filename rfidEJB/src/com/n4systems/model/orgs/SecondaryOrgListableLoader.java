package com.n4systems.model.orgs;

import com.n4systems.model.api.Listable;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListableLoader;
import com.n4systems.util.persistence.ListableSelect;
import com.n4systems.util.persistence.QueryBuilder;

public class SecondaryOrgListableLoader extends ListableLoader {

	public SecondaryOrgListableLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter) {
		QueryBuilder<Listable<Long>> builder = new QueryBuilder<Listable<Long>>(SecondaryOrg.class, filter);
		builder.setSelectArgument(new ListableSelect());
		
		return builder;
	}

}
