/**
 * 
 */
package com.n4systems.handlers.creator;

import com.n4systems.ejb.impl.CreateInspectionsMethodObject;
import com.n4systems.ejb.impl.InspectionSaver;
import com.n4systems.handlers.creator.inspections.InspectionCreator;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.AuditLogger;

public class NullObjectDefaultedInspectionPersistenceFactory implements InspectionPersistenceFactory {
	public InspectionSaver inspectionSaver = new NullInspectionSaver();
	public AuditLogger auditLogger = new NullAuditLogger();
	public InspectionCreator inspectionCreator = null;
	public CreateInspectionsMethodObject createInspectionsMethodObject = new NullCreateInspectionsMethodObject();

	public InspectionSaver createInspectionSaver(Transaction transaction) {
		return inspectionSaver;
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
}