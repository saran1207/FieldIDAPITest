package com.n4systems.model.builders;

import static com.n4systems.model.builders.AssetBuilder.*;
import static com.n4systems.model.builders.EventGroupBuilder.*;
import static com.n4systems.model.builders.EventTypeBuilder.*;
import static com.n4systems.model.builders.OrgBuilder.*;
import static com.n4systems.model.builders.TenantBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.*;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

public class EventBuilder extends BaseBuilder<Event> {

	private final EventType eventType;
	private final Asset asset;
	private final List<SubEvent> subEvents;
	private final List<FileAttachment> attachments;
	private final boolean printable;
	
	private final Date datePerformed;
	private final AssignedToUpdate assignedTo;
	
	private final BaseOrg owner;
	private final User performedBy;
	private final Tenant tenant;
	private final Status status;
    private final EventGroup group;
    private final AssetStatus assetStatus;
    private final Set<CriteriaResult> results;

    public static EventBuilder anEvent() {
        return anEvent(anEventType(), anEventGroup());
    }
    
    public static EventBuilder aFailedEvent() {
        return aFailedEvent(anEventType(), anEventGroup());
    }

	public static EventBuilder anEvent(EventTypeBuilder eventTypeBuilder, EventGroupBuilder eventGroupBuilder) {
		return new EventBuilder(eventTypeBuilder.build(), anAsset().build(), new ArrayList<SubEvent>(), new Date(), new ArrayList<FileAttachment>(), true, null, aPrimaryOrg().build(), aUser().build(), aTenant().build(), eventGroupBuilder.build(), Status.PASS, null, new HashSet<CriteriaResult>());
	}
	
	public static EventBuilder aFailedEvent(EventTypeBuilder eventTypeBuilder, EventGroupBuilder eventGroupBuilder) {
		return new EventBuilder(eventTypeBuilder.build(), anAsset().build(), new ArrayList<SubEvent>(), new Date(), new ArrayList<FileAttachment>(), true, null, aPrimaryOrg().build(), aUser().build(), aTenant().build(), eventGroupBuilder.build(), Status.FAIL, null, new HashSet<CriteriaResult>());
	}

	protected EventBuilder(EventType type, Asset asset, List<SubEvent> subEvents, Date datePerformed, List<FileAttachment> attachements, boolean printable, AssignedToUpdate assignedTo, BaseOrg owner, User performedBy, Tenant tenant, EventGroup group, Status status, AssetStatus assetStatus, Set<CriteriaResult> results) {
		this.eventType = type;
		this.asset = asset;
		this.subEvents = subEvents;
		this.attachments = attachements;
		this.datePerformed = datePerformed;
		this.printable = printable;
		this.assignedTo = assignedTo;
		this.owner = owner;
		this.performedBy = performedBy;
		this.tenant = tenant;
        this.group = group;
        this.status=status;
        this.assetStatus=assetStatus;
        this.results = results;
	}
	
	public EventBuilder ofType(EventType type) {
		return makeBuilder(new EventBuilder(type, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus, results));
	}
	
	public EventBuilder on(Asset asset) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus, results));
	}
	
	public EventBuilder withSubEvents(List<SubEvent> subEvents) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus, results));
	}

	public EventBuilder performedOn(Date datePerformed) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus, results));
	}
	
	public EventBuilder withAttachment(List<FileAttachment> attachments) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus, results));
	}

	public EventBuilder withNoAssignedToUpdate() {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, null, owner, performedBy, tenant, group, status, assetStatus, results));
	}

	public EventBuilder withAssignedToUpdate(AssignedToUpdate assignToUpdate) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignToUpdate, owner, performedBy, tenant, group, status, assetStatus, results));
	}
	
	public EventBuilder withOwner(BaseOrg owner) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus, results));
	}
	
	public EventBuilder withPerformedBy(User performedBy) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus, results));
	}
	
	public EventBuilder withTenant(Tenant tenant) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus, results));
	}

	public EventBuilder withGroup(EventGroup group) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus, results));
	}
	
	public EventBuilder withResult(Status status){
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus, results));
	}

	public EventBuilder withAssetStatus(AssetStatus assetStatus){
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus, results));
	}

	public EventBuilder withCriteriaResults(CriteriaResult... results){
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus, setOf(results)));
	}

	@Override
	public Event createObject() {
		Event event = new Event();
		event.setId(getId());
		event.setType(eventType);
		event.setAsset(asset);
		event.setSubEvents(subEvents);
		event.setDate(datePerformed);
		event.setAttachments(attachments);
		event.setPrintable(printable);
		event.setAssignedTo(assignedTo);
		event.setOwner(owner);
		event.setPerformedBy(performedBy);
		event.setTenant(tenant);
        event.setGroup(group);
        event.setStatus(status);
        event.setAssetStatus(assetStatus);
        event.setCriteriaResults(results);
        
		return event;
	}

}
