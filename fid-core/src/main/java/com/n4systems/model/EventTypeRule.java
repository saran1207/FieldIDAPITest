package com.n4systems.model;

import com.n4systems.fieldid.service.event.EventTypeRulesService;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;

@Entity
@Table(name = "event_type_rules")
public class EventTypeRule extends EntityWithTenant {

    public EventTypeRule() {
    }

    @ManyToOne(targetEntity = EventType.class)
    @JoinColumn(name="eventtype_id")
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(name="event_result", nullable = false)
    private EventResult result;

    @ManyToOne(optional = true)
    @JoinColumn(name="assetstatus_id")
    private AssetStatus assetStatus;

    @Transient
    private Boolean enabled;

    public EventTypeRule(EventType eventType, EventResult result) {
        this.eventType = eventType;
        this.result = result;
        this.enabled = false;
        this.assetStatus = EventTypeRulesService.getNoChangeStatus();
    }

    public EventTypeRule(EventType eventType, EventResult result, AssetStatus assetStatus) {
        this.eventType = eventType;
        this.result = result;
        this.assetStatus = assetStatus;
        this.enabled = true;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventResult getResult() {
        return result;
    }

    public void setResult(EventResult result) {
        this.result = result;
    }

    public AssetStatus getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(AssetStatus assetStatus) {
        this.assetStatus = assetStatus;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
