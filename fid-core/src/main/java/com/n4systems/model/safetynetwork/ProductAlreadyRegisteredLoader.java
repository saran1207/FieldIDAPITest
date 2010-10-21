package com.n4systems.model.safetynetwork;

import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;

public class ProductAlreadyRegisteredLoader extends SecurityFilteredLoader<Boolean> {

    private long networkId;

    public ProductAlreadyRegisteredLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected Boolean load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<Asset> queryBuilder = new QueryBuilder<Asset>(Asset.class, filter);

        queryBuilder.addSimpleWhere("networkId", networkId);

        return queryBuilder.getCount(em) > 0;
    }

    public ProductAlreadyRegisteredLoader setNetworkId(long networkId) {
        this.networkId = networkId;
        return this;
    }

}
