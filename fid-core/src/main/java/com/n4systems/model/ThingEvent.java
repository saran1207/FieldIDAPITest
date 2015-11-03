package com.n4systems.model;

import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.utils.AssetEvent;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="thing_events")
@PrimaryKeyJoinColumn(name="id", referencedColumnName="event_id")
public class ThingEvent extends Event<ThingEventType,ThingEvent,Asset> implements AssetEvent, NetworkEntity<ThingEvent> {

    public static final SecurityDefiner createSecurityDefiner() {
        return new SecurityDefiner("tenant.id", "asset.owner", null, "state", true);
    }

    @OneToOne( mappedBy="thingEvent", targetEntity = ThingEventProofTest.class, cascade = CascadeType.ALL)
    private ThingEventProofTest proofTestInfo;

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
    @AllowSafetyNetworkAccess
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
        //TODO arezafar: do I need to copy ThingEventProofTests?
        return this;
    }

    @AllowSafetyNetworkAccess
    public ThingEventProofTest getProofTestInfo() {
        return proofTestInfo;
    }

    public void setProofTestInfo(ThingEventProofTest thingEventProofTest) {
        proofTestInfo = thingEventProofTest;
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

    @AllowSafetyNetworkAccess
    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    @AllowSafetyNetworkAccess
    public AssetStatus getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(AssetStatus assetStatus) {
        this.assetStatus = assetStatus;
    }

    @AllowSafetyNetworkAccess
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

    @AllowSafetyNetworkAccess
    public ThingEventType getThingType() {
        return (ThingEventType) getType();
    }

    @Override
    @AllowSafetyNetworkAccess
    public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
        return SecurityLevel.calculateSecurityLevel(fromOrg, getOwner());
    }

    @AllowSafetyNetworkAccess
    public RecurringAssetTypeEvent getRecurringEvent() {
        return recurringEvent;
    }

    public void setRecurringEvent(RecurringAssetTypeEvent recurringEvent) {
        this.recurringEvent = recurringEvent;
    }
}
