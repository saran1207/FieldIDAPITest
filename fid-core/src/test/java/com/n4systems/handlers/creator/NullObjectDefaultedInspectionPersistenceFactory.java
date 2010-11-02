/**
 * 
 */
package com.n4systems.handlers.creator;

import com.n4systems.ejb.impl.CreateInspectionsMethodObject;
import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.handlers.creator.inspections.InspectionCreator;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.AuditLogger;
import com.n4systems.services.NextInspectionScheduleSerivce;

public class NullObjectDefaultedInspectionPersistenceFactory implements InspectionPersistenceFactory {
	public EventSaver eventSaver = new NullEventSaver();
	public AuditLogger auditLogger = new NullAuditLogger();
	public InspectionCreator inspectionCreator = null;
	public CreateInspectionsMethodObject createInspectionsMethodObject = new NullCreateInspectionsMethodObject();
	public NextInspectionScheduleSerivce nextInspectionScheduleSerivce = new NullNextInspectionScheduleSerivce();

	public EventSaver createInspectionSaver(Transaction transaction) {
		return eventSaver;
	}
	
	public AuditLogger createCreateInspectionAuditLogger() {
		return auditLogger;
	}

	public InspectionCreator createInspectionCreator() {
		return inspectionCreator;
	}

	public CreateInspectionsMethodObject createCreateInspectionsMethodObject(Transaction transaction) {
		return createInspectionsMethodObject;
	}

	public InspectionsInAGroupCreator createInspectionsInAGroupCreator() {
		return null;
	}

	public NextInspectionScheduleSerivce createNextInspectionScheduleService(Transaction transaction) {
		return nextInspectionScheduleSerivce;
	}
}