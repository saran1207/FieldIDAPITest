package com.n4systems.model.eventbook;

import javax.persistence.EntityManager;

import com.n4systems.model.EventBook;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class EventBookByMobileIdLoader extends SecurityFilteredLoader<EventBook> {
	private String mobileId;
	
	public EventBookByMobileIdLoader(SecurityFilter filter) {
		super(filter);
	}
	@Override
	public EventBook load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<EventBook> builder = new QueryBuilder<EventBook>(EventBook.class, filter);
		builder.addWhere(WhereClauseFactory.create("mobileId", mobileId));
		
		EventBook book = builder.getSingleResult(em);
		return book;
	}
	public EventBookByMobileIdLoader setMobileId(String mobileId) {
		this.mobileId = mobileId;
		return this;
	}
}
