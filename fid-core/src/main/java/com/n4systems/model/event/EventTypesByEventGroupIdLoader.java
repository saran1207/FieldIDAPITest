package com.n4systems.model.event;

import com.n4systems.model.ThingEventType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class EventTypesByEventGroupIdLoader extends ListLoader<ThingEventType> {

    private Long eventTypeGroupId;

    public EventTypesByEventGroupIdLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<ThingEventType> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<ThingEventType> builder = new QueryBuilder<ThingEventType>(ThingEventType.class, filter);

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
