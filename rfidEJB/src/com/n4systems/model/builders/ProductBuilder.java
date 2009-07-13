package com.n4systems.model.builders;

import static com.n4systems.model.builders.ProductTypeBuilder.*;

import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.TenantOrganization;

public class ProductBuilder extends BaseBuilder<Product>{

	private final TenantOrganization tenantOrganization;
	private final ProductType type;
	
	public static ProductBuilder aProduct() {
		return new ProductBuilder(TenantBuilder.n4(), aProductType().build());
	}
	
	public ProductBuilder(TenantOrganization tenantOrganization, ProductType type) {
		super();
		this.type = type;
		this.tenantOrganization = tenantOrganization;
	}
	
	public ProductBuilder ofType(ProductType type) {
		return new ProductBuilder(tenantOrganization, type);
	}
	
	public ProductBuilder forTenant(TenantOrganization tenantOrganization) {
		return new ProductBuilder(tenantOrganization, type);
	}
	
	@Override
	public Product build() {
		Product product = generate();
		product.setId(id);
		return product;
	}
	
	public Product generate() {
		Product product = new Product();
		product.setTenant(tenantOrganization);
		product.setType(type);
		return product;
	}

	
	
}
