package com.n4systems.model;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithOwner;

import javax.persistence.*;


@MappedSuperclass
public abstract class RecurringEvent<E extends EventType> extends ArchivableEntityWithOwner implements Saveable, SecurityEnhanced<RecurringEvent>, Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_type_id", nullable = false)
    private E eventType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="recurrence_id")
    private Recurrence recurrence = new Recurrence();

    private boolean autoAssign;

    protected RecurringEvent(Tenant tenant, BaseOrg owner) {
        super(tenant, owner);
    }

    public RecurringEvent() {
    }

    public boolean isAutoAssign() {
        return autoAssign;
    }

    public void setAutoAssign(boolean autoAssign) {
        this.autoAssign = autoAssign;
    }

    public E getEventType() {
        return eventType;
    }

    public void setEventType(E eventType) {
        this.eventType = eventType;
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }
}
