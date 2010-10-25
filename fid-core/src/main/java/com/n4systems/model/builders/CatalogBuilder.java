package com.n4systems.model.builders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.n4systems.model.AssetType;
import com.n4systems.model.Tenant;
import com.n4systems.model.catalog.Catalog;

public class CatalogBuilder extends BaseBuilder<Catalog> {

	public static CatalogBuilder aCatalog() {
		return new CatalogBuilder();
	}
	
	private final Set<AssetType> publishedAssetTypes;
	private final Tenant tenant;
	
	public CatalogBuilder() {
		this(TenantBuilder.aTenant().build(), new HashSet<AssetType>());
	}
	
	public CatalogBuilder(Tenant tenant, Set<AssetType> publishedAssetTypes) {
		this.publishedAssetTypes = publishedAssetTypes;
		this.tenant = tenant;
	}
	
	public CatalogBuilder belongingTo(Tenant tenant) {
		return new CatalogBuilder(tenant, this.publishedAssetTypes);
	}
	
	public CatalogBuilder with(AssetType... publishedAssetTypes) {
		Set<AssetType> publishedTypes = new HashSet<AssetType>(Arrays.asList(publishedAssetTypes));
		return new CatalogBuilder(this.tenant, publishedTypes);
	}

	@Override
	public Catalog createObject() {
		Catalog catalog = new Catalog();
		catalog.setId(id);
		catalog.setPublishedAssetTypes(publishedAssetTypes);
		catalog.setTenant(tenant);
		return catalog;
	}

}
