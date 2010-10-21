package com.n4systems.model.builders;

import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static com.n4systems.model.builders.ProductBuilder.*;
import static com.n4systems.model.builders.OrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static com.n4systems.model.builders.TenantBuilder.*; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;
import com.n4systems.model.Tenant;
import com.n4systems.model.inspection.AssignedToUpdate;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

public class InspectionBuilder extends BaseBuilder<Inspection> {

	private final InspectionType inspectionType;
	private final Product product;
	private final List<SubInspection> subInspections;
	private final List<FileAttachment> attachments;
	private final boolean printable;
	
	private final Date datePerformed;
	private final AssignedToUpdate assignedTo;
	
	private final BaseOrg owner;
	private final User performedBy;
	private final Tenant tenant;
		
	public static InspectionBuilder anInspection() {
		return new InspectionBuilder(anInspectionType().build(), aProduct().build(), new ArrayList<SubInspection>(), 
				new Date(), new ArrayList<FileAttachment>(), true, null, aPrimaryOrg().build(), aUser().build(), aTenant().build());
	}
	
	private InspectionBuilder(InspectionType type, Product product, List<SubInspection> subInspections, Date datePerformed, 
			List<FileAttachment> attachements, boolean printable, AssignedToUpdate assignedTo, BaseOrg owner, User performedBy, Tenant tenant) {
		this.inspectionType = type;
		this.product = product;
		this.subInspections = subInspections;
		this.attachments = attachements;
		this.datePerformed = datePerformed;
		this.printable = printable;
		this.assignedTo = assignedTo;
		this.owner = owner;
		this.performedBy = performedBy;
		this.tenant = tenant;
	}
	
	public InspectionBuilder ofType(InspectionType type) {
		return new InspectionBuilder(type, product, subInspections, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}
	
	public InspectionBuilder on(Product product) {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}
	
	public InspectionBuilder withSubInspections(List<SubInspection> subInspections) {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}

	public InspectionBuilder performedOn(Date datePerformed) {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}
	
	public InspectionBuilder withAttachment(List<FileAttachment> attachments) {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}

	public InspectionBuilder withNoAssignedToUpdate() {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, null, owner, performedBy, tenant);
	}

	public InspectionBuilder withAssignedToUpdate(AssignedToUpdate assignToUpdate) {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, assignToUpdate, owner, performedBy, tenant);
	}
	
	public InspectionBuilder withOwner(BaseOrg owner) {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}
	
	public InspectionBuilder withPerformedBy(User performedBy) {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}
	
	public InspectionBuilder withTenant(Tenant tenant) {
		return new InspectionBuilder(inspectionType, product, subInspections, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}

	@Override
	public Inspection createObject() {
		Inspection inspection = new Inspection();
		inspection.setId(id);
		inspection.setType(inspectionType);
		inspection.setProduct(product);
		inspection.setSubInspections(subInspections);
		inspection.setDate(datePerformed);
		inspection.setAttachments(attachments);
		inspection.setPrintable(printable);
		inspection.setAssignedTo(assignedTo);
		inspection.setOwner(owner);
		inspection.setPerformedBy(performedBy);
		inspection.setTenant(tenant);
		return inspection;
	}

}
