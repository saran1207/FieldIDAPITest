package com.n4systems.model.safetynetwork;

import com.n4systems.model.Product;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OrgOnlySecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class SafetyNetworkPreAssignedAssetLoader extends PaginatedLoader<Product> {

	private PrimaryOrg vendor;
	private PrimaryOrg owner;
	
	public SafetyNetworkPreAssignedAssetLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<Product> createBuilder(SecurityFilter filter) {
		QueryBuilder<Product> queryBuilder = new QueryBuilder<Product>(Product.class, new OrgOnlySecurityFilter(vendor));
		queryBuilder.addSimpleWhere("owner.customerOrg.linkedOrg", owner);
		queryBuilder.addSimpleWhere("published", true);
		queryBuilder.addPostFetchPaths("infoOptions");
		return queryBuilder;
	}

	public SafetyNetworkPreAssignedAssetLoader setVendor(PrimaryOrg vendor) {
		this.vendor = vendor;
		return this;
	}
	
	public SafetyNetworkPreAssignedAssetLoader setOwner(PrimaryOrg owner) {
		this.owner = owner;
		return this;
	}

}
