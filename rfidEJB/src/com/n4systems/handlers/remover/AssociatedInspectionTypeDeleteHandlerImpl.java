package com.n4systems.handlers.remover;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.remover.summary.AssociatedInspectionTypeDeleteSummary;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.inspectiontype.AssociatedInspectionTypeSaver;
import com.n4systems.persistence.Transaction;

public class AssociatedInspectionTypeDeleteHandlerImpl implements AssociatedInspectionTypeDeleteHandler {

	
	private final InspectionFrequenciesDeleteHandler inspectionFrequencyHandler;
	private final AssociatedInspectionTypeSaver associatedInspectionTypeSaver;
	private final ScheduleListDeleteHandler scheduleListDeleter;
	
	private Transaction transaction;
	
	private AssociatedInspectionType associatedInspectionType;

	
	public AssociatedInspectionTypeDeleteHandlerImpl(InspectionFrequenciesDeleteHandler inspectionFrequencyHandler, AssociatedInspectionTypeSaver associatedInspectionTypeSaver,
			ScheduleListDeleteHandler scheduleListDeleter) {
		super();
		this.inspectionFrequencyHandler = inspectionFrequencyHandler;
		this.associatedInspectionTypeSaver = associatedInspectionTypeSaver;
		this.scheduleListDeleter = scheduleListDeleter;
	}

	
	
	public void remove(Transaction transaction) {
		if (associatedInspectionType == null) {
			throw new InvalidArgumentException("you must give an associatedInspectionType");
		}
		
		setTransaction(transaction);
		
		deleteNonCompleteSchedules();
		deleteInspectionFrequencies();
		deleteAssociatedInspectionType();
		
		clearTransaction();
	}


	private void deleteAssociatedInspectionType() {
		associatedInspectionTypeSaver.remove(transaction, associatedInspectionType);
	}


	private void deleteInspectionFrequencies() {
		inspectionFrequencyHandler.forAssociatedInspectionType(associatedInspectionType).remove(transaction);
	}


	private void deleteNonCompleteSchedules() {
		scheduleListDeleter.targetNonCompleted().setAssociatedInspectionType(associatedInspectionType).remove(transaction);
	}


	public AssociatedInspectionTypeDeleteSummary summary(Transaction transaction) {
		setTransaction(transaction);
		
		AssociatedInspectionTypeDeleteSummary summary = new AssociatedInspectionTypeDeleteSummary();
		
		summary.setDeleteInspectionFrequencies(inspectionFrequencyHandler.forAssociatedInspectionType(associatedInspectionType).summary(transaction).getElementsToRemove());
		summary.setDeleteNonCompletedInspection(scheduleListDeleter.targetNonCompleted().setAssociatedInspectionType(associatedInspectionType).summary(transaction).getSchedulesToRemove());
		
		clearTransaction();
		return summary;
	}

	public AssociatedInspectionTypeDeleteHandler setAssociatedInspectionType(AssociatedInspectionType associatedInspectionType) {
		this.associatedInspectionType = associatedInspectionType;
		return this;
	}
	
	
	private void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	
	private void clearTransaction() {
		this.transaction = null;
	}
}
