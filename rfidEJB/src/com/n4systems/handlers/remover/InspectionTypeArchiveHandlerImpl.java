package com.n4systems.handlers.remover;

import com.n4systems.handlers.remover.summary.InspectionTypeArchiveSummary;
import com.n4systems.model.InspectionType;
import com.n4systems.model.inspection.InspectionListDeleter;
import com.n4systems.model.inspectiontype.InspectionTypeSaver;
import com.n4systems.persistence.Transaction;

public class InspectionTypeArchiveHandlerImpl implements InspectionTypeArchiveHandler {

	private final InspectionTypeSaver inspectionTypeSaver;
	private final CatalogElementRemovalHandler catalogElementRemovalHandler;
	private final InspectionListDeleter inspectionDeleter;
	private final AssociatedInspectionTypeListDeleteHandler associatedInspectionTypesDeleteHandler;
	
	private InspectionType inspectionType;
	private Transaction transaction;
	private InspectionTypeArchiveSummary summary;
	
	public InspectionTypeArchiveHandlerImpl(InspectionTypeSaver inspectionTypeSaver, InspectionListDeleter inspectionListDeleter, AssociatedInspectionTypeListDeleteHandler associatedInspectionTypesDeleteHandler, CatalogElementRemovalHandler catalogElementRemovalHandler) {
		super();
		this.inspectionTypeSaver = inspectionTypeSaver;
		this.inspectionDeleter = inspectionListDeleter;
		this.associatedInspectionTypesDeleteHandler = associatedInspectionTypesDeleteHandler;
		this.catalogElementRemovalHandler = catalogElementRemovalHandler;
	}


	public void remove(Transaction transaction) {
		this.transaction = transaction;
		
		breakConnectionsToProductType(inspectionType);
		archiveInspectionsOfType(inspectionType);
		removeInspectionTypeFromCatalog(inspectionType);
		archiveInspectionType(inspectionType);
	}

	
	private void breakConnectionsToProductType(InspectionType inspectionType) {
		associatedInspectionTypesDeleteHandler.setInspectionType(inspectionType).remove(transaction);
	}
	

	private void archiveInspectionsOfType(InspectionType inspectionType) {
		inspectionDeleter.setInspectionType(inspectionType).archive(transaction);
	}

	
	private void archiveInspectionType(InspectionType inspectionType) {
		inspectionType.archiveEntity();
		inspectionTypeSaver.update(transaction, inspectionType);
	}


	private void removeInspectionTypeFromCatalog(InspectionType inspectionType) {
		catalogElementRemovalHandler.setInspectionType(inspectionType).cleanUp(transaction);
	}


	public InspectionTypeArchiveSummary summary(Transaction transaction) {
		summary = new InspectionTypeArchiveSummary();
		summary.setAssociatedInspectionTypeDeleteSummary(associatedInspectionTypesDeleteHandler.setInspectionType(inspectionType).summary(transaction));
		summary.setInspectionArchiveSummary(inspectionDeleter.setInspectionType(inspectionType).summary(transaction));
		return summary;
	}
	
	public InspectionTypeArchiveHandler forInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}
}
