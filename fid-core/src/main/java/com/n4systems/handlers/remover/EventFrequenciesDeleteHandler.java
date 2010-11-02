package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.EventFrequencyDeleteSummary;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;

public interface EventFrequenciesDeleteHandler extends RemovalHandler<EventFrequencyDeleteSummary> {
	
	public EventFrequenciesDeleteHandler forEventType(EventType eventType);
	public EventFrequenciesDeleteHandler forAssociatedEventType(AssociatedEventType associatedEventType);
}
