package com.n4systems.model.event;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.EventType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class EventTypesByEventGroupIdLoader extends ListLoader<EventType> {

    private Long eventTypeGroupId;

    public EventTypesByEventGroupIdLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<EventType> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<EventType> builder = new QueryBuilder<EventType>(EventType.class, filter);

        if (eventTypeGroupId != null) { 
            builder.addSimpleWhere("group.id", eventTypeGroupId);
        }

        builder.addOrder("name");

        return builder.getResultList(em);
    }

    public EventTypesByEventGroupIdLoader setEventTypeGroupId(Long eventTypeGroupId) {
        this.eventTypeGroupId = eventTypeGroupId;
        return this;
    }

}
