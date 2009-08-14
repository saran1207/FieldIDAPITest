package com.n4systems.handlers.remover.summary;

public class AssociatedInspectionTypeDeleteSummary extends RemovalSummary {
	
	private Long removeFromProductTypes;
	private Long deleteInspectionFrequencies;
	private Long deleteNonCompletedInspection;

	public AssociatedInspectionTypeDeleteSummary() {
		this(0L, 0L, 0L);
	}
	
	public AssociatedInspectionTypeDeleteSummary(Long removeFromProductTypes, Long deleteInspectionFrequencies, Long deleteNonCompletedInspection) {
		this.removeFromProductTypes = removeFromProductTypes;
		this.deleteInspectionFrequencies = deleteInspectionFrequencies;
		this.deleteNonCompletedInspection = deleteNonCompletedInspection;
	}

	
	@Override
	public boolean canBeRemoved() {
		return true;
	}
	
	public Long getRemoveFromProductTypes() {
		return removeFromProductTypes;
	}

	public void setRemoveFromProductTypes(Long removeFromProductTypes) {
		this.removeFromProductTypes = removeFromProductTypes;
	}

	public Long getDeleteInspectionFrequencies() {
		return deleteInspectionFrequencies;
	}

	public void setDeleteInspectionFrequencies(Long deleteInspectionFrequencies) {
		this.deleteInspectionFrequencies = deleteInspectionFrequencies;
	}

	public Long getDeleteNonCompletedInspection() {
		return deleteNonCompletedInspection;
	}

	public void setDeleteNonCompletedInspection(Long deleteNonCompletedInspection) {
		this.deleteNonCompletedInspection = deleteNonCompletedInspection;
	}

}
