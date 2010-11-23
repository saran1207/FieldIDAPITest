package com.n4systems.model.builders;

import com.n4systems.model.EventGroup;
import com.n4systems.model.Tenant;

public class EventGroupBuilder extends BaseBuilder<EventGroup> {

    private Tenant tenant;

    public static EventGroupBuilder anEventGroup() {
        return new EventGroupBuilder(null);
    }

    public EventGroupBuilder(Tenant tenant) {
        this.tenant = tenant;
    }

    public EventGroupBuilder forTenant(Tenant tenant) {
        return makeBuilder(new EventGroupBuilder(tenant));
    }

    @Override
    public EventGroup createObject() {
        EventGroup group = new EventGroup();
        group.setTenant(tenant);
        return group;
    }

}
