/**
 * 
 */
package com.n4systems.handlers.creator;

import com.n4systems.ejb.impl.CreateEventsMethodObject;
import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.handlers.creator.inspections.EventCreator;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.AuditLogger;
import com.n4systems.services.NextEventScheduleSerivce;

public class NullObjectDefaultedEventPersistenceFactory implements EventPersistenceFactory {
	public EventSaver eventSaver = new NullEventSaver();
	public AuditLogger auditLogger = new NullAuditLogger();
	public EventCreator eventCreator = null;
	public CreateEventsMethodObject createEventsMethodObject = new NullCreateEventsMethodObject();
	public NextEventScheduleSerivce nextEventScheduleSerivce = new NullNextEventScheduleSerivce();

	public EventSaver createEventSaver(Transaction transaction) {
		return eventSaver;
	}
	
	public AuditLogger createCreateEventAuditLogger() {
		return auditLogger;
	}

	public EventCreator createEventCreator() {
		return eventCreator;
	}

	public CreateEventsMethodObject createCreateEventsMethodObject(Transaction transaction) {
		return createEventsMethodObject;
	}

	public EventsInAGroupCreator createEventsInAGroupCreator() {
		return null;
	}

	public NextEventScheduleSerivce createNextEventScheduleService(Transaction transaction) {
		return nextEventScheduleSerivce;
	}
}