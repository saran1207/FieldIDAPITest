package com.n4systems.handlers.remover.summary;


public class InspectionTypeArchiveSummary extends RemovalSummary {

	private AssociatedInspectionTypeDeleteSummary associatedInspectionTypeDeleteSummary = new  AssociatedInspectionTypeDeleteSummary();
	private InspectionArchiveSummary inspectionArchiveSummary = new InspectionArchiveSummary();
	private NotificationSettingDeleteSummary notificationSettingDeleteSummary = new NotificationSettingDeleteSummary();
	
	public InspectionTypeArchiveSummary() {
	}

	@Override
	public boolean canBeRemoved() {
		return (inspectionArchiveSummary.canBeRemoved() && associatedInspectionTypeDeleteSummary.canBeRemoved());
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

	
	public NotificationSettingDeleteSummary getNotificationSettingDeleteSummary() {
		return notificationSettingDeleteSummary;
	}

	public void setNotificationSettingDeleteSummary(NotificationSettingDeleteSummary notificationSettingDeleteSummary) {
		this.notificationSettingDeleteSummary = notificationSettingDeleteSummary;
	}

	
	public Long getRemoveFromProductTypes() {
		return associatedInspectionTypeDeleteSummary.getRemoveFromAssetTypes();
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

	
	public Long getNotificationsToDelete() {
		return notificationSettingDeleteSummary.getNotificationsToDelete();
	}
	
}
