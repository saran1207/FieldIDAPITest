package com.n4systems.model;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.ArchivableEntityWithOwner;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "recurring_asset_type_events")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RecurringAssetTypeEvent extends ArchivableEntityWithOwner implements Saveable, SecurityEnhanced<RecurringAssetTypeEvent>, Cloneable {

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "asset_type_id", nullable = true)
    private AssetType assetType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "thing_event_type_id", nullable = false)
    private ThingEventType eventType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="recurrence_id")
    private Recurrence recurrence;

    private Boolean ownerAndDown;
    private boolean autoAssign;

    public RecurringAssetTypeEvent() {
        this(null, null, new Recurrence());
    }

    public RecurringAssetTypeEvent(AssetType assetType) {
        this(assetType, null, new Recurrence());
    }

    public RecurringAssetTypeEvent(AssetType assetType, ThingEventType eventType, Recurrence recurrence) {
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

    public ThingEventType getEventType() {
        return eventType;
    }

    public void setEventType(ThingEventType eventType) {
        this.eventType = eventType;
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    public Boolean getOwnerAndDown() {
        return ownerAndDown;
    }

    public void setOwnerAndDown(Boolean ownerAndDown) {
        this.ownerAndDown = ownerAndDown;
    }

    @Override
    public RecurringAssetTypeEvent enhance(SecurityLevel level) {
        RecurringAssetTypeEvent enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
        enhanced.setAssetType(enhance(assetType, level));
        enhanced.setEventType(enhance(eventType, level));
        return enhanced;
    }

    public void setAutoAssign(Boolean autoAssign) {
        this.autoAssign = autoAssign;
    }

    public Boolean getAutoAssign() {
        return autoAssign;
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
