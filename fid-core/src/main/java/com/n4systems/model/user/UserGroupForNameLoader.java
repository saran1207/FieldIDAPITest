package com.n4systems.model.user;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

import javax.persistence.EntityManager;

public class UserGroupForNameLoader extends SecurityFilteredLoader<UserGroup> {

    private String name;

    public UserGroupForNameLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected UserGroup load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<UserGroup> builder = new QueryBuilder<UserGroup>(UserGroup.class, filter);
        builder.addWhere(WhereClauseFactory.create("name", name));
        return builder.getSingleResult(em);
    }

    public UserGroupForNameLoader setName(String name) {
        this.name = name;
        return this;
    }

}
