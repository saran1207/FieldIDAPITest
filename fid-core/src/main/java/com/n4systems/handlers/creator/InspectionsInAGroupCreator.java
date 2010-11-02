package com.n4systems.handlers.creator;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.Event;

public interface InspectionsInAGroupCreator {

	public List<Event> create(String transactionGUID, List<Event> events, Map<Event, Date> nextInspectionDates) throws TransactionAlreadyProcessedException,
			ProcessingProofTestException, FileAttachmentException, UnknownSubAsset;

}