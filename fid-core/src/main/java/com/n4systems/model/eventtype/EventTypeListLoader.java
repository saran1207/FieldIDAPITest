package com.n4systems.model.eventtype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.EventType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class EventTypeListLoader extends ListLoader<EventType> {

	public EventTypeListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<EventType> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<EventType> builder = new QueryBuilder<EventType>(EventType.class, filter);
		builder.addPostFetchPaths("eventForm.sections", "infoFieldNames");
		List<EventType> eventTypes = builder.getResultList(em);
		return eventTypes;
	}
}
