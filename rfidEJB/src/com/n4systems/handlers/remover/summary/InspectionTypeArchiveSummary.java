package com.n4systems.handlers.remover.summary;

import com.n4systems.model.inspection.InspectionArchiveSummary;

public class InspectionTypeArchiveSummary extends RemovalSummary {

	private AssociatedInspectionTypeDeleteSummary associatedInspectionTypeDeleteSummary = new  AssociatedInspectionTypeDeleteSummary();
	private InspectionArchiveSummary inspectionArchiveSummary = new InspectionArchiveSummary();
	
	
	public InspectionTypeArchiveSummary() {
	}

	@Override
	public boolean canBeRemoved() {
		return (inspectionArchiveSummary.inspectionsCanBeArchived() && associatedInspectionTypeDeleteSummary.canBeRemoved());
	}
	
	public InspectionArchiveSummary getInspectionArchiveSummary() {
		return inspectionArchiveSummary;
	}

	public void setInspectionArchiveSummary(InspectionArchiveSummary inspectionArchiveSummary) {
		this.inspectionArchiveSummary = inspectionArchiveSummary;
	}
	

	public AssociatedInspectionTypeDeleteSummary getAssociatedInspectionTypeDeleteSummary() {
		return associatedInspectionTypeDeleteSummary;
	}

	public void setAssociatedInspectionTypeDeleteSummary(AssociatedInspectionTypeDeleteSummary associatedInspectionTypeDeleteSummary) {
		this.associatedInspectionTypeDeleteSummary = associatedInspectionTypeDeleteSummary;
	}


	
	public Long getRemoveFromProductTypes() {
		return associatedInspectionTypeDeleteSummary.getRemoveFromProductTypes();
	}

	public Long getDeleteInspectionFrequencies() {
		return associatedInspectionTypeDeleteSummary.getDeleteInspectionFrequencies();
	}

	public Long getDeleteSchedules() {
		return inspectionArchiveSummary.getDeleteCompletedSchedules() + associatedInspectionTypeDeleteSummary.getDeleteNonCompletedInspection();
	}


	public Long getDeleteInspections() {
		return inspectionArchiveSummary.getDeleteInspections();
	}

	public Long getInspectionsPartOfMaster() {
		return inspectionArchiveSummary.getInspectionsPartOfMaster();
	}


	
}
