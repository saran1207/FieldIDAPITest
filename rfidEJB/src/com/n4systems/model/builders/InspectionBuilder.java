package com.n4systems.model.builders;

import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static com.n4systems.model.builders.ProductBuilder.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;

public class InspectionBuilder extends BaseBuilder<Inspection> {

	private final InspectionType inspectionType;
	private final Product product;
	private final List<SubInspection> subInspections;
	private final Set<FileAttachment> attachments;
	
	private final Date inspectionDate;
	
		
	public static InspectionBuilder anInspection() {
		return new InspectionBuilder(anInspectionType().build(), aProduct().build(), new ArrayList<SubInspection>(), new Date(), new HashSet<FileAttachment>());
	}
	
	public InspectionBuilder(InspectionType type, Product product, List<SubInspection> subInspections, Date inspectionDate, Set<FileAttachment> attachements) {
		this.inspectionType = type;
		this.product = product;
		this.subInspections = subInspections;
		this.attachments = attachements;
		this.inspectionDate = inspectionDate;
	}
	
	public InspectionBuilder ofType(InspectionType type) {
		return new InspectionBuilder(type, product, subInspections, inspectionDate, attachments);
	}
	
	public InspectionBuilder on(Product product) {
		return new InspectionBuilder(inspectionType, product, subInspections, inspectionDate, attachments);
	}
	
	public InspectionBuilder withSubInspections(List<SubInspection> subInspections) {
		return new InspectionBuilder(inspectionType, product, subInspections, inspectionDate, attachments);
	}
	

	public InspectionBuilder withInspectionDate(Date inspectionDate) {
		return new InspectionBuilder(inspectionType, product, subInspections, inspectionDate, attachments);
	}
	
	public InspectionBuilder withAttachment(Set<FileAttachment> attachments) {
		return new InspectionBuilder(inspectionType, product, subInspections, inspectionDate, attachments);
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
		
		return inspection;
	}
	
	

}
