package com.n4systems.model.inspection;

public class InspectionArchiveSummary {
	private Long deleteInspections;
	private Long inspectionsPartOfMaster;
	private Long deleteSchedules;
	
	
	
	public InspectionArchiveSummary() {
		this(0L, 0L, 0L);
	}

	public InspectionArchiveSummary(Long deleteInspections, Long inspectionsPartOfMaster, Long deleteSchedules) {
		super();
		this.deleteInspections = deleteInspections;
		this.inspectionsPartOfMaster = inspectionsPartOfMaster;
		this.deleteSchedules = deleteSchedules;
	}

	public boolean inspectionsCanBeArchived() {
		return inspectionsPartOfMaster == 0L;
	}
	
	public Long getInspectionsPartOfMaster() {
		return inspectionsPartOfMaster;
	}
	public InspectionArchiveSummary setInspectionsPartOfMaster(Long inspectionsPartOfMaster) {
		this.inspectionsPartOfMaster = inspectionsPartOfMaster;
		return this;
	}

	public Long getDeleteInspections() {
		return deleteInspections;
	}

	public InspectionArchiveSummary setDeleteInspections(Long deleteInspections) {
		this.deleteInspections = deleteInspections;
		return this;
	}

	public Long getDeleteCompletedSchedules() {
		return deleteSchedules;
	}

	public InspectionArchiveSummary setDeleteSchedules(Long deleteSchedules) {
		this.deleteSchedules = deleteSchedules;
		return this;
	}
}
