package com.n4systems.handlers.creator;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;

public interface EventsInAGroupCreator {

	public List<ThingEvent> create(String transactionGUID, List<ThingEvent> events, Map<ThingEvent, Date> nextEventDates) throws TransactionAlreadyProcessedException,
			ProcessingProofTestException, FileAttachmentException, UnknownSubAsset;

}