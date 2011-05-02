package com.n4systems.model.orgs.customer;

import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

import javax.persistence.EntityManager;

public class CustomerOrgsWithNameLoader extends ListLoader<CustomerOrg> {

    private String name;

    public CustomerOrgsWithNameLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<CustomerOrg> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<CustomerOrg> query = new QueryBuilder<CustomerOrg>(CustomerOrg.class, filter);
        query.addSimpleWhere("name", name);
        return query.getResultList(em);
    }

    public void setName(String name) {
        this.name = name;
    }

}
