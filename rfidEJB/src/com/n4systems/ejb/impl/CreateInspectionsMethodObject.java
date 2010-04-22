package com.n4systems.ejb.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubProduct;
import com.n4systems.model.Inspection;

public interface CreateInspectionsMethodObject {

	public List<Inspection> createInspections(String transactionGUID, List<Inspection> inspections, Map<Inspection, Date> nextInspectionDates) throws ProcessingProofTestException,
			FileAttachmentException, TransactionAlreadyProcessedException, UnknownSubProduct;

}