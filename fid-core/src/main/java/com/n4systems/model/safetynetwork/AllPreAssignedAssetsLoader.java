package com.n4systems.model.safetynetwork;

import com.n4systems.model.Asset;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;

import java.util.List;

import javax.persistence.EntityManager;

public class AllPreAssignedAssetsLoader extends ListLoader<Asset> {

    private PrimaryOrg vendor;
    private PrimaryOrg customer;

    public AllPreAssignedAssetsLoader(SecurityFilter filter) {
        super(filter);
    }

    public AllPreAssignedAssetsLoader setVendor(PrimaryOrg vendor) {
        this.vendor = vendor;
        return this;
    }

    public AllPreAssignedAssetsLoader setCustomer(PrimaryOrg customer) {
        this.customer = customer;
        return this;
    }

    @Override
    protected List<Asset> load(EntityManager em, SecurityFilter filter) {
        UnregisteredAssetQueryHelper helper = new UnregisteredAssetQueryHelper(vendor, customer, true);

        return helper.getList(em);
    }

}
