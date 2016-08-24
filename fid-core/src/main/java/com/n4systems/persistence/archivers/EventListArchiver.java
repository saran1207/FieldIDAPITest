package com.n4systems.persistence.archivers;

import com.n4systems.model.Event;
import com.n4systems.model.user.User;

import java.util.Set;

public class EventListArchiver extends EntityListArchiver<Event> {
	
	public EventListArchiver(Set<Long> ids, User modifyUser) {
		super(Event.class, ids, modifyUser);
	}

	public EventListArchiver(Set<Long> ids) {
		super(Event.class, ids);
	}

}
