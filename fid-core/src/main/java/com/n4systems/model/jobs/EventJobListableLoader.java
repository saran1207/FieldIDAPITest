package com.n4systems.model.jobs;

import com.n4systems.model.Project;
import com.n4systems.model.api.Listable;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListableLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class EventJobListableLoader extends ListableLoader {

	public EventJobListableLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter) {
		QueryBuilder<Listable<Long>> query = new QueryBuilder<Listable<Long>>(Project.class, filter);
		query.addSimpleWhere("eventJob", true);
		query.addSimpleWhere("retired", false);
		return query;
	}

}
