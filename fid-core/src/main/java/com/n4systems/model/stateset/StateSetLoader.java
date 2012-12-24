package com.n4systems.model.stateset;

import com.n4systems.model.ButtonGroup;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

import javax.persistence.EntityManager;

public class StateSetLoader extends ListLoader<ButtonGroup> {

    public StateSetLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<ButtonGroup> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<ButtonGroup> query = new QueryBuilder<ButtonGroup>(ButtonGroup.class, filter);
        query.addSimpleWhere("retired", false);
        return query.getResultList(em);
    }

}
