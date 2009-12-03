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

public class ProductBuilder extends BaseBuilder<Product>{

	private final Tenant tenantOrganization;
	private final ProductType type;

	private String serialNumber;
	private Date modified;
	private List<SubProduct> subProducts = new ArrayList<SubProduct>();
	
	public static ProductBuilder aProduct() {
		return new ProductBuilder(TenantBuilder.n4(), aProductType().build());
	}
	
	public ProductBuilder(Tenant tenantOrganization, ProductType type) {
		super();
		this.type = type;
		this.tenantOrganization = tenantOrganization;
	}
	
	public ProductBuilder ofType(ProductType type) {
		return new ProductBuilder(tenantOrganization, type);
	}
	
	public ProductBuilder forTenant(Tenant tenantOrganization) {
		return new ProductBuilder(tenantOrganization, type);
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
		product.setTenant(tenantOrganization);
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
