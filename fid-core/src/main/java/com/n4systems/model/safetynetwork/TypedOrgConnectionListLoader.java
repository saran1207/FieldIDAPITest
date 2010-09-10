package com.n4systems.model.safetynetwork;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class TypedOrgConnectionListLoader extends ListLoader<TypedOrgConnection> {
    
    public TypedOrgConnectionListLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<TypedOrgConnection> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<TypedOrgConnection> query = new QueryBuilder<TypedOrgConnection>(TypedOrgConnection.class, filter);
        query.addOrder("connectedOrg.name");

        return query.getResultList(em);
    }

}
