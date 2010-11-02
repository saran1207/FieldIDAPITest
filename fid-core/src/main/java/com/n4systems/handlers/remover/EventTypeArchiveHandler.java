package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.EventTypeArchiveSummary;
import com.n4systems.model.EventType;

public interface EventTypeArchiveHandler extends RemovalHandler<EventTypeArchiveSummary> {
	
	public EventTypeArchiveHandler forEventType(EventType eventType);
}
