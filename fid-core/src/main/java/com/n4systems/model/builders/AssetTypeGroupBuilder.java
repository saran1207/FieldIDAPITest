package com.n4systems.model.builders;

import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.Tenant;

public class AssetTypeGroupBuilder extends EntityWithTenantBuilder<AssetTypeGroup> {

	private String name;	
	private Long orderIdx;
	private Tenant tenant;
	
	public AssetTypeGroupBuilder(Tenant tenant, String name, long orderIdx) {
		this.tenant = tenant;
		this.name = name;
		this.orderIdx = orderIdx;
	}

	public static AssetTypeGroupBuilder anAssetTypeGroup() {
		return new AssetTypeGroupBuilder( TenantBuilder.aTenant().build() ,"AssetTypeGroup", 1L);
	}

	@Override
	public AssetTypeGroup createObject() {
		AssetTypeGroup assetTypeGroup = new AssetTypeGroup();
		assetTypeGroup.setTenant(tenant);
		assetTypeGroup.setName(name);
		assetTypeGroup.setOrderIdx(orderIdx);
		return assetTypeGroup;
	}
	
	public AssetTypeGroupBuilder withTenant(Tenant tenant) {
		return makeBuilder(new AssetTypeGroupBuilder(tenant, name, orderIdx));
	}

	public AssetTypeGroupBuilder withName(String name) {
		return makeBuilder(new AssetTypeGroupBuilder(tenant, name, orderIdx));
	}

	public AssetTypeGroupBuilder withOrderIdx(Long orderIdx) {
		return makeBuilder(new AssetTypeGroupBuilder(tenant, name, orderIdx));
	}
	
}
