package com.n4systems.model;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "recurring_place_events")
@PrimaryKeyJoinColumn(name="id")
public class RecurringPlaceEvent extends RecurringEvent<PlaceEventType> {

    public RecurringPlaceEvent() {
        super();
    }

    public RecurringPlaceEvent(BaseOrg place) {
        this(place, null, new Recurrence());
    }

    public RecurringPlaceEvent(BaseOrg place, PlaceEventType eventType, Recurrence recurrence) {
        //Preconditions.checkArgument(place!=null,"can't pass null org.");
        super(place.getTenant(),place);
        setEventType(eventType);
        setRecurrence(recurrence);
    }

    @Override
    public RecurringPlaceEvent enhance(SecurityLevel level) {
        RecurringPlaceEvent enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
        enhanced.setOwner(enhance(getOwner(), level));
        enhanced.setEventType(enhance(getEventType(),level));
        return enhanced;
    }

    @Override
    @Deprecated /** @see #getPlace instead **/
    public BaseOrg getOwner() {
        return super.getOwner();
    }

    public BaseOrg getPlace() {
        return getOwner();
    }

}
