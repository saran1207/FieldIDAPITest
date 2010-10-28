package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.Inspection;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.NonSecureIdLoader;

public class SafetyNetworkAssignedAssetInspectionLoader extends SafetyNetworkInspectionLoader {
	private final AssetsByNetworkIdLoader assetsByNetworkIdLoader;

	public SafetyNetworkAssignedAssetInspectionLoader(SecurityFilter filter, NonSecureIdLoader<Inspection> inspectionLoader, AssetsByNetworkIdLoader assetsByNetworkIdLoader) {
		super(filter, inspectionLoader);
		this.assetsByNetworkIdLoader = assetsByNetworkIdLoader;
	}
	
	public SafetyNetworkAssignedAssetInspectionLoader(SecurityFilter filter) {
		super(filter);
		this.assetsByNetworkIdLoader = new AssetsByNetworkIdLoader(filter);
	}
	
	@Override
	protected boolean accessAllowed(EntityManager em, SecurityFilter filter, Inspection inspection) {
		SafetyNetworkAssetSecurityManager securityManager = new SafetyNetworkAssetSecurityManager(filter.getOwner());
		
		List<Asset> linkedAssets = getLinkedAssets(em, filter, inspection.getAsset());
		
		boolean hasAssignedAsset = securityManager.listContainsAnAssignedAsset(linkedAssets);
		boolean assetIsPubliclyAvailable = securityManager.listContainsAnAssetPubliclyPublished(linkedAssets);
		
		return hasAssignedAsset || assetIsPubliclyAvailable;
	}

	private List<Asset> getLinkedAssets(EntityManager em, SecurityFilter filter, Asset asset) {
		assetsByNetworkIdLoader.setNetworkId(asset.getNetworkId());
		
		// there's no need for these to be enhanced since they're just used for a security check
		assetsByNetworkIdLoader.bypassSecurityEnhancement();
		
		return assetsByNetworkIdLoader.load(em, filter);
	}
	
}
