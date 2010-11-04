package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.ScheduleListRemovalSummary;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;

public interface ScheduleListDeleteHandler extends RemovalHandler<ScheduleListRemovalSummary> {

	
	public ScheduleListDeleteHandler setEventType(EventType eventType);
	public ScheduleListDeleteHandler setAssociatedEventType(AssociatedEventType associatedEventType);
	
	public ScheduleListDeleteHandler targetNonCompleted();
	public ScheduleListDeleteHandler targetCompleted();
}
