package com.n4systems.model.safetynetwork;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.loaders.Loader;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class SafetyNetworkUnregisteredAssetCountLoader extends Loader<Long> {

	private PrimaryOrg vendor;
	private PrimaryOrg customer;

    @Override
    protected Long load(EntityManager em) {
        UnregisteredAssetQueryHelper helper = new UnregisteredAssetQueryHelper(vendor, customer);

        String hql = "SELECT COUNT(*) " + helper.createBaseQuery();

        Query query = em.createQuery(hql);
        helper.applyParameters(query);

        return (Long) query.getSingleResult();
    }

    public SafetyNetworkUnregisteredAssetCountLoader setVendor(PrimaryOrg vendor) {
        this.vendor = vendor;
        return this;
    }

    public SafetyNetworkUnregisteredAssetCountLoader setCustomer(PrimaryOrg customer) {
        this.customer = customer;
        return this;
    }

}
