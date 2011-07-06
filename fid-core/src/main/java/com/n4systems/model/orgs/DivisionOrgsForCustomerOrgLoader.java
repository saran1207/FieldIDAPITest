package com.n4systems.model.orgs;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

import javax.persistence.EntityManager;

public class DivisionOrgsForCustomerOrgLoader extends ListLoader<DivisionOrg> {

    private BaseOrg parent;

    public DivisionOrgsForCustomerOrgLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<DivisionOrg> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<DivisionOrg> query = new QueryBuilder<DivisionOrg>(DivisionOrg.class, filter);
        query.addSimpleWhere("parent", parent);
        query.addOrder("name");

        return query.getResultList(em);
    }

    public DivisionOrgsForCustomerOrgLoader parent(BaseOrg parent) {
        this.parent = parent;
        return this;
    }
}
