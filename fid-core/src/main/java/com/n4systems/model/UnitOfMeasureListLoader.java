package com.n4systems.model;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class UnitOfMeasureListLoader extends ListLoader<UnitOfMeasure> {

    public UnitOfMeasureListLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<UnitOfMeasure> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<UnitOfMeasure> query = new QueryBuilder<UnitOfMeasure>(UnitOfMeasure.class, filter);

        return query.getResultList(em);
    }

}
