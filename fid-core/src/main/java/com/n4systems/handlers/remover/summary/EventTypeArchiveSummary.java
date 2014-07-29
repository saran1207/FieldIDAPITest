package com.n4systems.handlers.remover.summary;

import com.n4systems.model.EventType;

public class EventTypeArchiveSummary extends RemovalSummary {

	private AssociatedEventTypeDeleteSummary associatedEventTypeDeleteSummary = new AssociatedEventTypeDeleteSummary();
	private EventArchiveSummary eventArchiveSummary = new EventArchiveSummary();
	private NotificationSettingDeleteSummary notificationSettingDeleteSummary = new NotificationSettingDeleteSummary();
	private SavedReportDeleteSummary savedReportDeleteSummary = new SavedReportDeleteSummary();
    private AssociatedPlaceEventTypeSummary associatedPlaceEventTypeSummary = new AssociatedPlaceEventTypeSummary();

    private Long recurringProcedureAuditEvents = 0L;

    private EventType eventType;
	
	public EventTypeArchiveSummary(EventType eventType) {
        this.eventType = eventType;
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

	public Long getNotificationsToDelete() {
		return notificationSettingDeleteSummary.getNotificationsToDelete();
	}

	public Long getSavedReportsToDelete() {
		return savedReportDeleteSummary.getSavedReportsToDelete();
	}

	public void setSavedReportDeleteSummary(SavedReportDeleteSummary savedReportDeleteSummary) {
		this.savedReportDeleteSummary = savedReportDeleteSummary;
	}

    public AssociatedPlaceEventTypeSummary getAssociatedPlaceEventTypeSummary() {
        return associatedPlaceEventTypeSummary;
    }

    public void setAssociatedPlaceEventTypeSummary(AssociatedPlaceEventTypeSummary associatedPlaceEventTypeSummary) {
        this.associatedPlaceEventTypeSummary = associatedPlaceEventTypeSummary;
    }

    public Long getRemoveFromPlaces() {
        return associatedPlaceEventTypeSummary.getRemoveFromPlaces();
    }

    public Long getRecurringPlaceEventsToDelete() {
        return associatedPlaceEventTypeSummary.getRemoveFromPlaceRecurrences();
    }

    public Long getRecurringProcedureAuditEvents() {
        return recurringProcedureAuditEvents;
    }

    public void setRecurringProcedureAuditEvents(Long recurringProcedureAuditEvents) {
        this.recurringProcedureAuditEvents = recurringProcedureAuditEvents;
    }

    public boolean isThingEventType() {
        return eventType.isThingEventType();
    }

    public boolean isPlaceEventType() {
        return eventType.isPlaceEventType();
    }

    public boolean isActionEventType() {
        return eventType.isActionEventType();
    }

    public boolean isProcedureAuditEventType() {
        return eventType.isProcedureAuditEventType();
    }

}
