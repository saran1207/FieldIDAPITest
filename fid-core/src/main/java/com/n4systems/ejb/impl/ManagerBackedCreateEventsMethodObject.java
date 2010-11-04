package com.n4systems.ejb.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.parameters.CreateEventParameterBuilder;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.EventGroup;
import com.n4systems.model.SubEvent;
import com.n4systems.model.Tenant;
import com.n4systems.util.TransactionSupervisor;

public class ManagerBackedCreateEventsMethodObject implements CreateEventsMethodObject {
	private final PersistenceManager persistenceManager;
	private final EventSaver eventSaver;


	public ManagerBackedCreateEventsMethodObject(PersistenceManager persistenceManager, EventSaver eventSaver) {
		super();
		this.persistenceManager = persistenceManager;
		this.eventSaver = eventSaver;
	}

	public List<Event> createEvents(String transactionGUID, List<Event> events, Map<Event, Date> nextEventDates) throws ProcessingProofTestException, FileAttachmentException, TransactionAlreadyProcessedException, UnknownSubAsset {
		return createEvents(transactionGUID, events);
	}

	public List<Event> createEvents(String transactionGUID, List<Event> events) throws ProcessingProofTestException, FileAttachmentException, TransactionAlreadyProcessedException, UnknownSubAsset {
		List<Event> savedEvents = new ArrayList<Event>();
		
		/*
		 *  XXX - Here we pull the Asset off the first event.  We then re-attach the asset back into persistence managed scope.
		 *  This is done so that the infoOptions can be indexed after the Asset is updated (otherwise you get a lazy load on infoOptions).
		 *  Since all events passed in here are for the same asset, we cannot re-attach in the loop since only one entity type with the same id
		 *  can exist in managed scope.  In loop we then set the now managed entity back onto the event so they all point to the same
		 *  instance.  This is not optimal and a refactor is in order to avoid this strange case. 
		 */
		Asset managedAsset = events.iterator().next().getAsset();
		persistenceManager.reattach(managedAsset);
		
		EventGroup createdEventGroup = null;
		Tenant tenant = null;
		Event savedEvent = null;
		for (Event event : events) {
			if (tenant == null) {
				tenant = event.getTenant();
			}
			if (createdEventGroup != null && event.getGroup() == null) {
				event.setGroup(createdEventGroup);
			}

			// set the managed asset back onto the event.  See note above.
			event.setAsset(managedAsset);
			
			// Pull the attachments off the event and send them in seperately so that they get processed properly
			List<FileAttachment> fileAttachments = new ArrayList<FileAttachment>();
			fileAttachments.addAll(event.getAttachments());
			event.setAttachments(new ArrayList<FileAttachment>());
			
			// Pull off the sub event attachments and put them in a map for later use
			Map<Asset, List<FileAttachment>> subEventAttachments = new HashMap<Asset, List<FileAttachment>>();
			for (SubEvent subEvent : event.getSubEvents()) {
				subEventAttachments.put(subEvent.getAsset(), subEvent.getAttachments());
				subEvent.setAttachments(new ArrayList<FileAttachment>());
			}
			
			savedEvent = eventSaver.createEvent(new CreateEventParameterBuilder(event, event.getModifiedBy().getId())
					.withUploadedImages(fileAttachments).build());
			
			// handle the subevent attachments
			SubEvent subEvent = null;
			for (int i =0; i < event.getSubEvents().size(); i++) {
				subEvent = event.getSubEvents().get(i);
				savedEvent = eventSaver.attachFilesToSubEvent(savedEvent, subEvent, new ArrayList<FileAttachment>(subEventAttachments.get(subEvent.getAsset())));
			}			

			// If the event didn't have an event group before saving,
			// and we havn't created an event group yet
			// hang on to the now created event group to apply to other
			// events
			if (createdEventGroup == null && event.getGroup() != null) {
				createdEventGroup = savedEvent.getGroup();
			}

			savedEvents.add(savedEvent);
		}

		TransactionSupervisor transaction = new TransactionSupervisor(persistenceManager);
		transaction.completeEventTransaction(transactionGUID, tenant);

		return savedEvents;
	}
}
