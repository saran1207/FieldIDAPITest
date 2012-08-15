package com.n4systems.model.dashboard.widget;

import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.orgs.BaseOrg;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_configurations_work")
@PrimaryKeyJoinColumn(name="id")
public class WorkWidgetConfiguration extends WidgetConfiguration {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id", nullable = true)
    private BaseOrg org;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="asset_type_id", nullable=true)
    private AssetType assetType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="event_type_id", nullable=true)
    private EventType eventType;


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

    public BaseOrg getOrg() {
        return org;
    }

    public void setOrg(BaseOrg org) {
        this.org = org;
    }

    @Override
    public WorkWidgetConfiguration copy() {
        WorkWidgetConfiguration copy = (WorkWidgetConfiguration) super.copy();
        copy.setOrg(org);
        copy.setAssetType(assetType);
        copy.setEventType(eventType);
        return copy;
    }

}
