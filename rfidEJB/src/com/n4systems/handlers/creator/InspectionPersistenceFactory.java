package com.n4systems.handlers.creator;

import com.n4systems.ejb.impl.CreateInspectionsMethodObject;
import com.n4systems.ejb.impl.InspectionSaver;
import com.n4systems.handlers.creator.inspections.InspectionCreator;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.AuditLogger;

public interface InspectionPersistenceFactory {

	public InspectionSaver createInspectionSaver(Transaction transaction);

	public AuditLogger createCreateInspectionAuditLogger();
	
	public InspectionCreator createInspectionCreator();

	public CreateInspectionsMethodObject createCreateInspectionsMethodObject(Transaction transaction);

	public InspectionsInAGroupCreator createInspectionsInAGroupCreator();

}
