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

public class NullObjectDefaultedInspectionPersistenceFactory implements InspectionPersistenceFactory {
	public EventSaver eventSaver = new NullEventSaver();
	public AuditLogger auditLogger = new NullAuditLogger();
	public EventCreator eventCreator = null;
	public CreateEventsMethodObject createEventsMethodObject = new NullCreateInspectionsMethodObject();
	public NextInspectionScheduleSerivce nextInspectionScheduleSerivce = new NullNextInspectionScheduleSerivce();

	public EventSaver createInspectionSaver(Transaction transaction) {
		return eventSaver;
	}
	
	public AuditLogger createCreateInspectionAuditLogger() {
		return auditLogger;
	}

	public EventCreator createInspectionCreator() {
		return eventCreator;
	}

	public CreateEventsMethodObject createCreateInspectionsMethodObject(Transaction transaction) {
		return createEventsMethodObject;
	}

	public InspectionsInAGroupCreator createInspectionsInAGroupCreator() {
		return null;
	}

	public NextInspectionScheduleSerivce createNextInspectionScheduleService(Transaction transaction) {
		return nextInspectionScheduleSerivce;
	}
}