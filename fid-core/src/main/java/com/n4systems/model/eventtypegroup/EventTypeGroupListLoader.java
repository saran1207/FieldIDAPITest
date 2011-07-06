package com.n4systems.model.eventtypegroup;

import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

import javax.persistence.EntityManager;

public class EventTypeGroupListLoader extends ListLoader<EventTypeGroup> {

    public EventTypeGroupListLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<EventTypeGroup> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<EventTypeGroup> query = new QueryBuilder<EventTypeGroup>(EventTypeGroup.class, filter);
        query.addOrder("name");
        return query.getResultList(em);
    }

}
