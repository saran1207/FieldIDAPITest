package com.n4systems.handlers.remover.summary;

public class AssociatedEventTypeDeleteSummary extends RemovalSummary {
	
	private Long removeFromAssetTypes;
	private Long deleteEventFrequencies;
	private Long deleteNonCompletedEvent;

	public AssociatedEventTypeDeleteSummary() {
		this(0L, 0L, 0L);
	}
	
	public AssociatedEventTypeDeleteSummary(Long removeFromAssetTypes, Long deleteEventFrequencies, Long deleteNonCompletedEvent) {
		this.removeFromAssetTypes = removeFromAssetTypes;
		this.deleteEventFrequencies = deleteEventFrequencies;
		this.deleteNonCompletedEvent = deleteNonCompletedEvent;
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

	public Long getDeleteEventFrequencies() {
		return deleteEventFrequencies;
	}

	public void setDeleteEventFrequencies(Long deleteEventFrequencies) {
		this.deleteEventFrequencies = deleteEventFrequencies;
	}
	
	public void addToDeleteInspectionFrequencies(Long deleteInspectionFrequencies) {
		this.deleteEventFrequencies += deleteInspectionFrequencies;
	}

	public Long getDeleteNonCompletedEvent() {
		return deleteNonCompletedEvent;
	}

	public void setDeleteNonCompletedEvent(Long deleteNonCompletedEvent) {
		this.deleteNonCompletedEvent = deleteNonCompletedEvent;
	}
	
	public void addToDeleteNonCompletedInspection(Long deleteNonCompletedEvent) {
		this.deleteNonCompletedEvent += deleteNonCompletedEvent;
	}

}
