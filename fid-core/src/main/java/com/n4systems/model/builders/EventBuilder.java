package com.n4systems.model.builders;

import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static com.n4systems.model.builders.AssetBuilder.*;
import static com.n4systems.model.builders.OrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static com.n4systems.model.builders.TenantBuilder.*; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Asset;
import com.n4systems.model.SubEvent;
import com.n4systems.model.Tenant;
import com.n4systems.model.inspection.AssignedToUpdate;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

public class EventBuilder extends BaseBuilder<Event> {

	private final InspectionType inspectionType;
	private final Asset asset;
	private final List<SubEvent> subEvents;
	private final List<FileAttachment> attachments;
	private final boolean printable;
	
	private final Date datePerformed;
	private final AssignedToUpdate assignedTo;
	
	private final BaseOrg owner;
	private final User performedBy;
	private final Tenant tenant;
		
	public static EventBuilder anEvent() {
		return new EventBuilder(anInspectionType().build(), anAsset().build(), new ArrayList<SubEvent>(), new Date(), new ArrayList<FileAttachment>(), true, null, aPrimaryOrg().build(), aUser().build(), aTenant().build());
	}
	
	private EventBuilder(InspectionType type, Asset asset, List<SubEvent> subEvents, Date datePerformed, List<FileAttachment> attachements, boolean printable, AssignedToUpdate assignedTo, BaseOrg owner, User performedBy, Tenant tenant) {
		this.inspectionType = type;
		this.asset = asset;
		this.subEvents = subEvents;
		this.attachments = attachements;
		this.datePerformed = datePerformed;
		this.printable = printable;
		this.assignedTo = assignedTo;
		this.owner = owner;
		this.performedBy = performedBy;
		this.tenant = tenant;
	}
	
	public EventBuilder ofType(InspectionType type) {
		return new EventBuilder(type, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}
	
	public EventBuilder on(Asset asset) {
		return new EventBuilder(inspectionType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}
	
	public EventBuilder withSubInspections(List<SubEvent> subEvents) {
		return new EventBuilder(inspectionType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}

	public EventBuilder performedOn(Date datePerformed) {
		return new EventBuilder(inspectionType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}
	
	public EventBuilder withAttachment(List<FileAttachment> attachments) {
		return new EventBuilder(inspectionType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}

	public EventBuilder withNoAssignedToUpdate() {
		return new EventBuilder(inspectionType, asset, subEvents, datePerformed, attachments, printable, null, owner, performedBy, tenant);
	}

	public EventBuilder withAssignedToUpdate(AssignedToUpdate assignToUpdate) {
		return new EventBuilder(inspectionType, asset, subEvents, datePerformed, attachments, printable, assignToUpdate, owner, performedBy, tenant);
	}
	
	public EventBuilder withOwner(BaseOrg owner) {
		return new EventBuilder(inspectionType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}
	
	public EventBuilder withPerformedBy(User performedBy) {
		return new EventBuilder(inspectionType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}
	
	public EventBuilder withTenant(Tenant tenant) {
		return new EventBuilder(inspectionType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant);
	}

	@Override
	public Event createObject() {
		Event event = new Event();
		event.setId(id);
		event.setType(inspectionType);
		event.setAsset(asset);
		event.setSubEvents(subEvents);
		event.setDate(datePerformed);
		event.setAttachments(attachments);
		event.setPrintable(printable);
		event.setAssignedTo(assignedTo);
		event.setOwner(owner);
		event.setPerformedBy(performedBy);
		event.setTenant(tenant);
		return event;
	}

}
