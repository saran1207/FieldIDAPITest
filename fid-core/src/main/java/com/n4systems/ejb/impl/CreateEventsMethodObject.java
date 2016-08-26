package com.n4systems.ejb.impl;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.ThingEvent;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CreateEventsMethodObject {

	public List<ThingEvent> createEvents(String transactionGUID, List<ThingEvent> events, Map<ThingEvent, Date> nextEventDates) throws ProcessingProofTestException,
	FileAttachmentException, TransactionAlreadyProcessedException, UnknownSubAsset;

	public List<ThingEvent> createEvents(String transactionGUID, List<ThingEvent> events) throws ProcessingProofTestException,
			FileAttachmentException, TransactionAlreadyProcessedException, UnknownSubAsset;

}