package com.n4systems.model.safetynetwork;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.loaders.Loader;

import javax.persistence.EntityManager;

public class SafetyNetworkUnregisteredAssetCountLoader extends Loader<Long> {

	private PrimaryOrg vendor;
	private PrimaryOrg customer;

    @Override
	public Long load(EntityManager em) {
        UnregisteredAssetQueryHelper helper = new UnregisteredAssetQueryHelper(vendor, customer, true);

        return helper.getCount(em);
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
