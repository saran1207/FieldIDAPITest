package com.n4systems.model;

import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name="place_events")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "EventCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PlaceEvent extends Event<PlaceEventType, PlaceEvent, BaseOrg> implements NetworkEntity<PlaceEvent> {

    public static final SecurityDefiner createSecurityDefiner() {
        return new SecurityDefiner("tenant.id", "place", null, "state", true);
    }

    @ManyToOne
    @JoinColumn(name="place_id")
    private BaseOrg place;

    @ManyToOne
    @JoinColumn(name="recurring_event_id")
    private RecurringPlaceEvent recurringEvent;

    @Override
    public BaseOrg getTarget() {
        return getPlace();
    }

    @Override
    public void setTarget(BaseOrg target) {
        setPlace(target);
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

    @Override
    @AllowSafetyNetworkAccess
    public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
        return SecurityLevel.calculateSecurityLevel(fromOrg, getPlace());
    }

    @Override
    public BaseOrg getOwner() {
        return getPlace();
    }

    @Override
    public void setOwner(BaseOrg owner) {
        setPlace(owner);
    }

    @Override
    public PlaceEvent enhance(SecurityLevel level) {
        return EntitySecurityEnhancer.enhance(this, level);
    }

    public RecurringPlaceEvent getRecurringEvent() {
        return recurringEvent;
    }

    public void setRecurringEvent(RecurringPlaceEvent recurringEvent) {
        this.recurringEvent = recurringEvent;
    }
}
