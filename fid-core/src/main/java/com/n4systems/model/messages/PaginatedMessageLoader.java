package com.n4systems.model.messages;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class PaginatedMessageLoader extends PaginatedLoader<Message> {

	public PaginatedMessageLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<Message> createBuilder(SecurityFilter filter) {
		return createQueryBuilder(filter).addOrder("created", false);
	}

	protected QueryBuilder<Message> createQueryBuilder(SecurityFilter filter) {
		return new QueryBuilder<Message>(Message.class, filter);
	}

}
