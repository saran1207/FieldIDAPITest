package com.n4systems.model.builders;

import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static com.n4systems.model.builders.ProductBuilder.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;
import com.n4systems.model.inspection.AssignedToUpdate;

public class InspectionBuilder extends BaseBuilder<Inspection> {

	private final InspectionType inspectionType;
	private final Product product;
	private final List<SubInspection> subInspections;
	private final List<FileAttachment> attachments;
	private final boolean printable;
	
	private final Date datePerformed;
	private final AssignedToUpdate assignedTo;
	
		
	public static InspectionBuilder anInspection() {
		return new InspectionBuilder(anInspectionType().build(), aProduct().build(), new ArrayList<SubInspection>(), new Date(), new ArrayList<FileAttachment>(), true, null);
	}
	
	private InspectionBuilder(InspectionType type, Product product, List<SubInspection> subInspections, Date datePerformed, List<FileAttachment> attachements, boolean printable, AssignedToUpdate assignedTo) {
		this.inspectionType = type;
		this.product = product;
		this.subInspections = subInspections;
		this.attachments = attachements;
		this.datePerformed = datePerformed;
		this.printable = printable;
		this.assignedTo = assignedTo;
	}
	
	public InspectionBuilder ofType(InspectionType type) {
		return new InspectionBuilder(type, product, subInspections, datePerformed, attachments, printable, assignedTo);
	}
	
	public InspectionBuilder on(Product product) {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, assignedTo);
	}
	
	public InspectionBuilder withSubInspections(List<SubInspection> subInspections) {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, assignedTo);
	}
	

	public InspectionBuilder performedOn(Date datePerformed) {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, assignedTo);
	}
	
	public InspectionBuilder withAttachment(List<FileAttachment> attachments) {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, assignedTo);
	}
	

	public InspectionBuilder withNoAssignedToUpdate() {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, null);
	}

	public InspectionBuilder withAssignedToUpdate(AssignedToUpdate assignToUpdate) {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, assignToUpdate);
	}
	
	@Override
	public Inspection build() {
		Inspection inspection = new Inspection();
		inspection.setId(id);
		inspection.setType(inspectionType);
		inspection.setProduct(product);
		inspection.setSubInspections(subInspections);
		inspection.setDate(datePerformed);
		inspection.setAttachments(attachments);
		inspection.setPrintable(printable);
		inspection.setAssignedTo(assignedTo);
		return inspection;
	}

	
	

}
