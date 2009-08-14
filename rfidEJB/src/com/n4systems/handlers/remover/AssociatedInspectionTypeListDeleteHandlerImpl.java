package com.n4systems.handlers.remover;

import java.util.List;

import com.n4systems.handlers.remover.summary.AssociatedInspectionTypeDeleteSummary;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.inspectiontype.AssociatedInspectionTypesLoader;
import com.n4systems.persistence.Transaction;

public class AssociatedInspectionTypeListDeleteHandlerImpl  implements AssociatedInspectionTypeListDeleteHandler {

	private final AssociatedInspectionTypesLoader associatedInspectionTypeLoader;
	private final AssociatedInspectionTypeDeleteHandler associatedInspectionTypeDeleteHandler;

	private InspectionType inspectionType;
	
	public AssociatedInspectionTypeListDeleteHandlerImpl(AssociatedInspectionTypesLoader associatedInspectionTypeLoader, AssociatedInspectionTypeDeleteHandler associatedInspectionTypeDeleteHandler) {
		super();
		this.associatedInspectionTypeLoader = associatedInspectionTypeLoader;
		this.associatedInspectionTypeDeleteHandler = associatedInspectionTypeDeleteHandler;
	}
	
	public void remove(Transaction transaction) {
		List<AssociatedInspectionType> associations = associatedInspectionTypeLoader.setInspectionType(inspectionType).load(transaction);
		for (AssociatedInspectionType associatedInspectionType : associations) {
			associatedInspectionTypeDeleteHandler.setAssociatedInspectionType(associatedInspectionType).remove(transaction);
		}
	}

	public AssociatedInspectionTypeDeleteSummary summary(Transaction transaction) {
		AssociatedInspectionTypeDeleteSummary summary = new AssociatedInspectionTypeDeleteSummary();
		summary.setRemoveFromProductTypes(new Long(associatedInspectionTypeLoader.setInspectionType(inspectionType).load(transaction).size()));
		summary.setDeleteNonCompletedInspection(0L); //FIXME find all schedules that are non completed of type X may iterate on list above and get summaries from the aitdeleter.
		summary.setDeleteInspectionFrequencies(0L); //FIXME find all inspection frequencies for type X
		return summary;
	}
	
	public AssociatedInspectionTypeListDeleteHandler setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}

}
