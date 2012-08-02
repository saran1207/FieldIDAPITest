package com.n4systems.model.builders;

import com.n4systems.model.EventStatus;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable.EntityState;

public class EventStatusBuilder extends BaseBuilder<EventStatus> {

    private final String name;
    private Tenant tenant;
    private EntityState state;

    public static EventStatusBuilder anEventStatus() {
        return new EventStatusBuilder(null, null, EntityState.ACTIVE);
    }

    private EventStatusBuilder(String name, Tenant tenant, EntityState state) {
        this.name = name;
        this.tenant = tenant;
        this.state = state;
    }

    public EventStatusBuilder named(String name) {
        return makeBuilder(new EventStatusBuilder(name, tenant, state));
    }

    public EventStatusBuilder forTenant(Tenant tenant) {
        return makeBuilder(new EventStatusBuilder(name, tenant, state));
    }

    public EventStatusBuilder withState(EntityState state) {
        return makeBuilder(new EventStatusBuilder(name, tenant, state));
    }

    @Override
    public EventStatus createObject() {
        EventStatus status = new EventStatus();
        status.setName(name);
        status.setTenant(tenant);
        status.setState(state);
        return status;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

}
