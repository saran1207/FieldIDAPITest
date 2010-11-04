package com.n4systems.handlers.remover;

import java.util.List;

import com.n4systems.handlers.remover.summary.AssociatedEventTypeDeleteSummary;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.eventtype.AssociatedEventTypesLoader;
import com.n4systems.persistence.Transaction;

public class AssociatedEventTypeListDeleteHandlerImpl implements AssociatedEventTypeListDeleteHandler {

	private final AssociatedEventTypesLoader associatedEventTypeLoader;
	private final AssociatedEventTypeDeleteHandler associatedEventTypeDeleteHandler;

	private EventType eventType;
	
	public AssociatedEventTypeListDeleteHandlerImpl(AssociatedEventTypesLoader associatedEventTypeLoader, AssociatedEventTypeDeleteHandler associatedEventTypeDeleteHandler) {
		super();
		this.associatedEventTypeLoader = associatedEventTypeLoader;
		this.associatedEventTypeDeleteHandler = associatedEventTypeDeleteHandler;
	}
	
	public void remove(Transaction transaction) {
		List<AssociatedEventType> associations = getAssociatedEvents(transaction);
		for (AssociatedEventType associatedEventType : associations) {
			associatedEventTypeDeleteHandler.setAssociatedEventType(associatedEventType).remove(transaction);
		}
	}

	private List<AssociatedEventType> getAssociatedEvents(Transaction transaction) {
		List<AssociatedEventType> associations = associatedEventTypeLoader.setEventType(eventType).load(transaction);
		return associations;
	}

	public AssociatedEventTypeDeleteSummary summary(Transaction transaction) {
		AssociatedEventTypeDeleteSummary summary = new AssociatedEventTypeDeleteSummary();
		summary.setRemoveFromAssetTypes(new Long(associatedEventTypeLoader.setEventType(eventType).load(transaction).size()));
		
		List<AssociatedEventType> associations = getAssociatedEvents(transaction);
		for (AssociatedEventType associatedEventType : associations) {
			AssociatedEventTypeDeleteSummary singleAITSummary = associatedEventTypeDeleteHandler.setAssociatedEventType(associatedEventType).summary(transaction);
			summary.addToDeleteEventFrequencies(singleAITSummary.getDeleteEventFrequencies());
			summary.addToDeleteNonCompletedEvent(singleAITSummary.getDeleteNonCompletedEvent());
		}
		
		return summary;
	}
	
	public AssociatedEventTypeListDeleteHandler setEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}

}
