/**
 * 
 */
package com.n4systems.handlers.creator;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.n4systems.ejb.impl.CreateEventsMethodObject;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.Event;

final class NullCreateEventsMethodObject implements CreateEventsMethodObject {
	public List<Event> createEvents(String transactionGUID, List<Event> events, Map<Event, Date> nextEventDates) throws ProcessingProofTestException,
	FileAttachmentException, TransactionAlreadyProcessedException, UnknownSubAsset {
		return createEvents(transactionGUID, events);
	}

	public List<Event> createEvents(String transactionGUID, List<Event> events) throws ProcessingProofTestException,
			FileAttachmentException, TransactionAlreadyProcessedException, UnknownSubAsset {
		return events;
	}
}