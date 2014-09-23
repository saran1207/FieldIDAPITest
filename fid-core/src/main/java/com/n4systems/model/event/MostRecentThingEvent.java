package com.n4systems.model.event;

import com.n4systems.model.Asset;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.security.SecurityDefiner;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="most_recent_completed_thing_events")
public class MostRecentThingEvent extends BaseEntity {

    public static final SecurityDefiner createSecurityDefiner() {
        return new SecurityDefiner("event.tenant.id", "asset.owner", null, "event.state", true);
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="event_id")
    private ThingEvent event;

    @ManyToOne(optional = false)
    @JoinColumn(name="asset_id")
    private Asset asset;

    @ManyToOne(optional = false)
    @JoinColumn(name="type_id")
    private ThingEventType type;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completedDate;

    public ThingEvent getEvent() {
        return event;
    }

    public void setEvent(ThingEvent event) {
        this.event = event;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public ThingEventType getType() {
        return type;
    }

    public void setType(ThingEventType type) {
        this.type = type;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}
