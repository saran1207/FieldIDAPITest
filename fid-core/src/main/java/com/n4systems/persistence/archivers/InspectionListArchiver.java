package com.n4systems.persistence.archivers;

import java.util.Set;


import com.n4systems.model.Event;
import com.n4systems.model.user.User;

public class InspectionListArchiver extends EntityListArchiver<Event> {
	
	public InspectionListArchiver(Set<Long> ids, User modifyUser) {
		super(Event.class, ids, modifyUser);
	}

	public InspectionListArchiver(Set<Long> ids) {
		super(Event.class, ids);
	}

}
