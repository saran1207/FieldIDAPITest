package com.n4systems.model.safetynetwork;

import com.n4systems.model.Product;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

public class SafetyNetworkPreAssignedAssetLoader extends Loader<Pager<Product>> {

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
    protected Pager<Product> load(EntityManager em) {
        UnregisteredAssetQueryHelper helper = new UnregisteredAssetQueryHelper(vendor, customer);

        return helper.getList(em, page, pageSize, new String[] { "infoOptions" });
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
