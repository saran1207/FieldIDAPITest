package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.InspectionTypeArchiveSummary;
import com.n4systems.model.InspectionType;

public interface InspectionTypeArchiveHandler extends RemovalHandler<InspectionTypeArchiveSummary> {
	
	public InspectionTypeArchiveHandler forInspectionType(InspectionType inspectionType);
}
