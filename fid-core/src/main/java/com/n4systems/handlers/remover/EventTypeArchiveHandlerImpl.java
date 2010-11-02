package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.EventTypeArchiveSummary;
import com.n4systems.model.EventType;
import com.n4systems.model.inspectiontype.EventTypeSaver;
import com.n4systems.persistence.Transaction;

public class EventTypeArchiveHandlerImpl implements EventTypeArchiveHandler {

	private final EventTypeSaver eventTypeSaver;
	private final CatalogElementRemovalHandler catalogElementRemovalHandler;
	private final EventTypeListArchiveHandler eventDeleter;
	private final AssociatedEventTypeListDeleteHandler associatedEventTypesDeleteHandler;
	private final NotificationSettingDeleteHandler notificationSettingDeleteHandler;
	
	private EventType eventType;
	private Transaction transaction;
	private EventTypeArchiveSummary summary;
	
	public EventTypeArchiveHandlerImpl(EventTypeSaver eventTypeSaver, EventTypeListArchiveHandler eventListDeleter, AssociatedEventTypeListDeleteHandler associatedEventTypesDeleteHandler, CatalogElementRemovalHandler catalogElementRemovalHandler, NotificationSettingDeleteHandler notificationSettingDeleteHandler) {
		super();
		this.eventTypeSaver = eventTypeSaver;
		this.eventDeleter = eventListDeleter;
		this.associatedEventTypesDeleteHandler = associatedEventTypesDeleteHandler;
		this.catalogElementRemovalHandler = catalogElementRemovalHandler;
		this.notificationSettingDeleteHandler = notificationSettingDeleteHandler;
	}


	public void remove(Transaction transaction) {
		this.transaction = transaction;
		
		breakConnectionsToAssetType();
		archiveInspectionsOfType();
		removeInspectionTypeFromCatalog();
		deleteNotificationSettingsUsing();
		archiveInspectionType();
		
		this.transaction = null;
	}


	private void breakConnectionsToAssetType() {
		associatedEventTypesDeleteHandler.setEventType(eventType).remove(transaction);
	}
	

	private void archiveInspectionsOfType() {
		eventDeleter.setEventType(eventType).remove(transaction);
	}


	private void removeInspectionTypeFromCatalog() {
		catalogElementRemovalHandler.setEventType(eventType).cleanUp(transaction);
	}
	
	private void deleteNotificationSettingsUsing() {
		notificationSettingDeleteHandler.forEventType(eventType).remove(transaction);
	}
	
	
	private void archiveInspectionType() {
		eventType.archiveEntity();
		eventType = eventTypeSaver.update(transaction, eventType);
	}

	public EventTypeArchiveSummary summary(Transaction transaction) {
		summary = new EventTypeArchiveSummary();
		
		summary.setAssociatedEventTypeDeleteSummary(associatedEventTypesDeleteHandler.setEventType(eventType).summary(transaction));
		summary.setEventArchiveSummary(eventDeleter.setEventType(eventType).summary(transaction));
		summary.setNotificationSettingDeleteSummary(notificationSettingDeleteHandler.forEventType(eventType).summary(transaction));
		
		return summary;
	}
	
	public EventTypeArchiveHandler forEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}
}
