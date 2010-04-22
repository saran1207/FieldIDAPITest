package com.n4systems.handlers.creator;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubProduct;
import com.n4systems.model.Inspection;

public interface InspectionsInAGroupCreator {

	public List<Inspection> create(String transactionGUID, List<Inspection> inspections, Map<Inspection, Date> nextInspectionDates) throws TransactionAlreadyProcessedException,
			ProcessingProofTestException, FileAttachmentException, UnknownSubProduct;

}