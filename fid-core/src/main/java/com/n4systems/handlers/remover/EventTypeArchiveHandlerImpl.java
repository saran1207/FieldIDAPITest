package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.EventTypeArchiveSummary;
import com.n4systems.model.EventType;
import com.n4systems.model.eventtype.EventTypeSaver;
import com.n4systems.persistence.Transaction;

public class EventTypeArchiveHandlerImpl implements EventTypeArchiveHandler {

	private final EventTypeSaver eventTypeSaver;

	private final CatalogElementRemovalHandler catalogElementRemovalHandler;
	private final EventTypeListArchiveHandler eventDeleter;
	private final AssociatedEventTypeListDeleteHandler associatedEventTypesDeleteHandler;
	private final NotificationSettingDeleteHandler notificationSettingDeleteHandler;
	private final SavedReportDeleteHandler savedReportDeleteHandler;
	
	private EventType eventType;
	private Transaction transaction;
	private EventTypeArchiveSummary summary;
	
	public EventTypeArchiveHandlerImpl(EventTypeSaver eventTypeSaver, EventTypeListArchiveHandler eventListDeleter, 
			AssociatedEventTypeListDeleteHandler associatedEventTypesDeleteHandler, CatalogElementRemovalHandler catalogElementRemovalHandler, 
			NotificationSettingDeleteHandler notificationSettingDeleteHandler, SavedReportDeleteHandler savedReportDeleteHandler) {
		super();
		this.eventTypeSaver = eventTypeSaver;
		this.eventDeleter = eventListDeleter;
		this.associatedEventTypesDeleteHandler = associatedEventTypesDeleteHandler;
		this.catalogElementRemovalHandler = catalogElementRemovalHandler;
		this.notificationSettingDeleteHandler = notificationSettingDeleteHandler;
		this.savedReportDeleteHandler = savedReportDeleteHandler;
	}


	public void remove(Transaction transaction) {
		this.transaction = transaction;
		
		breakConnectionsToAssetType();
		archiveEventsOfType();
		removeEventTypeFromCatalog();
		deleteNotificationSettingsUsing();
		deleteSavedReportsWithEventTypeCriteria();
		archiveEventType();
		
		this.transaction = null;
	}


	private void deleteSavedReportsWithEventTypeCriteria() {
		savedReportDeleteHandler.forEventType(eventType).remove(transaction);
	}


	private void breakConnectionsToAssetType() {
		associatedEventTypesDeleteHandler.setEventType(eventType).remove(transaction);
	}
	

	private void archiveEventsOfType() {
		eventDeleter.setEventType(eventType).remove(transaction);
	}


	private void removeEventTypeFromCatalog() {
		catalogElementRemovalHandler.setEventType(eventType).cleanUp(transaction);
	}
	
	private void deleteNotificationSettingsUsing() {
		notificationSettingDeleteHandler.forEventType(eventType).remove(transaction);
	}
	
	
	private void archiveEventType() {
		eventType.archiveEntity();
		eventType = eventTypeSaver.update(transaction, eventType);
	}

	public EventTypeArchiveSummary summary(Transaction transaction) {
		summary = new EventTypeArchiveSummary();
		
		summary.setAssociatedEventTypeDeleteSummary(associatedEventTypesDeleteHandler.setEventType(eventType).summary(transaction));
		summary.setEventArchiveSummary(eventDeleter.setEventType(eventType).summary(transaction));
		summary.setNotificationSettingDeleteSummary(notificationSettingDeleteHandler.forEventType(eventType).summary(transaction));
		summary.setSavedReportDeleteSummary(savedReportDeleteHandler.forEventType(eventType).summary(transaction));
		return summary;
	}
	
	public EventTypeArchiveHandler forEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}
}
