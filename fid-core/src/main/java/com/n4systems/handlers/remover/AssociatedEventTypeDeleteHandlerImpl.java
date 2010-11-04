package com.n4systems.handlers.remover;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.remover.summary.AssociatedEventTypeDeleteSummary;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.eventtype.AssociatedEventTypeSaver;
import com.n4systems.persistence.Transaction;

public class AssociatedEventTypeDeleteHandlerImpl implements AssociatedEventTypeDeleteHandler {

	
	private final EventFrequenciesDeleteHandler eventFrequencyHandler;
	private final AssociatedEventTypeSaver associatedEventTypeSaver;
	private final ScheduleListDeleteHandler scheduleListDeleter;
	
	private Transaction transaction;
	
	private AssociatedEventType associatedEventType;

	
	public AssociatedEventTypeDeleteHandlerImpl(EventFrequenciesDeleteHandler eventFrequencyHandler, AssociatedEventTypeSaver associatedEventTypeSaver,
			ScheduleListDeleteHandler scheduleListDeleter) {
		super();
		this.eventFrequencyHandler = eventFrequencyHandler;
		this.associatedEventTypeSaver = associatedEventTypeSaver;
		this.scheduleListDeleter = scheduleListDeleter;
	}

	
	
	public void remove(Transaction transaction) {
		if (associatedEventType == null) {
			throw new InvalidArgumentException("you must give an associatedEventType");
		}
		
		setTransaction(transaction);
		
		deleteNonCompleteSchedules();
		deleteEventFrequencies();
		deleteAssociatedEventType();
		
		clearTransaction();
	}


	private void deleteAssociatedEventType() {
		associatedEventTypeSaver.remove(transaction, associatedEventType);
	}


	private void deleteEventFrequencies() {
		eventFrequencyHandler.forAssociatedEventType(associatedEventType).remove(transaction);
	}


	private void deleteNonCompleteSchedules() {
		scheduleListDeleter.targetNonCompleted().setAssociatedEventType(associatedEventType).remove(transaction);
	}


	public AssociatedEventTypeDeleteSummary summary(Transaction transaction) {
		setTransaction(transaction);
		
		AssociatedEventTypeDeleteSummary summary = new AssociatedEventTypeDeleteSummary();
		
		summary.setDeleteEventFrequencies(eventFrequencyHandler.forAssociatedEventType(associatedEventType).summary(transaction).getElementsToRemove());
		summary.setDeleteNonCompletedEvent(scheduleListDeleter.targetNonCompleted().setAssociatedEventType(associatedEventType).summary(transaction).getSchedulesToRemove());
		
		clearTransaction();
		return summary;
	}

	public AssociatedEventTypeDeleteHandler setAssociatedEventType(AssociatedEventType associatedEventType) {
		this.associatedEventType = associatedEventType;
		return this;
	}
	
	
	private void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	
	private void clearTransaction() {
		this.transaction = null;
	}
}
