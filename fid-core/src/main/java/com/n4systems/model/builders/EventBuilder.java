package com.n4systems.model.builders;

import static com.n4systems.model.builders.AssetBuilder.anAsset;
import static com.n4systems.model.builders.EventGroupBuilder.anEventGroup;
import static com.n4systems.model.builders.EventTypeBuilder.anEventType;
import static com.n4systems.model.builders.OrgBuilder.aPrimaryOrg;
import static com.n4systems.model.builders.TenantBuilder.aTenant;
import static com.n4systems.model.builders.UserBuilder.aUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.Event;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventType;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Status;
import com.n4systems.model.SubEvent;
import com.n4systems.model.Tenant;
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

    public static EventBuilder anEvent() {
        return anEvent(anEventType(), anEventGroup());
    }
    
    public static EventBuilder aFailedEvent() {
        return aFailedEvent(anEventType(), anEventGroup());
    }

	public static EventBuilder anEvent(EventTypeBuilder eventTypeBuilder, EventGroupBuilder eventGroupBuilder) {
		return new EventBuilder(eventTypeBuilder.build(), anAsset().build(), new ArrayList<SubEvent>(), new Date(), new ArrayList<FileAttachment>(), true, null, aPrimaryOrg().build(), aUser().build(), aTenant().build(), eventGroupBuilder.build(), Status.PASS, null);
	}
	
	public static EventBuilder aFailedEvent(EventTypeBuilder eventTypeBuilder, EventGroupBuilder eventGroupBuilder) {
		return new EventBuilder(eventTypeBuilder.build(), anAsset().build(), new ArrayList<SubEvent>(), new Date(), new ArrayList<FileAttachment>(), true, null, aPrimaryOrg().build(), aUser().build(), aTenant().build(), eventGroupBuilder.build(), Status.FAIL, null);
	}

	protected EventBuilder(EventType type, Asset asset, List<SubEvent> subEvents, Date datePerformed, List<FileAttachment> attachements, boolean printable, AssignedToUpdate assignedTo, BaseOrg owner, User performedBy, Tenant tenant, EventGroup group, Status status, AssetStatus assetStatus) {
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
	}
	
	public EventBuilder ofType(EventType type) {
		return makeBuilder(new EventBuilder(type, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, Status.PASS, assetStatus));
	}
	
	public EventBuilder on(Asset asset) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, Status.PASS, assetStatus));
	}
	
	public EventBuilder withSubEvents(List<SubEvent> subEvents) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, Status.PASS, assetStatus));
	}

	public EventBuilder performedOn(Date datePerformed) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, Status.PASS, assetStatus));
	}
	
	public EventBuilder withAttachment(List<FileAttachment> attachments) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, Status.PASS, assetStatus));
	}

	public EventBuilder withNoAssignedToUpdate() {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, null, owner, performedBy, tenant, group, Status.PASS, assetStatus));
	}

	public EventBuilder withAssignedToUpdate(AssignedToUpdate assignToUpdate) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignToUpdate, owner, performedBy, tenant, group, Status.PASS, assetStatus));
	}
	
	public EventBuilder withOwner(BaseOrg owner) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, Status.PASS, assetStatus));
	}
	
	public EventBuilder withPerformedBy(User performedBy) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, Status.PASS, assetStatus));
	}
	
	public EventBuilder withTenant(Tenant tenant) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, Status.PASS, assetStatus));
	}

	public EventBuilder withGroup(EventGroup group) {
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, Status.PASS, assetStatus));
	}
	
	public EventBuilder withResult(Status status){
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus));
	}

	public EventBuilder withAssetStatus(AssetStatus assetStatus){
		return makeBuilder(new EventBuilder(eventType, asset, subEvents, datePerformed, attachments, printable, assignedTo, owner, performedBy, tenant, group, status, assetStatus));
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
        
		return event;
	}

}
