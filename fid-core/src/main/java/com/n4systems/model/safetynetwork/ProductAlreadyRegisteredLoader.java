package com.n4systems.model.safetynetwork;

import com.n4systems.model.Product;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;

public class ProductAlreadyRegisteredLoader extends SecurityFilteredLoader<Product> {

    private long networkId;

    public ProductAlreadyRegisteredLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected Product load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<Product> queryBuilder = new QueryBuilder<Product>(Product.class, filter);


        queryBuilder.addSimpleWhere("networkId", networkId);

        return queryBuilder.getSingleResult(em);
    }

    public ProductAlreadyRegisteredLoader setNetworkId(long networkId) {
        this.networkId = networkId;
        return this;
    }

}
