package com.n4systems.model;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithOwner;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.*;

@Entity
@Table(name = "recurring_place_events")
@PrimaryKeyJoinColumn(name="id")
public class RecurringPlaceEvent extends ArchivableEntityWithOwner implements Saveable, SecurityEnhanced<RecurringPlaceEvent>, Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_type_id", nullable = false)
    private PlaceEventType eventType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="recurrence_id")
    private Recurrence recurrence = new Recurrence();

    private boolean autoAssign;

    public RecurringPlaceEvent() {
        super();
    }

    public RecurringPlaceEvent(BaseOrg place) {
        this(place, null, new Recurrence());
    }

    public RecurringPlaceEvent(BaseOrg place, PlaceEventType eventType, Recurrence recurrence) {
        //Preconditions.checkArgument(place!=null,"can't pass null org.");
        super(place.getTenant(), place);
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

    public boolean isAutoAssign() {
        return autoAssign;
    }

    public void setAutoAssign(boolean autoAssign) {
        this.autoAssign = autoAssign;
    }

    public PlaceEventType getEventType() {
        return eventType;
    }

    public void setEventType(PlaceEventType eventType) {
        this.eventType = eventType;
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

}
