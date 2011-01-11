package com.n4systems.model.eventtype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.EventType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class EventTypeListLoader extends ListLoader<EventType> {
	
	private String[] postFetchFields = new String[0];

	public EventTypeListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<EventType> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<EventType> builder = new QueryBuilder<EventType>(EventType.class, filter);
		builder.addOrder("name");
		builder.addPostFetchPaths("eventForm.sections", "infoFieldNames");
		builder.addPostFetchPaths(postFetchFields);
		return builder.getResultList(em);
	}
	
	public EventTypeListLoader setPostFetchFields(String...fields) {
		this.postFetchFields = fields;
		return this;
	}
	
	
}
