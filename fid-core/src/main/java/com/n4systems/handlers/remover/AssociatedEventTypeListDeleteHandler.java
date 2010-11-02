package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.AssociatedEventTypeDeleteSummary;
import com.n4systems.model.EventType;

public interface AssociatedEventTypeListDeleteHandler extends RemovalHandler<AssociatedEventTypeDeleteSummary> {
	
	public AssociatedEventTypeListDeleteHandler setEventType(EventType eventType);
}
