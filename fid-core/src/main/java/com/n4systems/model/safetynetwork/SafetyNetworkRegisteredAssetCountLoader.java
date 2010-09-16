package com.n4systems.model.safetynetwork;

import com.n4systems.model.Product;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OrgOnlySecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;

public class SafetyNetworkRegisteredAssetCountLoader extends Loader<Long> {

	private PrimaryOrg vendor;
	private PrimaryOrg customer;

    @Override
    protected Long load(EntityManager em) {
		QueryBuilder<Product> queryBuilder = new QueryBuilder<Product>(Product.class, new OrgOnlySecurityFilter(customer));
		queryBuilder.addSimpleWhere("linkedProduct.owner.tenant", vendor.getTenant());
        return queryBuilder.getCount(em);
    }

    public SafetyNetworkRegisteredAssetCountLoader setVendor(PrimaryOrg vendor) {
        this.vendor = vendor;
        return this;
    }

    public SafetyNetworkRegisteredAssetCountLoader setCustomer(PrimaryOrg customer) {
        this.customer = customer;
        return this;
    }
}
