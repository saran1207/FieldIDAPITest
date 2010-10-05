package com.n4systems.model.safetynetwork;

import com.n4systems.model.Product;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.OrgOnlySecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SubSelectNotExistsClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class UnregisteredAssetQueryHelper {

    private PrimaryOrg customer;
    private PrimaryOrg vendor;
    private SecurityFilter filter;
    private QueryBuilder<Product> builder;

    public UnregisteredAssetQueryHelper(PrimaryOrg vendor, PrimaryOrg customer) {
        this.customer = customer;
        this.vendor = vendor;
        filter = new OrgOnlySecurityFilter(vendor);

        builder = new QueryBuilder<Product>(Product.class, new OpenSecurityFilter());
        builder.setTableAlias("outerProduct");

        builder.addSimpleWhere("published", true);
        builder.addSimpleWhere("owner.customerOrg.linkedOrg", customer);
        builder.addSimpleWhere("tenant", vendor.getTenant());

        QueryBuilder<Product> subSelect = new QueryBuilder<Product>(Product.class, new OpenSecurityFilter());
        WhereParameter<String> whereClause = new WhereParameter<String>(WhereParameter.Comparator.EQ, "outerProduct", "linkedProduct", "IGNORED");
        whereClause.setLiteralParameter(true);

        subSelect.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "tenant.id", customer.getTenant().getId()));
        subSelect.addWhere(whereClause);

        SubSelectNotExistsClause subClause = new SubSelectNotExistsClause("networkIdPath", subSelect);
        builder.addWhere(subClause);
    }

    public Pager<Product> getList(EntityManager em, Integer first, Integer pageSize, String[] postFetchPaths) {
        builder.addPostFetchPaths(postFetchPaths);
        return builder.getPaginatedResults(em, first, pageSize);
    }

    public Long getCount(EntityManager em) {
        return builder.getCount(em);
    }

    public void applyParameters(Query query) {
        query.setParameter("owner", customer);
        query.setParameter("tenant_id", customer.getTenant().getId());
    }

}
