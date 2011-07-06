package com.n4systems.model.orgs;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

import javax.persistence.EntityManager;

public class InternalOrgsLoader extends ListLoader<InternalOrg> {

    public InternalOrgsLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<InternalOrg> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<InternalOrg> query = new QueryBuilder<InternalOrg>(InternalOrg.class, filter);
        query.addOrder("name");

        return query.getResultList(em);
    }

}
