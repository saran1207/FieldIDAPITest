package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.Inspection;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.NonSecureIdLoader;

public class SafetyNetworkAssignedProductInspectionLoader extends SafetyNetworkInspectionLoader {
	private final ProductsByNetworkIdLoader productsByNetworkIdLoader;

	public SafetyNetworkAssignedProductInspectionLoader(SecurityFilter filter, NonSecureIdLoader<Inspection> inspectionLoader, ProductsByNetworkIdLoader productsByNetworkIdLoader) {
		super(filter, inspectionLoader);
		this.productsByNetworkIdLoader = productsByNetworkIdLoader;
	}
	
	public SafetyNetworkAssignedProductInspectionLoader(SecurityFilter filter) {
		super(filter);
		this.productsByNetworkIdLoader = new ProductsByNetworkIdLoader(filter);
	}
	
	@Override
	protected boolean accessAllowed(EntityManager em, SecurityFilter filter, Inspection inspection) {
		SafetyNetworkProductSecurityManager securityManager = new SafetyNetworkProductSecurityManager(filter.getOwner());
		
		List<Asset> linkedAssets = getLinkedProducts(em, filter, inspection.getAsset());
		
		boolean hasAssignedProduct = securityManager.listContainsAnAssignedProduct(linkedAssets);
		boolean productIsPubliclyAvailable = securityManager.listContainsAnAssetPubliclyPublished(linkedAssets);
		
		return hasAssignedProduct || productIsPubliclyAvailable;
	}

	private List<Asset> getLinkedProducts(EntityManager em, SecurityFilter filter, Asset asset) {
		productsByNetworkIdLoader.setNetworkId(asset.getNetworkId());
		
		// there's no need for these to be enhanced since they're just used for a security check
		productsByNetworkIdLoader.bypassSecurityEnhancement();
		
		return productsByNetworkIdLoader.load(em, filter);
	}
	
}
