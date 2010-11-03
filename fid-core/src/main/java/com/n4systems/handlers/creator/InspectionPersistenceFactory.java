package com.n4systems.handlers.creator;

import com.n4systems.ejb.impl.CreateEventsMethodObject;
import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.handlers.creator.inspections.EventCreator;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.AuditLogger;
import com.n4systems.services.NextInspectionScheduleSerivce;

public interface InspectionPersistenceFactory {

	public EventSaver createInspectionSaver(Transaction transaction);

	public AuditLogger createCreateInspectionAuditLogger();
	
	public EventCreator createInspectionCreator();

	public CreateEventsMethodObject createCreateInspectionsMethodObject(Transaction transaction);

	public InspectionsInAGroupCreator createInspectionsInAGroupCreator();

	public NextInspectionScheduleSerivce createNextInspectionScheduleService(Transaction transaction);

}
