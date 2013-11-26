package com.n4systems.model;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.EntityWithOwner;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

// DISCONNECTED FOR NOW....WILL HAVE TO HOOK UP TO REAL TABLE WHEN BACK END REFACTORING IS DONE.
//@Entity
//@Table(name = "assettypeschedules")
//@Localized(ignore=true)
public class OrgRecurringEvent extends EntityWithOwner implements Saveable, SecurityEnhanced<OrgRecurringEvent> {

    @ManyToOne(optional=false)
    @JoinColumn(name = "place_event_type_id")
    private EventType eventType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="recurrence_id")
    private Recurrence recurrence;

    private boolean autoAssign;   // TODO DD : is this needed? ask matt?

    public OrgRecurringEvent() {
    }

    @Override
    public OrgRecurringEvent enhance(SecurityLevel level) {
        OrgRecurringEvent enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
        return enhanced;
    }

    public boolean isAutoAssign() {
        return autoAssign;
    }

    public void setAutoAssign(boolean autoAssign) {
        this.autoAssign = autoAssign;
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
}
