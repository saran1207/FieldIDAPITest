package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.persistence.utils.PostFetcher;

public class SafetyNetworkProductLoader extends SecurityFilteredLoader<Product> {
	private final FilteredIdLoader<Product> productLoader;
	private final VendorLinkedOrgLoader vendorOrgLoader; 
	private String[] postLoadFields = new String[0];
	
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
		Product product = PostFetcher.postFetchFields(productLoader.load(em, new OpenSecurityFilter()), postLoadFields);
		
		// First we need to ensure that the PrimaryOrg owner for this product is one of my vendors.
		// if, it is not, this loader will throw a SecurityException
		InternalOrg vendor = vendorOrgLoader.setLinkedOrg(product.getOwner().getPrimaryOrg()).load(em, filter);
		
		// this check is extraneous but is here as a fail safe
		if (vendor == null) {
			throw new SecurityException(String.format("Org [%s] attempted access to Product for org [%s] ", filter.getOwner().toString(), product.getOwner().toString()));
		}
				
		SafetyNetworkProductSecurityManager securityManager = new SafetyNetworkProductSecurityManager(filter.getOwner());

		// if the owner was for an ExternalOrg, we need to make sure it's assigned to me
		if (product.getOwner().isExternal() && !securityManager.isAssigned(product)) {
			throw new SecurityException(String.format("Org [%s] attempted access to Product for org [%s] ", filter.getOwner().toString(), product.getOwner().toString()));
		}
		
		return product;
	}

	public SafetyNetworkProductLoader setProductId(Long productId) {
		productLoader.setId(productId);
		return this;
	}
	
	public SafetyNetworkProductLoader withAllFields() {
		postLoadFields = new String[] {"infoOptions", "type.infoFields", "type.inspectionTypes", "type.attachments", "type.subTypes", "projects", "modifiedBy.displayName"};
		return this;
	}
}
