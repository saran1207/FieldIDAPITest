package com.n4systems.model.event;

import com.n4systems.model.EventType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

import javax.persistence.EntityManager;

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

    public void setEventTypeGroupId(Long eventTypeGroupId) {
        this.eventTypeGroupId = eventTypeGroupId;
    }

}
