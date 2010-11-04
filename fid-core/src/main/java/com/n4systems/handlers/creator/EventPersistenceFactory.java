package com.n4systems.handlers.creator;

import com.n4systems.ejb.impl.CreateEventsMethodObject;
import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.handlers.creator.inspections.EventCreator;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.AuditLogger;
import com.n4systems.services.NextInspectionScheduleSerivce;

public interface EventPersistenceFactory {

	public EventSaver createEventSaver(Transaction transaction);

	public AuditLogger createCreateEventAuditLogger();
	
	public EventCreator createEventCreator();

	public CreateEventsMethodObject createCreateEventsMethodObject(Transaction transaction);

	public InspectionsInAGroupCreator createEventsInAGroupCreator();

	public NextInspectionScheduleSerivce createNextEventScheduleService(Transaction transaction);

}
