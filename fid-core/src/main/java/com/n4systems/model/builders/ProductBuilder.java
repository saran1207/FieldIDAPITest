package com.n4systems.model.builders;

import static com.n4systems.model.builders.ProductTypeBuilder.*;
import static com.n4systems.model.builders.SubProductBuilder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.SubProduct;
import com.n4systems.model.Tenant;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

public class ProductBuilder extends BaseBuilder<Product>{
	private static final User NOT_ASSIGNED = null;

	private Tenant tenant;
	private BaseOrg owner;
	private ProductType type;

	private String serialNumber;
	private Date modified;
	
	private SubProduct[] subProducts;
	private Location location;
	private ProductStatusBean productStatus;
	private User assignedTo;
    private boolean published;
	
	public static ProductBuilder aProduct() {
		return new ProductBuilder(TenantBuilder.n4(), OrgBuilder.aPrimaryOrg().build(), aProductType().build(), null, null, new Location(), null, NOT_ASSIGNED, true);
	}

    public ProductBuilder(){}

	private ProductBuilder(Tenant tenant, BaseOrg owner, ProductType type, String serialNumber, Date modified, Location location, ProductStatusBean productStatus, User assignedTo, boolean published, SubProduct... subProducts) {
		super();
		this.tenant = tenant;
		this.owner = owner;
		this.type = type;
		this.serialNumber = serialNumber;
		this.modified = modified;
		this.location = location;
		this.productStatus = productStatus;
		this.assignedTo = assignedTo;
		this.subProducts = subProducts;
        this.published = published;
	}

	public ProductBuilder ofType(ProductType type) {
		return makeBuilder(new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, assignedTo, published, subProducts));
	}
	
	public ProductBuilder forTenant(Tenant tenant) {
		return makeBuilder(new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, assignedTo, published, subProducts));
	}
	
	public ProductBuilder withOwner(BaseOrg owner) {
		return makeBuilder(new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, assignedTo, published, subProducts));
	}
	
	public ProductBuilder withSerialNumber(String serialNumber) {
		return makeBuilder(new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, assignedTo, published, subProducts));
	}
	
	public ProductBuilder withModifiedDate(Date modified) {
		return makeBuilder(new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, assignedTo, published, subProducts));
	}
	
	public ProductBuilder withOneSubProduct() {
		return makeBuilder(new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, assignedTo, published, aSubProduct().build()));
	}
	
	public ProductBuilder withTwoSubProducts() {
		return makeBuilder(new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, assignedTo, published, aSubProduct().build(), aSubProduct().build()));
	}
	
	public ProductBuilder inFreeformLocation(String location) {
		return withAdvancedLocation(Location.onlyFreeformLocation(location));
	}
	
	public ProductBuilder withAdvancedLocation(Location location) {
		return makeBuilder(new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, assignedTo, published, subProducts));
	}
	
	public ProductBuilder havingStatus(ProductStatusBean productStatus) {
		return makeBuilder(new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, assignedTo, published, subProducts));
	}
	
	public ProductBuilder assignedTo(User employee) {
		return makeBuilder(new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, employee, published, subProducts));
	}
	
	public ProductBuilder unassigned() {
		return makeBuilder(new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, null, published, subProducts));
	}

    public ProductBuilder published(boolean published) {
		return makeBuilder(new ProductBuilder(tenant, owner, type, serialNumber, modified, location, productStatus, null, published, subProducts));
	}
	
	@Override
	public Product createObject() {
		Product product = generate();
		product.setId(id);
		populateMasterProductInSubProducts(product);
		product.setSubProducts(new ArrayList<SubProduct>(Arrays.asList(subProducts)));
		return product;
	}
	
	public Product generate() {
		Product product = new Product();
		
		product.setTenant(tenant);
		product.setOwner(owner);
		product.setType(type);
		product.setSerialNumber(serialNumber);
		product.setModified(modified);
		product.setProductStatus(productStatus);
		product.setAssignedUser(assignedTo);
		product.setAdvancedLocation(location);
        product.setPublished(published);
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
