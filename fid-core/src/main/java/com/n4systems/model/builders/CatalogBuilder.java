package com.n4systems.model.builders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.model.catalog.Catalog;

public class CatalogBuilder extends BaseBuilder<Catalog> {

	public static CatalogBuilder aCatalog() {
		return new CatalogBuilder();
	}
	
	private final Set<ProductType> publishedProductTypes;
	private final Tenant tenant;
	
	public CatalogBuilder() {
		this(TenantBuilder.aTenant().build(), new HashSet<ProductType>());
	}
	
	public CatalogBuilder(Tenant tenant, Set<ProductType> publishedProductTypes) {
		this.publishedProductTypes = publishedProductTypes;
		this.tenant = tenant;
	}
	
	public CatalogBuilder belongingTo(Tenant tenant) {
		return new CatalogBuilder(tenant, this.publishedProductTypes);
	}
	
	public CatalogBuilder with(ProductType...publishedProductTypes) {
		Set<ProductType> publishedTypes = new HashSet<ProductType>(Arrays.asList(publishedProductTypes));
		return new CatalogBuilder(this.tenant, publishedTypes);
	}

	@Override
	public Catalog createObject() {
		Catalog catalog = new Catalog();
		catalog.setId(id);
		catalog.setPublishedProductTypes(publishedProductTypes);
		catalog.setTenant(tenant);
		return catalog;
	}

}
