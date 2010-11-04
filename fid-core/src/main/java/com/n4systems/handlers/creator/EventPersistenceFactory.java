package com.n4systems.handlers.creator;

import com.n4systems.ejb.impl.CreateEventsMethodObject;
import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.handlers.creator.events.EventCreator;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.AuditLogger;
import com.n4systems.services.NextEventScheduleSerivce;

public interface EventPersistenceFactory {

	public EventSaver createEventSaver(Transaction transaction);

	public AuditLogger createCreateEventAuditLogger();
	
	public EventCreator createEventCreator();

	public CreateEventsMethodObject createCreateEventsMethodObject(Transaction transaction);

	public EventsInAGroupCreator createEventsInAGroupCreator();

	public NextEventScheduleSerivce createNextEventScheduleService(Transaction transaction);

}
