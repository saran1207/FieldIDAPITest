package com.n4systems.model.orgs;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

import javax.persistence.EntityManager;

public class CustomerOrgsForInternalOrgLoader extends ListLoader<CustomerOrg> {

    private BaseOrg parent;

    public CustomerOrgsForInternalOrgLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<CustomerOrg> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<CustomerOrg> query = new QueryBuilder<CustomerOrg>(CustomerOrg.class, filter);
        query.addSimpleWhere("parent", parent);
        query.addOrder("name");

        return query.getResultList(em);
    }

    public CustomerOrgsForInternalOrgLoader parent(BaseOrg parent) {
        this.parent = parent;
        return this;
    }

}
