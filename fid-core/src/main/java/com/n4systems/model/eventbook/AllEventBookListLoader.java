package com.n4systems.model.eventbook;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.EventBook;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AllEventBookListLoader extends ListLoader<EventBook> {

	public AllEventBookListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<EventBook> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<EventBook> builder = new QueryBuilder<EventBook>(EventBook.class, filter);
		List<EventBook> books = builder.getResultList(em);
		return books;
	}

}
