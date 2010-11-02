package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.EventArchiveSummary;
import com.n4systems.model.EventType;

public interface EventTypeListArchiveHandler extends RemovalHandler<EventArchiveSummary>{

	public abstract EventTypeListArchiveHandler setEventType(EventType eventType);

}