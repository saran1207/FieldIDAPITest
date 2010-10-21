package com.n4systems.model.safetynetwork;

import com.n4systems.model.Asset;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.tools.Pager;

import javax.persistence.EntityManager;

public class SafetyNetworkPreAssignedAssetLoader extends Loader<Pager<Asset>> {

	private PrimaryOrg vendor;
	private PrimaryOrg customer;

    private Integer page;
    private Integer pageSize = 10;
	
	public SafetyNetworkPreAssignedAssetLoader setVendor(PrimaryOrg vendor) {
		this.vendor = vendor;
		return this;
	}
	
	public SafetyNetworkPreAssignedAssetLoader setCustomer(PrimaryOrg customer) {
		this.customer = customer;
		return this;
	}

    @Override
    protected Pager<Asset> load(EntityManager em) {
        UnregisteredAssetQueryHelper helper = new UnregisteredAssetQueryHelper(vendor, customer, true);

        return helper.getPager(em, page, pageSize, new String[] { "infoOptions" });
    }

    public SafetyNetworkPreAssignedAssetLoader setPage(Integer page) {
        this.page = page;
        return this;
    }

    public SafetyNetworkPreAssignedAssetLoader setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

}
