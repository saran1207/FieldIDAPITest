package com.n4systems.model.eventtype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.EventType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;

public class EventTypeFormVersionLoader extends ListLoader<EventTypeFormVersion> {

	public EventTypeFormVersionLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<EventTypeFormVersion> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<EventTypeFormVersion> builder = new QueryBuilder<EventTypeFormVersion>(EventType.class, filter);
		builder.setSelectArgument(new NewObjectSelect(EventTypeFormVersion.class, "id", "formVersion"));
		
		List<EventTypeFormVersion> versions = builder.getResultList(em);
		return versions;
	}
	
}
