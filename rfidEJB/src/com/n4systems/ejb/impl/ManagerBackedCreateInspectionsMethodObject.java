package com.n4systems.ejb.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.parameters.CreateInspectionParameterBuilder;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubProduct;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;
import com.n4systems.model.Tenant;
import com.n4systems.util.TransactionSupervisor;

public class ManagerBackedCreateInspectionsMethodObject implements CreateInspectionsMethodObject {
	private final PersistenceManager persistenceManager;
	private final InspectionSaver inspectionSaver;


	public ManagerBackedCreateInspectionsMethodObject(PersistenceManager persistenceManager, InspectionSaver inspectionSaver) {
		super();
		this.persistenceManager = persistenceManager;
		this.inspectionSaver = inspectionSaver;
	}

	public List<Inspection> createInspections(String transactionGUID, List<Inspection> inspections, Map<Inspection, Date> nextInspectionDates) throws ProcessingProofTestException, FileAttachmentException, TransactionAlreadyProcessedException, UnknownSubProduct {
		List<Inspection> savedInspections = new ArrayList<Inspection>();
		
		/*
		 *  XXX - Here we pull the Product off the first inspection.  We then re-attach the product back into persistence managed scope.
		 *  This is done so that the infoOptions can be indexed after the Product is updated (otherwise you get a lazy load on infoOptions).
		 *  Since all inspections passed in here are for the same product, we cannot re-attach in the loop since only one entity type with the same id
		 *  can exist in managed scope.  In loop we then set the now managed entity back onto the inspection so they all point to the same
		 *  instance.  This is not optimal and a refactor is in order to avoid this strange case. 
		 */
		Product managedProduct = inspections.iterator().next().getProduct();
		persistenceManager.reattach(managedProduct);
		
		InspectionGroup createdInspectionGroup = null;
		Tenant tenant = null;
		Inspection savedInspection = null;
		for (Inspection inspection : inspections) {
			if (tenant == null) {
				tenant = inspection.getTenant();
			}
			if (createdInspectionGroup != null && inspection.getGroup() == null) {
				inspection.setGroup(createdInspectionGroup);
			}

			// set the managed product back onto the inspection.  See note above.
			inspection.setProduct(managedProduct);
			
			// Pull the attachments off the inspection and send them in seperately so that they get processed properly
			List<FileAttachment> fileAttachments = new ArrayList<FileAttachment>();
			fileAttachments.addAll(inspection.getAttachments());
			inspection.setAttachments(new ArrayList<FileAttachment>());
			
			// Pull off the sub inspection attachments and put them in a map for later use
			Map<Product, List<FileAttachment>> subInspectionAttachments = new HashMap<Product, List<FileAttachment>>();
			for (SubInspection subInspection : inspection.getSubInspections()) {
				subInspectionAttachments.put(subInspection.getProduct(), subInspection.getAttachments());
				subInspection.setAttachments(new ArrayList<FileAttachment>());
			}
			
			savedInspection = inspectionSaver.createInspection(new CreateInspectionParameterBuilder(inspection, inspection.getModifiedBy().getId())
					.withANextInspectionDate(nextInspectionDates.get(inspection)).withUploadedImages(fileAttachments).build());
			
			// handle the subinspection attachments
			SubInspection subInspection = null;
			for (int i =0; i < inspection.getSubInspections().size(); i++) {
				subInspection = inspection.getSubInspections().get(i);
				savedInspection = inspectionSaver.attachFilesToSubInspection(savedInspection, subInspection, new ArrayList<FileAttachment>(subInspectionAttachments.get(subInspection.getProduct())));
			}			

			// If the inspection didn't have an inspection group before saving,
			// and we havn't created an inspection group yet
			// hang on to the now created inspection group to apply to other
			// inspections
			if (createdInspectionGroup == null && inspection.getGroup() != null) {
				createdInspectionGroup = savedInspection.getGroup();
			}

			savedInspections.add(savedInspection);
		}

		TransactionSupervisor transaction = new TransactionSupervisor(persistenceManager);
		transaction.completeInspectionTransaction(transactionGUID, tenant);

		return savedInspections;
	}
}
