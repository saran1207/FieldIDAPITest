package com.n4systems.model.stateset;

import com.n4systems.model.StateSet;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

import javax.persistence.EntityManager;

public class StateSetLoader extends ListLoader<StateSet> {

    public StateSetLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<StateSet> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<StateSet> query = new QueryBuilder<StateSet>(StateSet.class, filter);
        query.addSimpleWhere("retired", false);
        return query.getResultList(em);
    }

}
