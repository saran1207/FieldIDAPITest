package com.n4systems.ejb.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.Event;

public interface CreateEventsMethodObject {

	public List<Event> createInspections(String transactionGUID, List<Event> events, Map<Event, Date> nextInspectionDates) throws ProcessingProofTestException,
	FileAttachmentException, TransactionAlreadyProcessedException, UnknownSubAsset;

	public List<Event> createInspections(String transactionGUID, List<Event> events) throws ProcessingProofTestException,
			FileAttachmentException, TransactionAlreadyProcessedException, UnknownSubAsset;

}