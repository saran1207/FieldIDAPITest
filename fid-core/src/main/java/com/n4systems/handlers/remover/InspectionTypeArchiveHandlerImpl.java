package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.InspectionTypeArchiveSummary;
import com.n4systems.model.InspectionType;
import com.n4systems.model.inspectiontype.InspectionTypeSaver;
import com.n4systems.persistence.Transaction;

public class InspectionTypeArchiveHandlerImpl implements InspectionTypeArchiveHandler {

	private final InspectionTypeSaver inspectionTypeSaver;
	private final CatalogElementRemovalHandler catalogElementRemovalHandler;
	private final InspectionTypeListArchiveHandler inspectionDeleter;
	private final AssociatedInspectionTypeListDeleteHandler associatedInspectionTypesDeleteHandler;
	private final NotificationSettingDeleteHandler notificationSettingDeleteHandler;
	
	private InspectionType inspectionType;
	private Transaction transaction;
	private InspectionTypeArchiveSummary summary;
	
	public InspectionTypeArchiveHandlerImpl(InspectionTypeSaver inspectionTypeSaver, InspectionTypeListArchiveHandler inspectionListDeleter, AssociatedInspectionTypeListDeleteHandler associatedInspectionTypesDeleteHandler, CatalogElementRemovalHandler catalogElementRemovalHandler, NotificationSettingDeleteHandler notificationSettingDeleteHandler) {
		super();
		this.inspectionTypeSaver = inspectionTypeSaver;
		this.inspectionDeleter = inspectionListDeleter;
		this.associatedInspectionTypesDeleteHandler = associatedInspectionTypesDeleteHandler;
		this.catalogElementRemovalHandler = catalogElementRemovalHandler;
		this.notificationSettingDeleteHandler = notificationSettingDeleteHandler;
	}


	public void remove(Transaction transaction) {
		this.transaction = transaction;
		
		breakConnectionsToProductType();
		archiveInspectionsOfType();
		removeInspectionTypeFromCatalog();
		deleteNotificationSettingsUsing();
		archiveInspectionType();
		
		this.transaction = null;
	}


	private void breakConnectionsToProductType() {
		associatedInspectionTypesDeleteHandler.setInspectionType(inspectionType).remove(transaction);
	}
	

	private void archiveInspectionsOfType() {
		inspectionDeleter.setInspectionType(inspectionType).remove(transaction);
	}


	private void removeInspectionTypeFromCatalog() {
		catalogElementRemovalHandler.setInspectionType(inspectionType).cleanUp(transaction);
	}
	
	private void deleteNotificationSettingsUsing() {
		notificationSettingDeleteHandler.forInspectionType(inspectionType).remove(transaction);
	}
	
	
	private void archiveInspectionType() {
		inspectionType.archiveEntity();
		inspectionType = inspectionTypeSaver.update(transaction, inspectionType);
	}

	public InspectionTypeArchiveSummary summary(Transaction transaction) {
		summary = new InspectionTypeArchiveSummary();
		
		summary.setAssociatedInspectionTypeDeleteSummary(associatedInspectionTypesDeleteHandler.setInspectionType(inspectionType).summary(transaction));
		summary.setInspectionArchiveSummary(inspectionDeleter.setInspectionType(inspectionType).summary(transaction));
		summary.setNotificationSettingDeleteSummary(notificationSettingDeleteHandler.forInspectionType(inspectionType).summary(transaction));
		
		return summary;
	}
	
	public InspectionTypeArchiveHandler forInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}
}
