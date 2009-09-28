package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;

public class SafetyNetworkProductLoader extends SecurityFilteredLoader<Product> {
	private final FilteredIdLoader<Product> productLoader;
	private final VendorLinkedOrgLoader vendorOrgLoader; 
	
	public SafetyNetworkProductLoader(SecurityFilter filter, FilteredIdLoader<Product> productLoader, VendorLinkedOrgLoader vendorOrgLoader) {
		super(filter);
		this.productLoader = productLoader;
		this.vendorOrgLoader = vendorOrgLoader;
	}
	
	public SafetyNetworkProductLoader(SecurityFilter filter) {
		// these doesn't need SecurityFilters since we call the load(em, filter) directly
		this(
				filter, 
				new FilteredIdLoader<Product>(null, Product.class),
				new VendorLinkedOrgLoader(null)
		);
	}

	@Override
	protected Product load(EntityManager em, SecurityFilter filter) {
		// since we don't know the vendor of this product, we're going to 
		// look it up unsecured first, get the owner off it, and then do the 
		// security check.
		Product product = productLoader.load(em, new OpenSecurityFilter());
		
		// the act of calling load on the linked org loader will cause a security exception if no connection exists
		vendorOrgLoader.setLinkedOrgId(product.getOwner().getId()).load(em, filter);
		
		return product;
	}

	public SafetyNetworkProductLoader setProductId(Long productId) {
		productLoader.setId(productId);
		return this;
	}
}
