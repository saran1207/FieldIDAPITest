package com.n4systems.handlers.remover;

import java.util.List;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.remover.summary.EventFrequencyDeleteSummary;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.assettype.EventFrequencyListLoader;
import com.n4systems.model.inspectiontype.EventFrequencySaver;
import com.n4systems.persistence.Transaction;

public class EventFrequenciesDeleteHandlerImpl implements EventFrequenciesDeleteHandler {
	
	private final EventFrequencyListLoader listLoader;
	private final EventFrequencySaver saver;
	
	private EventType eventType;
	private AssociatedEventType associatedEventType;
	private Transaction transaction;
	
	public EventFrequenciesDeleteHandlerImpl(EventFrequencyListLoader listLoader, EventFrequencySaver saver) {
		this.listLoader = listLoader;
		this.saver = saver;
	}

	public void remove(Transaction transaction) {
		this.transaction = transaction;
		List<AssetTypeSchedule> frequencies = getEventFrequencies();
		deleteFrequencies(frequencies);
	}

	private List<AssetTypeSchedule> getEventFrequencies() {
		
		if (eventType != null) {
			return  getFrequenciesForEventType();
		} else if (associatedEventType != null) {
			return getFrequenciesForAssociatedEventType(associatedEventType);
		}
		
		throw new InvalidArgumentException("you must give the event type or the associated event type.");
	}

	private List<AssetTypeSchedule> getFrequenciesForEventType() {
		return listLoader.setEventTypeId(eventType.getId()).load(transaction);
	}

	private List<AssetTypeSchedule> getFrequenciesForAssociatedEventType(AssociatedEventType entity) {
		return listLoader.setEventTypeId(entity.getEventType().getId()).setAssetTypeId(entity.getAssetType().getId()).load(transaction);
	}
	
	private int deleteFrequencies(List<AssetTypeSchedule> frequencies ) {
		for (AssetTypeSchedule assetTypeSchedule : frequencies) {
			saver.remove(transaction, assetTypeSchedule);
		}
		return frequencies.size();
	}
	
	public EventFrequencyDeleteSummary summary(Transaction transaction) {
		this.transaction = transaction;
		EventFrequencyDeleteSummary eventFrequencyDeleteSummary = new EventFrequencyDeleteSummary();
		eventFrequencyDeleteSummary.setElementsToRemove(getEventFrequencies().size());
		return eventFrequencyDeleteSummary;
	}

	public EventFrequenciesDeleteHandler forAssociatedEventType(AssociatedEventType associatedEventType) {
		this.associatedEventType = associatedEventType;
		return this;
	}

	public EventFrequenciesDeleteHandler forEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}

}
