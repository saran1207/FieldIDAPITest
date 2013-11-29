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

public class EventBuilder extends BaseBuilder<ThingEvent> {

	private final ThingEventType eventType;
	private final Asset asset;
	private final List<SubEvent> subEvents;
	private final List<FileAttachment> attachments;
	private final boolean printable;
	
	private final Date datePerformed;
	private final AssignedToUpdate assignedTo;
	
	private final BaseOrg owner;
	private final User performedBy;
	private final Tenant tenant;
	private final EventResult eventResult;
    private final AssetStatus assetStatus;
    private final Set<CriteriaResult> results;
    private final Date dueDate;
    private final WorkflowState workflowState;
    private final EventStatus eventStatus;

    public static EventBuilder anEvent() {
        return anEvent(anEventType());
    }

    public static EventBuilder aFailedEvent() {
        return aFailedEvent(anEventType());
    }

    public static EventBuilder aClosedEvent() {
        return aClosedEvent(anEventType());
    }

    public static EventBuilder anOpenEvent() {
        return anOpenEvent(anEventType());
    }

    public static EventBuilder anEvent(EventTypeBuilder eventTypeBuilder) {
		return new EventBuilder(eventTypeBuilder.build(), anAsset().build(), new ArrayList<SubEvent>(), new Date(), new ArrayList<FileAttachment>(), true, null, aPrimaryOrg().build(), aUser().build(), aTenant().build(), EventResult.PASS, null, new HashSet<CriteriaResult>(), WorkflowState.COMPLETED, null, null);
	}

    public static EventBuilder anOpenEvent(EventTypeBuilder eventTypeBuilder) {
        return new EventBuilder(eventTypeBuilder.build(), anAsset().build(), new ArrayList<SubEvent>(), new Date(), new ArrayList<FileAttachment>(), true, null, aPrimaryOrg().build(), aUser().build(), aTenant().build(), EventResult.PASS, null, new HashSet<CriteriaResult>(), WorkflowState.OPEN, null, null);
    }

    public static EventBuilder aFailedEvent(EventTypeBuilder eventTypeBuilder) {
        return new EventBuilder(eventTypeBuilder.build(), anAsset().build(), new ArrayList<SubEvent>(), new Date(), new ArrayList<FileAttachment>(), true, null, aPrimaryOrg().build(), aUser().build(), aTenant().build(), EventResult.FAIL, null, new HashSet<CriteriaResult>(), WorkflowState.COMPLETED, null, null);
    }

    public static EventBuilder aClosedEvent(EventTypeBuilder eventTypeBuilder) {
        return new EventBuilder(eventTypeBuilder.build(), anAsset().build(), new ArrayList<SubEvent>(), new Date(), new ArrayList<FileAttachment>(), true, null, aPrimaryOrg().build(), aUser().build(), aTenant().build(), EventResult.FAIL, null, new HashSet<CriteriaResult>(), WorkflowState.CLOSED, null, null);
    }

	protected EventBuilder(ThingEventType type, Asset asset, List<SubEvent> subEvents, Date datePerformed, List<FileAttachment> attachements, boolean printable, AssignedToUpdate assignedTo, BaseOrg owner, User performedBy, Tenant tenant, EventResult eventResult, AssetStatus assetStatus, Set<CriteriaResult> results, WorkflowState workflowState, Date dueDate, EventStatus eventStatus) {
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
        this.eventResult = eventResult;
        this.assetStatus=assetStatus;
        this.results = results;
        this.workflowState = workflowState;
        this.dueDate = dueDate;
        this.eventStatus = eventStatus;
	}

    public EventBuilder withWorkflowState(WorkflowState state) {
        return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, results, state, dueDate, eventStatus));
    }

    public EventBuilder ofType(ThingEventType type) {
        return makeBuilder(new EventBuilder(type, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, dueDate, eventStatus));
    }
	
	public EventBuilder on(Asset asset) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, dueDate, eventStatus));
	}
	
	public EventBuilder withSubEvents(List<SubEvent> subEvents) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, dueDate, eventStatus));
	}

	public EventBuilder performedOn(Date datePerformed) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, dueDate, eventStatus));
	}
	
	public EventBuilder withAttachment(List<FileAttachment> attachments) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, dueDate, eventStatus));
	}

	public EventBuilder withNoAssignedToUpdate() {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, null, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, dueDate, eventStatus));
	}

	public EventBuilder withAssignedToUpdate(AssignedToUpdate assignToUpdate) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignToUpdate, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, dueDate, eventStatus));
	}
	
	public EventBuilder withOwner(BaseOrg owner) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, dueDate, eventStatus));
	}
	
	public EventBuilder withPerformedBy(User performedBy) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, dueDate, eventStatus));
	}
	
	public EventBuilder withTenant(Tenant tenant) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, dueDate, eventStatus));
	}
	
	public EventBuilder withResult(EventResult eventResult){
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, dueDate, eventStatus));
	}

	public EventBuilder withAssetStatus(AssetStatus assetStatus){
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, dueDate, eventStatus));
	}

	public EventBuilder withCriteriaResults(CriteriaResult... results){
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, setOf(results), workflowState, dueDate, eventStatus));
	}

    public EventBuilder scheduledFor(Date nextDate){
        return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, nextDate, eventStatus));
    }

    public EventBuilder withEventStatus(EventStatus eventStatus) {
        return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, eventResult, assetStatus, results, workflowState, dueDate, eventStatus));
    }

    @Override
	public ThingEvent createObject() {
		ThingEvent event = new ThingEvent();
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
        event.setEventResult(eventResult);
        event.setAssetStatus(assetStatus);
        event.setCriteriaResults(results);
        event.setWorkflowState(workflowState);
        event.setDueDate(dueDate);
        event.setEventStatus(eventStatus);
        
		return event;
	}

}
