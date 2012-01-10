package com.n4systems.model.eventbook;

import com.n4systems.model.EventBook;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class EventBookPaginatedLoader extends PaginatedLoader<EventBook> {
	
	boolean archivedOnly = false;
	

	public EventBookPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<EventBook> createBuilder(SecurityFilter filter) {
		QueryBuilder<EventBook> builder = new QueryBuilder<EventBook>(EventBook.class, filter);
		
		builder.addOrder("name");

		if (archivedOnly) {
			builder.addSimpleWhere("state", EntityState.ARCHIVED);
		} else {
			builder.addSimpleWhere("state", EntityState.ACTIVE);
		}
		
		return builder;
	}
	
	public EventBookPaginatedLoader archivedOnly() {
		archivedOnly = true;
		return this;
	}
}
