package com.n4systems.fieldid.wicket.model;

import com.n4systems.model.EventType;
import com.n4systems.model.eventtype.EventTypeListLoader;
import com.n4systems.model.security.SecurityFilter;

import java.util.List;

public class EventTypesForTenantModel extends FieldIDSpringModel<List<EventType>> {

    private SecurityFilter securityFilter;

    public EventTypesForTenantModel(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Override
    protected List<EventType> load() {
        return new EventTypeListLoader(securityFilter).doPostFetches(false).load();
    }

}
