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

public class InspectionBuilder extends BaseBuilder<Inspection> {

	private final InspectionType inspectionType;
	private final Product product;
	private final List<SubInspection> subInspections;
	private final List<FileAttachment> attachments;
	private final boolean printable;
	
	private final Date inspectionDate;
	
		
	public static InspectionBuilder anInspection() {
		return new InspectionBuilder(anInspectionType().build(), aProduct().build(), new ArrayList<SubInspection>(), new Date(), new ArrayList<FileAttachment>(), true);
	}
	
	private InspectionBuilder(InspectionType type, Product product, List<SubInspection> subInspections, Date inspectionDate, List<FileAttachment> attachements, boolean printable) {
		this.inspectionType = type;
		this.product = product;
		this.subInspections = subInspections;
		this.attachments = attachements;
		this.inspectionDate = inspectionDate;
		this.printable = printable;
	}
	
	public InspectionBuilder ofType(InspectionType type) {
		return new InspectionBuilder(type, product, subInspections, inspectionDate, attachments, printable);
	}
	
	public InspectionBuilder on(Product product) {
		return new InspectionBuilder(inspectionType, product, subInspections, inspectionDate, attachments, printable);
	}
	
	public InspectionBuilder withSubInspections(List<SubInspection> subInspections) {
		return new InspectionBuilder(inspectionType, product, subInspections, inspectionDate, attachments, printable);
	}
	

	public InspectionBuilder withInspectionDate(Date inspectionDate) {
		return new InspectionBuilder(inspectionType, product, subInspections, inspectionDate, attachments, printable);
	}
	
	public InspectionBuilder withAttachment(List<FileAttachment> attachments) {
		return new InspectionBuilder(inspectionType, product, subInspections, inspectionDate, attachments, printable);
	}
	
	@Override
	public Inspection build() {
		Inspection inspection = new Inspection();
		inspection.setId(id);
		inspection.setType(inspectionType);
		inspection.setProduct(product);
		inspection.setSubInspections(subInspections);
		inspection.setDate(inspectionDate);
		inspection.setAttachments(attachments);
		inspection.setPrintable(printable);
		return inspection;
	}
	
	

}
