package com.n4systems.handlers.remover.summary;


public class EventTypeArchiveSummary extends RemovalSummary {

	private AssociatedEventTypeDeleteSummary associatedEventTypeDeleteSummary = new AssociatedEventTypeDeleteSummary();
	private EventArchiveSummary eventArchiveSummary = new EventArchiveSummary();
	private NotificationSettingDeleteSummary notificationSettingDeleteSummary = new NotificationSettingDeleteSummary();
	
	public EventTypeArchiveSummary() {
	}

	@Override
	public boolean canBeRemoved() {
		return (eventArchiveSummary.canBeRemoved() && associatedEventTypeDeleteSummary.canBeRemoved());
	}
	
	public EventArchiveSummary getEventArchiveSummary() {
		return eventArchiveSummary;
	}

	public void setEventArchiveSummary(EventArchiveSummary eventArchiveSummary) {
		this.eventArchiveSummary = eventArchiveSummary;
	}
	

	public AssociatedEventTypeDeleteSummary getAssociatedEventTypeDeleteSummary() {
		return associatedEventTypeDeleteSummary;
	}

	public void setAssociatedEventTypeDeleteSummary(AssociatedEventTypeDeleteSummary associatedEventTypeDeleteSummary) {
		this.associatedEventTypeDeleteSummary = associatedEventTypeDeleteSummary;
	}

	
	public NotificationSettingDeleteSummary getNotificationSettingDeleteSummary() {
		return notificationSettingDeleteSummary;
	}

	public void setNotificationSettingDeleteSummary(NotificationSettingDeleteSummary notificationSettingDeleteSummary) {
		this.notificationSettingDeleteSummary = notificationSettingDeleteSummary;
	}

	
	public Long getRemoveFromAssetTypes() {
		return associatedEventTypeDeleteSummary.getRemoveFromAssetTypes();
	}

	public Long getDeleteEventFrequencies() {
		return associatedEventTypeDeleteSummary.getDeleteEventFrequencies();
	}

	public Long getDeleteSchedules() {
		return eventArchiveSummary.getDeleteCompletedSchedules() + associatedEventTypeDeleteSummary.getDeleteNonCompletedEvent();
	}


	public Long getDeleteEvents() {
		return eventArchiveSummary.getDeleteEvents();
	}

	public Long getEventsPartOfMaster() {
		return eventArchiveSummary.getEventsPartOfMaster();
	}

	
	public Long getNotificationsToDelete() {
		return notificationSettingDeleteSummary.getNotificationsToDelete();
	}
	
}
