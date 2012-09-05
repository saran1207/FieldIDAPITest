package com.n4systems.model.builders;

import com.n4systems.model.*;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

import java.util.*;

import static com.n4systems.model.builders.AssetBuilder.anAsset;
import static com.n4systems.model.builders.EventTypeBuilder.anEventType;
import static com.n4systems.model.builders.OrgBuilder.aPrimaryOrg;
import static com.n4systems.model.builders.TenantBuilder.aTenant;
import static com.n4systems.model.builders.UserBuilder.aUser;

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
    private final AssetStatus assetStatus;
    private final Set<CriteriaResult> results;
    private final Date nextDate;
    private final Event.EventState eventState;
    private final EventStatus eventStatus;

    public static EventBuilder anEvent() {
        return anEvent(anEventType());
    }
    
    public static EventBuilder aFailedEvent() {
        return aFailedEvent(anEventType());
    }

    public static EventBuilder anOpenEvent() {
        return anOpenEvent(anEventType());
    }

    public static EventBuilder anEvent(EventTypeBuilder eventTypeBuilder) {
		return new EventBuilder(eventTypeBuilder.build(), anAsset().build(), new ArrayList<SubEvent>(), new Date(), new ArrayList<FileAttachment>(), true, null, aPrimaryOrg().build(), aUser().build(), aTenant().build(), Status.PASS, null, new HashSet<CriteriaResult>(), Event.EventState.COMPLETED, null, null);
	}

    public static EventBuilder anOpenEvent(EventTypeBuilder eventTypeBuilder) {
        return new EventBuilder(eventTypeBuilder.build(), anAsset().build(), new ArrayList<SubEvent>(), new Date(), new ArrayList<FileAttachment>(), true, null, aPrimaryOrg().build(), aUser().build(), aTenant().build(), Status.PASS, null, new HashSet<CriteriaResult>(), Event.EventState.OPEN, null, null);
    }

    public static EventBuilder aFailedEvent(EventTypeBuilder eventTypeBuilder) {
		return new EventBuilder(eventTypeBuilder.build(), anAsset().build(), new ArrayList<SubEvent>(), new Date(), new ArrayList<FileAttachment>(), true, null, aPrimaryOrg().build(), aUser().build(), aTenant().build(), Status.FAIL, null, new HashSet<CriteriaResult>(), Event.EventState.COMPLETED, null, null);
	}

	protected EventBuilder(EventType type, Asset asset, List<SubEvent> subEvents, Date datePerformed, List<FileAttachment> attachements, boolean printable, AssignedToUpdate assignedTo, BaseOrg owner, User performedBy, Tenant tenant, Status status, AssetStatus assetStatus, Set<CriteriaResult> results, Event.EventState eventState, Date nextDate, EventStatus eventStatus) {
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
        this.status=status;
        this.assetStatus=assetStatus;
        this.results = results;
        this.eventState = eventState;
        this.nextDate = nextDate;
        this.eventStatus = eventStatus;
	}
	
	public EventBuilder ofType(EventType type) {
		return makeBuilder(new EventBuilder(type, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
	}
	
	public EventBuilder on(Asset asset) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
	}
	
	public EventBuilder withSubEvents(List<SubEvent> subEvents) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
	}

	public EventBuilder performedOn(Date datePerformed) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
	}
	
	public EventBuilder withAttachment(List<FileAttachment> attachments) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
	}

	public EventBuilder withNoAssignedToUpdate() {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, null, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
	}

	public EventBuilder withAssignedToUpdate(AssignedToUpdate assignToUpdate) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignToUpdate, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
	}
	
	public EventBuilder withOwner(BaseOrg owner) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
	}
	
	public EventBuilder withPerformedBy(User performedBy) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
	}
	
	public EventBuilder withTenant(Tenant tenant) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
	}
	
	public EventBuilder withResult(Status status){
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
	}

	public EventBuilder withAssetStatus(AssetStatus assetStatus){
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
	}

	public EventBuilder withCriteriaResults(CriteriaResult... results){
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, status, assetStatus, setOf(results), eventState, nextDate, eventStatus));
	}

    public EventBuilder scheduledFor(Date nextDate){
        return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
    }

    public EventBuilder withEventStatus(EventStatus eventStatus) {
        return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, status, assetStatus, results, eventState, nextDate, eventStatus));
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
        event.setStatus(status);
        event.setAssetStatus(assetStatus);
        event.setCriteriaResults(results);
        event.setEventState(eventState);
        event.setNextDate(nextDate);
        event.setEventStatus(eventStatus);
        
		return event;
	}

}
