package com.n4systems.model;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.*;

@Entity
@Table(name="place_events")
@PrimaryKeyJoinColumn(name="id")
public class PlaceEvent extends Event<PlaceEventType, BaseOrg> {

    @ManyToOne
    @JoinColumn(name="place_id")
    private BaseOrg place;

    @ManyToOne
    @JoinColumn(name="place_event_type_id")
    private PlaceEventType eventType;

    @Override
    public PlaceEventType getType() {
        return eventType;
    }

    @Override
    public void setType(PlaceEventType type) {
        eventType = type;
    }

    @Override
    public BaseOrg getTarget() {
        return getPlace();
    }

    @Override
    public void setTarget(BaseOrg target) {
        setPlace(target);
    }

    @Override
    public ThingEvent enhance(SecurityLevel level) {
        return null;
    }

    public BaseOrg getPlace() {
        return place;
    }

    public void setPlace(BaseOrg place) {
        this.place = place;
    }

    @Override
    protected void copyDataIntoResultingAction(AbstractEvent<?,?> event) {
        PlaceEvent action = (PlaceEvent) event;
        action.setPlace(getPlace());
    }

}
