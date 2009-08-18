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
		List<AssociatedInspectionType> associations = getAssociatedInspections(transaction);
		for (AssociatedInspectionType associatedInspectionType : associations) {
			associatedInspectionTypeDeleteHandler.setAssociatedInspectionType(associatedInspectionType).remove(transaction);
		}
	}

	private List<AssociatedInspectionType> getAssociatedInspections(Transaction transaction) {
		List<AssociatedInspectionType> associations = associatedInspectionTypeLoader.setInspectionType(inspectionType).load(transaction);
		return associations;
	}

	public AssociatedInspectionTypeDeleteSummary summary(Transaction transaction) {
		AssociatedInspectionTypeDeleteSummary summary = new AssociatedInspectionTypeDeleteSummary();
		summary.setRemoveFromProductTypes(new Long(associatedInspectionTypeLoader.setInspectionType(inspectionType).load(transaction).size()));
		
		List<AssociatedInspectionType> associations = getAssociatedInspections(transaction);
		for (AssociatedInspectionType associatedInspectionType : associations) {
			AssociatedInspectionTypeDeleteSummary singleAITSummary = associatedInspectionTypeDeleteHandler.setAssociatedInspectionType(associatedInspectionType).summary(transaction);
			summary.addToDeleteInspectionFrequencies(singleAITSummary.getDeleteInspectionFrequencies());
			summary.addToDeleteNonCompletedInspection(singleAITSummary.getDeleteNonCompletedInspection());
		}
		
		return summary;
	}
	
	public AssociatedInspectionTypeListDeleteHandler setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}

}
