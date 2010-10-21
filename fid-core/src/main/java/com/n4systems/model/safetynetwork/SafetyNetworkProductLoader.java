package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.persistence.utils.PostFetcher;

public class SafetyNetworkProductLoader extends SecurityFilteredLoader<Asset> {
	private final FilteredIdLoader<Asset> productLoader;
	private final VendorLinkedOrgLoader vendorOrgLoader; 
	private String[] postLoadFields = new String[0];
	
	public SafetyNetworkProductLoader(SecurityFilter filter, FilteredIdLoader<Asset> productLoader, VendorLinkedOrgLoader vendorOrgLoader) {
		super(filter);
		this.productLoader = productLoader;
		this.vendorOrgLoader = vendorOrgLoader;
	}
	
	public SafetyNetworkProductLoader(SecurityFilter filter) {
		// these doesn't need SecurityFilters since we call the load(em, filter) directly
		this(
				filter, 
				new FilteredIdLoader<Asset>(null, Asset.class),
				new VendorLinkedOrgLoader(null)
		);
	}

	@Override
	protected Asset load(EntityManager em, SecurityFilter filter) {
		// since we don't know the vendor of this asset, we're going to
		// look it up unsecured first, get the owner off it, and then do the 
		// security check.
		Asset asset = PostFetcher.postFetchFields(productLoader.load(em, new OpenSecurityFilter()), postLoadFields);
		
		// First we need to ensure that the PrimaryOrg owner for this asset is one of my vendors.
		// if, it is not, this loader will throw a SecurityException
		InternalOrg vendor = vendorOrgLoader.setLinkedOrg(asset.getOwner().getPrimaryOrg()).load(em, filter);
		
		// this check is extraneous but is here as a fail safe
		if (vendor == null) {
			throw new SecurityException(String.format("Org [%s] attempted access to Asset for org [%s] ", filter.getOwner().toString(), asset.getOwner().toString()));
		}
				
		SafetyNetworkProductSecurityManager securityManager = new SafetyNetworkProductSecurityManager(filter.getOwner());

		// if the owner was for an ExternalOrg, we need to make sure it's assigned to me
		if (asset.getOwner().isExternal() && !securityManager.isAssigned(asset)) {
			throw new SecurityException(String.format("Org [%s] attempted access to Asset for org [%s] ", filter.getOwner().toString(), asset.getOwner().toString()));
		}
		
		return asset;
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
