package com.n4systems.model.eventschedule;

import com.n4systems.model.Event;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;

import javax.persistence.EntityManager;
import java.util.Date;

public class NextEventDateByEventLoader extends SecurityFilteredLoader<Date> {
	private Event event;
	
	public NextEventDateByEventLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Date load(EntityManager em, SecurityFilter filter) {
        return event!=null ? event.getDueDate() : null;
	}

	public NextEventDateByEventLoader setEvent(Event event) {
		this.event = event;
		return this;
	}
}
