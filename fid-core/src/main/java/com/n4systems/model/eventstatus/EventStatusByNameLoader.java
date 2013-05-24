package com.n4systems.model.eventstatus;

import com.n4systems.model.EventStatus;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;

public class EventStatusByNameLoader extends SecurityFilteredLoader<EventStatus> {

    private String name;

    public EventStatusByNameLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected EventStatus load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<EventStatus> builder = new QueryBuilder<EventStatus>(EventStatus.class, filter);
        builder.addSimpleWhere("name", name);

        return builder.getSingleResult(em);
    }

    public EventStatusByNameLoader setName(String name) {
        this.name = name;
        return this;
    }
}
