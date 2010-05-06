package com.n4systems.model.builders;

import static com.n4systems.model.builders.ProductTypeBuilder.*;
import static com.n4systems.model.builders.SubProductBuilder.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.SubProduct;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.test.helpers.FluentArrayList;

public class ProductBuilder extends BaseBuilder<Product>{

	private final Tenant tenant;
	private final BaseOrg owner;
	private final ProductType type;

	private final String serialNumber;
	private final Date modified;
	
	private final List<SubProduct> subProducts;
	private final String location;
	private final ProductStatusBean productStatus;
	
	public static ProductBuilder aProduct() {
		return new ProductBuilder(TenantBuilder.n4(), OrgBuilder.aPrimaryOrg().build(), aProductType().build(), null, null, null, null, new ArrayList<SubProduct>());
	}
	
	


	public ProductBuilder(Tenant tenant, BaseOrg owner, ProductType type, String serialNumber, Date modified, String location, ProductStatusBean productStatus, List<SubProduct> subProducts) {
		super();
		this.tenant = tenant;
		this.owner = owner;
		this.type = type;
		this.serialNumber = serialNumber;
		this.modified = modified;
		this.location = location;
		this.productStatus = productStatus;
		this.subProducts = new ArrayList<SubProduct>(subProducts);
	}


	public ProductBuilder ofType(ProductType type) {
		return new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, subProducts);
	}
	
	public ProductBuilder forTenant(Tenant tenant) {
		return new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, subProducts);
	}
	
	public ProductBuilder withOwner(BaseOrg owner) {
		return new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, subProducts);
	}
	
	public ProductBuilder withSerialNumber(String serialNumber) {
		return new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, subProducts);
	}
	
	public ProductBuilder withModifiedDate(Date modified) {
		return new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, subProducts);
	}
	
	public ProductBuilder withOneSubProduct() {
		return new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, new FluentArrayList<SubProduct>(aSubProduct().build()));
		
	}
	
	public ProductBuilder withTwoSubProducts() {
		return new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, 
				new FluentArrayList<SubProduct>(aSubProduct().build(),aSubProduct().build()));
	}
	
	public ProductBuilder inLocation(String location) {
		return new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, subProducts);
	}
	
	public ProductBuilder havingStatus(ProductStatusBean productStatus) {
		return new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, subProducts);
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
		product.setLocation(location);
		product.setProductStatus(productStatus);
		
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
