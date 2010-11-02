package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.AssociatedEventTypeDeleteSummary;
import com.n4systems.model.AssociatedEventType;

public interface AssociatedEventTypeDeleteHandler extends RemovalHandler<AssociatedEventTypeDeleteSummary> {

	public AssociatedEventTypeDeleteHandler setAssociatedEventType(AssociatedEventType associatedEventType);
}
