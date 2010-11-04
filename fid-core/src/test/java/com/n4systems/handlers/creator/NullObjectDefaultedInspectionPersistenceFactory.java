/**
 * 
 */
package com.n4systems.handlers.creator;

import com.n4systems.ejb.impl.CreateEventsMethodObject;
import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.handlers.creator.inspections.EventCreator;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.AuditLogger;
import com.n4systems.services.NextInspectionScheduleSerivce;

public class NullObjectDefaultedInspectionPersistenceFactory implements EventPersistenceFactory {
	public EventSaver eventSaver = new NullEventSaver();
	public AuditLogger auditLogger = new NullAuditLogger();
	public EventCreator eventCreator = null;
	public CreateEventsMethodObject createEventsMethodObject = new NullCreateEventsMethodObject();
	public NextInspectionScheduleSerivce nextInspectionScheduleSerivce = new NullNextInspectionScheduleSerivce();

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

	public InspectionsInAGroupCreator createEventsInAGroupCreator() {
		return null;
	}

	public NextInspectionScheduleSerivce createNextEventScheduleService(Transaction transaction) {
		return nextInspectionScheduleSerivce;
	}
}