package com.n4systems.ejb.impl;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.parameters.CreateEventParameterBuilder;
import com.n4systems.exceptions.*;
import com.n4systems.model.*;
import com.n4systems.util.TransactionSupervisor;
import org.apache.log4j.Logger;

import java.util.*;

public class ManagerBackedCreateEventsMethodObject implements CreateEventsMethodObject {

	private static Logger logger = Logger.getLogger(ManagerBackedCreateEventsMethodObject.class);

	private final PersistenceManager persistenceManager;
	private final EventSaver eventSaver;


	public ManagerBackedCreateEventsMethodObject(PersistenceManager persistenceManager, EventSaver eventSaver) {
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

		Tenant tenant = null;
		Event savedEvent = null;
		for (Event event : events) {
			if (tenant == null) {
				tenant = event.getTenant();
			}
			// set the managed asset back onto the event.  See note above.
			event.setAsset(managedAsset);
			
			// Pull the attachments off the event and send them in separately so that they get processed properly
			List<FileAttachment> fileAttachments = new ArrayList<FileAttachment>();
			fileAttachments.addAll(event.getAttachments());
			event.setAttachments(new ArrayList<FileAttachment>());
			
			// Pull off the sub event attachments and put them in a map for later use
			Map<Asset, List<FileAttachment>> subEventAttachments = new HashMap<Asset, List<FileAttachment>>();
			for (SubEvent subEvent : event.getSubEvents()) {
				subEventAttachments.put(subEvent.getAsset(), subEvent.getAttachments());
				subEvent.setAttachments(new ArrayList<FileAttachment>());
			}

            final CreateEventParameter createEventParameter = new CreateEventParameterBuilder(event, event.getModifiedBy().getId())
                    .withUploadedImages(fileAttachments).build();

            try {
                eventSaver.createEvent(createEventParameter);
            } catch (InvalidScheduleStateException e) {
                logger.warn("the state of the schedule is not valid to be completed.");
            }
            
            savedEvent = createEventParameter.event;

			// handle the sub event attachments
            for (SubEvent subEvent : new ArrayList<SubEvent>(event.getSubEvents())) {
                savedEvent = eventSaver.attachFilesToSubEvent(savedEvent, subEvent, new ArrayList<FileAttachment>(subEventAttachments.get(subEvent.getAsset())));
            }

			savedEvents.add(savedEvent);
		}

		TransactionSupervisor transaction = new TransactionSupervisor(persistenceManager);
		transaction.completeEventTransaction(transactionGUID, tenant);

		return savedEvents;
	}
}
