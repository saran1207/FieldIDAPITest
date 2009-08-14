package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.ScheduleListRemovalSummary;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;

public interface ScheduleListDeleteHandler extends RemovalHandler<ScheduleListRemovalSummary> {

	
	public ScheduleListDeleteHandler setInspectionType(InspectionType inspectionType);
	public ScheduleListDeleteHandler setAssociatedInspectionType(AssociatedInspectionType associatedInspectionType);
	
	public ScheduleListDeleteHandler targetNonCompleted();
	public ScheduleListDeleteHandler targetCompleted();
}
