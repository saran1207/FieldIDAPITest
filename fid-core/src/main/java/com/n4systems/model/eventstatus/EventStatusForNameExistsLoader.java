package com.n4systems.model.eventstatus;

import com.n4systems.model.EventStatus;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

import javax.persistence.EntityManager;

public class EventStatusForNameExistsLoader extends SecurityFilteredLoader<Boolean>{

    protected String name;
    protected Long id;

    public EventStatusForNameExistsLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected Boolean load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<EventStatus> builder = new QueryBuilder<EventStatus>(EventStatus.class, filter);
        builder.addSimpleWhere("name", name);
        if(id != null) {
           builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "id", id));
        }

        return builder.entityExists(em);
    }

    public EventStatusForNameExistsLoader setName(String name) {
        this.name = name;
        return this;
    }

    public EventStatusForNameExistsLoader setId(Long id) {
        this.id = id;
        return this;
    }
}
