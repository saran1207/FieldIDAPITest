package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.AssociatedInspectionTypeDeleteSummary;
import com.n4systems.model.InspectionType;

public interface AssociatedInspectionTypeListDeleteHandler extends RemovalHandler<AssociatedInspectionTypeDeleteSummary> {
	
	public AssociatedInspectionTypeListDeleteHandler setInspectionType(InspectionType inspectionType);
}
