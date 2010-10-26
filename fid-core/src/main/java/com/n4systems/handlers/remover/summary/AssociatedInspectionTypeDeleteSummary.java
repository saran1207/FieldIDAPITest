package com.n4systems.handlers.remover.summary;

public class AssociatedInspectionTypeDeleteSummary extends RemovalSummary {
	
	private Long removeFromAssetTypes;
	private Long deleteInspectionFrequencies;
	private Long deleteNonCompletedInspection;

	public AssociatedInspectionTypeDeleteSummary() {
		this(0L, 0L, 0L);
	}
	
	public AssociatedInspectionTypeDeleteSummary(Long removeFromAssetTypes, Long deleteInspectionFrequencies, Long deleteNonCompletedInspection) {
		this.removeFromAssetTypes = removeFromAssetTypes;
		this.deleteInspectionFrequencies = deleteInspectionFrequencies;
		this.deleteNonCompletedInspection = deleteNonCompletedInspection;
	}

	
	@Override
	public boolean canBeRemoved() {
		return true;
	}
	
	public Long getRemoveFromAssetTypes() {
		return removeFromAssetTypes;
	}

	public void setRemoveFromAssetTypes(Long removeFromAssetTypes) {
		this.removeFromAssetTypes = removeFromAssetTypes;
	}

	public Long getDeleteInspectionFrequencies() {
		return deleteInspectionFrequencies;
	}

	public void setDeleteInspectionFrequencies(Long deleteInspectionFrequencies) {
		this.deleteInspectionFrequencies = deleteInspectionFrequencies;
	}
	
	public void addToDeleteInspectionFrequencies(Long deleteInspectionFrequencies) {
		this.deleteInspectionFrequencies += deleteInspectionFrequencies;
	}

	public Long getDeleteNonCompletedInspection() {
		return deleteNonCompletedInspection;
	}

	public void setDeleteNonCompletedInspection(Long deleteNonCompletedInspection) {
		this.deleteNonCompletedInspection = deleteNonCompletedInspection;
	}
	
	public void addToDeleteNonCompletedInspection(Long deleteNonCompletedInspection) {
		this.deleteNonCompletedInspection += deleteNonCompletedInspection;
	}

}
