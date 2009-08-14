package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.InspectionFrequencyDeleteSummary;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;

public interface InspectionFrequenciesDeleteHandler extends RemovalHandler<InspectionFrequencyDeleteSummary> {
	
	public InspectionFrequenciesDeleteHandler forInspectionType(InspectionType inspectionType);
	public InspectionFrequenciesDeleteHandler forAssociatedInspectionType(AssociatedInspectionType associatedInspectionType);
}
