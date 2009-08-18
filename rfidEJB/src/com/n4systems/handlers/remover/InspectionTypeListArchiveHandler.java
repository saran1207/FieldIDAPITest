package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.InspectionArchiveSummary;
import com.n4systems.model.InspectionType;

public interface InspectionTypeListArchiveHandler extends RemovalHandler<InspectionArchiveSummary>{

	public abstract InspectionTypeListArchiveHandler setInspectionType(InspectionType inspectionType);

}