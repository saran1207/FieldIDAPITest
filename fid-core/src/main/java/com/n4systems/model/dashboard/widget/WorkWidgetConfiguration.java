package com.n4systems.model.dashboard.widget;

import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.dashboard.widget.interfaces.ConfigurationForAgenda;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "widget_configurations_work")
@PrimaryKeyJoinColumn(name="id")
public class WorkWidgetConfiguration extends WidgetConfiguration implements ConfigurationForAgenda {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id", nullable = true)
    private BaseOrg org;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="asset_type_id", nullable=true)
    private AssetType assetType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="event_type_id", nullable=true)
    private EventType eventType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable=true)
    private User user;


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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
