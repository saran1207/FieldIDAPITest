package com.n4systems.model.inspection;

import javax.persistence.EntityManager;

import com.n4systems.model.Event;
import com.n4systems.model.SubEvent;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class EventBySubEventLoader extends Loader<Event> {

	private SubEvent subEvent;
	
	public EventBySubEventLoader() {}
	
	@Override
	protected Event load(EntityManager em) {
		QueryBuilder<Event> builder = new QueryBuilder<Event>(SubEventRelation.class, new OpenSecurityFilter());
		builder.setSimpleSelect("masterEvent");
		builder.addWhere(WhereClauseFactory.create("subEvent.id", subEvent.getId()));
		
		Event event = builder.getSingleResult(em);
		return event;
	}

	public EventBySubEventLoader setSubEvent(SubEvent subEvent) {
		this.subEvent = subEvent;
		return this;
	}
	
}
