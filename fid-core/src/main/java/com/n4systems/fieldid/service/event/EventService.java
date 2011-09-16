package com.n4systems.fieldid.service.event;

import static com.google.common.base.Preconditions.*;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Event;
import com.n4systems.util.persistence.QueryBuilder;

public class EventService extends FieldIdPersistenceService {
	
    @Transactional(readOnly = true)	
	public List<Event> getEventsByType(Long eventTypeId) {
		checkArgument(eventTypeId!=null, "you must specify an event type id to get a list of events.");
		QueryBuilder<Event> builder = createUserSecurityBuilder(Event.class);    //FIXME DD : Change to user.
		builder.addSimpleWhere("type.id", eventTypeId);
        builder.addOrder("date");
        return persistenceService.findAll(builder);
	}	

}
