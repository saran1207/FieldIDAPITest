package com.n4systems.model;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.EntityWithOwner;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.*;

@Entity
@Table(name = "recurring_asset_type_events")
@PrimaryKeyJoinColumn(name="id")
public class RecurringAssetTypeEvent extends EntityWithOwner implements Saveable, SecurityEnhanced<RecurringAssetTypeEvent> {

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "asset_type_id", nullable = true)
    private AssetType assetType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventType eventType;

    @Column(name = "recurrence", nullable=false)
    @Enumerated(EnumType.STRING)
    private Recurrence recurrence;

    public RecurringAssetTypeEvent() {
    }

    public RecurringAssetTypeEvent(AssetType assetType, EventType eventType, Recurrence recurrence) {
        this.assetType = assetType;
        this.eventType = eventType;
        this.recurrence = recurrence;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    @Override
    public RecurringAssetTypeEvent enhance(SecurityLevel level) {
        RecurringAssetTypeEvent enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
        enhanced.setAssetType(enhance(assetType, level));
        enhanced.setEventType(enhance(eventType, level));
        return enhanced;
    }
}
