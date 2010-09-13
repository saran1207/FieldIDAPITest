package com.n4systems.model.tenant;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;

public class PrimaryOrgsWithNameLikeLoader extends PaginatedLoader<PrimaryOrg> {

    private String name;
    private boolean searchableOnly;

    public PrimaryOrgsWithNameLikeLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected QueryBuilder<PrimaryOrg> createBuilder(SecurityFilter filter) {
        QueryBuilder<PrimaryOrg> queryBuilder = new QueryBuilder<PrimaryOrg>(PrimaryOrg.class, new OpenSecurityFilter());

        queryBuilder.addWhere(WhereParameter.Comparator.LIKE, "name", "name", name, WhereParameter.WILDCARD_BOTH);

        if (searchableOnly)
            queryBuilder.addSimpleWhere("searchableOnSafetyNetwork", true);

        return queryBuilder;
    }

    public PrimaryOrgsWithNameLikeLoader setName(String name) {
        this.name = name;
        return this;
    }

    public PrimaryOrgsWithNameLikeLoader setSearchableOnly(boolean searchableOnly) {
        this.searchableOnly = searchableOnly;
        return this;
    }

}
