package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.model.orgs.ExternalOrg;
import com.n4systems.model.orgs.InternalOrg;
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
		
		// First we need to ensure that the InternalOrg owner for this product is one of my vendors.
		// if, it is not, this loader will throw a SecurityException
		InternalOrg vendor = vendorOrgLoader.setLinkedOrgId(product.getOwner().getInternalOrg().getId()).load(em, filter);
		
		// this check is extraneous but is here as a fail safe
		if (vendor == null) {
			throw new SecurityException(String.format("Org [%s] attempted access to Product for org [%s] ", filter.getOwner().toString(), product.getOwner().toString()));
		}
		
		// If the owner is an InternalOrg, we can stop here.
		if (product.getOwner().isInternal()) {
			return product;
		}
		
		// if the owner was for an ExternalOrg, we need to make sure the linked org 
		// is one I'm allowed to see
		if (!((ExternalOrg)product.getOwner()).getLinkedOrg().allowsAccessFor(filter.getOwner())) {
			throw new SecurityException(String.format("Org [%s] attempted access to Product for org [%s] ", filter.getOwner().toString(), product.getOwner().toString()));
		}
		
		return product;
	}

	public SafetyNetworkProductLoader setProductId(Long productId) {
		productLoader.setId(productId);
		return this;
	}
}
