package com.n4systems.model.builders;

import static com.n4systems.model.builders.ProductTypeBuilder.*;
import static com.n4systems.model.builders.SubProductBuilder.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.SubProduct;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;

public class ProductBuilder extends BaseBuilder<Product>{

	private final Tenant tenant;
	private final BaseOrg owner;
	private final ProductType type;

	private String serialNumber;
	private Date modified;
	
	private List<SubProduct> subProducts = new ArrayList<SubProduct>();
	
	public static ProductBuilder aProduct() {
		return new ProductBuilder(TenantBuilder.n4(), OrgBuilder.aPrimaryOrg().build(), aProductType().build());
	}
	
	public ProductBuilder(Tenant tenantOrganization, BaseOrg owner, ProductType type) {
		super();
		this.tenant = tenantOrganization;
		this.owner = owner;
		this.type = type;
	}
	
	public ProductBuilder ofType(ProductType type) {
		return new ProductBuilder(tenant, owner, type);
	}
	
	public ProductBuilder forTenant(Tenant tenant) {
		return new ProductBuilder(tenant, owner, type);
	}
	
	public ProductBuilder withOwner(BaseOrg owner) {
		return new ProductBuilder(tenant, owner, type);
	}
	
	public ProductBuilder withSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
		return this;
	}
	
	public ProductBuilder withModifiedDate(Date modified) {
		this.modified = modified;
		return this;
	}
	
	public ProductBuilder withOneSubProduct() {
		subProducts = new ArrayList<SubProduct>();
		subProducts.add(aSubProduct().build());
		return this;
	}
	
	public ProductBuilder withTwoSubProducts() {
		subProducts = new ArrayList<SubProduct>();
		subProducts.add(aSubProduct().build());
		subProducts.add(aSubProduct().build());
		return this;
	}
	
	@Override
	public Product build() {
		Product product = generate();
		product.setId(id);
		populateMasterProductInSubProducts(product);
		product.setSubProducts(subProducts);
		return product;
	}
	
	public Product generate() {
		Product product = new Product();
		product.setTenant(tenant);
		product.setOwner(owner);
		product.setType(type);
		product.setSerialNumber(serialNumber);
		product.setModified(modified);
		
		return product;
	}
	
	private void populateMasterProductInSubProducts(Product product) {
		if (subProducts != null) {
			for (SubProduct subProduct : subProducts) {
				subProduct.setMasterProduct(product);
			}
		}
	}
	
	
	
}
