package com.n4systems.model;

import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.utils.AssetEvent;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="thing_events")
@PrimaryKeyJoinColumn(name="id")
public class ThingEvent extends Event<ThingEventType,ThingEvent,Asset> implements AssetEvent, NetworkEntity<ThingEvent> {

    public static final SecurityDefiner createSecurityDefiner() {
        return new SecurityDefiner("tenant.id", "asset.owner", null, "state", true);
    }

    /* does this cause "failed to lazily initialize a collection of role"? YES!
    TODO figure out WHY above happens*/
    @OneToMany(fetch=FetchType.EAGER, mappedBy="thingEvent")
    //@JoinColumn(name="event_id")  Associations marked as mappedBy must not define database mappings like @JoinTable or @JoinColumn
    private Set<ThingEventProofTest> thingEventProofTests = new HashSet<ThingEventProofTest>();

    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name="asset_id")
    private Asset asset;

    @ManyToOne(optional = true)
    @JoinColumn(name="asset_status_id")
    private AssetStatus assetStatus;

    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    @JoinColumn(name="owner_id", nullable = false)
    private BaseOrg owner;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name="recurring_event_id")
    private RecurringAssetTypeEvent recurringEvent;

    @Override
    public Asset getTarget() {
        return getAsset();
    }

    @Override
    public void setTarget(Asset target) {
        setAsset(target);
    }

    public ThingEvent copyDataFrom(ThingEvent event) {
        SecurityEnhanced x = this;
        setAsset(event.getAsset());
        setType(event.getType());
        setTenant(event.getTenant());
        setCreated(event.getCreated());
        setCreatedBy(event.getCreatedBy());
        setModifiedBy(event.getModifiedBy());
        setModified(event.getModified());

        return this;
    }

    @AllowSafetyNetworkAccess
    public Set<ThingEventProofTest> getThingEventProofTests() {
        return thingEventProofTests;
    }

    public ThingEventProofTest getProofTestInfo() {
        Iterator<ThingEventProofTest> itr = thingEventProofTests.iterator();
        if(itr.hasNext()){
            return itr.next();
        }
        else {
            return null;
        }
    }

    public void setThingEventProofTests(Set<ThingEventProofTest> thingEventProofTests) {
        this.thingEventProofTests = thingEventProofTests;
    }

    @Override
    public ThingEvent enhance(SecurityLevel level) {
        ThingEvent enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
        enhanced.setBook(enhance(getBook(), level));
        enhanced.setPerformedBy(enhance(getPerformedBy(), level));
        enhanced.setType(enhance(getType(), level));
        enhanced.setAsset(enhance(getAsset(), level));
        enhanced.setOwner(enhance(getOwner(), level));

        List<SubEvent> enhancedSubEvents = new ArrayList<SubEvent>();
        for (SubEvent subEvent : getSubEvents()) {
            enhancedSubEvents.add(subEvent.enhance(level));
        }
        enhanced.setSubEvents(enhancedSubEvents);

        return enhanced;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public AssetStatus getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(AssetStatus assetStatus) {
        this.assetStatus = assetStatus;
    }

    public BaseOrg getOwner() {
        return owner;
    }

    public void setOwner(BaseOrg owner) {
        this.owner = owner;
    }

    @Override
    protected void copyDataIntoResultingAction(AbstractEvent<?,?> event) {
        ThingEvent action = (ThingEvent) event;
        action.setAsset(getAsset());
        action.setOwner(getOwner());
    }

    public ThingEventType getThingType() {
        return (ThingEventType) getType();
    }

    @Override
    @AllowSafetyNetworkAccess
    public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
        return SecurityLevel.calculateSecurityLevel(fromOrg, getOwner());
    }

    public RecurringAssetTypeEvent getRecurringEvent() {
        return recurringEvent;
    }

    public void setRecurringEvent(RecurringAssetTypeEvent recurringEvent) {
        this.recurringEvent = recurringEvent;
    }
}
