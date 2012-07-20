package com.n4systems.model;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.ArchivableEntityWithOwner;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.*;

@Entity
@Table(name = "recurring_asset_type_events")
@PrimaryKeyJoinColumn(name="id")
public class RecurringAssetTypeEvent extends ArchivableEntityWithOwner implements Saveable, SecurityEnhanced<RecurringAssetTypeEvent>, Cloneable {

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "asset_type_id", nullable = true)
    private AssetType assetType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventType eventType;

    @Embedded
    private Recurrence recurrence;


    public RecurringAssetTypeEvent() {
        this(null, null, new Recurrence());
    }

    public RecurringAssetTypeEvent(AssetType assetType) {
        this(assetType, null, new Recurrence());
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

    @Override
    public String toString() {
        return "RecurringAssetTypeEvent{" +
                "assetType=" + assetType.getName() +
                ", eventType=" + eventType.getName() +
                ", recurrence=" + recurrence +
                '}';
    }
}
