package com.n4systems.model.messages;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class PaginatedMessageLoaderTestExtention extends PaginatedMessageLoader {

	private QueryBuilder<Message> builder;
	
	public PaginatedMessageLoaderTestExtention(SecurityFilter filter) {
		super(filter);
	}
	
	
	
	public void setTestQueryBuilder(QueryBuilder<Message> builder) {
		this.builder = builder;
	}
	protected QueryBuilder<Message> createQueryBuilder(SecurityFilter filter) {
		return builder;
	}
}
