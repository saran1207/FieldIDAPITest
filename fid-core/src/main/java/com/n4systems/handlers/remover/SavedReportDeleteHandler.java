package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.SavedReportDeleteSummary;
import com.n4systems.model.EventType;

public interface SavedReportDeleteHandler extends RemovalHandler<SavedReportDeleteSummary> {

	public SavedReportDeleteHandler forEventType(EventType eventType);
	
}
