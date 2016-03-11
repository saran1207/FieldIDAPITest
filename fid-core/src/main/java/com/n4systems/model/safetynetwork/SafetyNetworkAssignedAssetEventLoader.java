package com.n4systems.model.safetynetwork;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Asset;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.NonSecureIdLoader;

import javax.persistence.EntityManager;
import java.util.List;

public class SafetyNetworkAssignedAssetEventLoader extends SafetyNetworkEventLoader {
	private final AssetsByNetworkIdLoader assetsByNetworkIdLoader;

	public SafetyNetworkAssignedAssetEventLoader(SecurityFilter filter, NonSecureIdLoader<ThingEvent> eventLoader, AssetsByNetworkIdLoader assetsByNetworkIdLoader) {
		super(filter, eventLoader);
		this.assetsByNetworkIdLoader = assetsByNetworkIdLoader;
	}
	
	public SafetyNetworkAssignedAssetEventLoader(SecurityFilter filter) {
		super(filter);
		this.assetsByNetworkIdLoader = new AssetsByNetworkIdLoader(filter);
	}
	
	@Override
	protected boolean accessAllowed(EntityManager em, SecurityFilter filter, AbstractEvent<?,Asset> event) {
		SafetyNetworkAssetSecurityManager securityManager = new SafetyNetworkAssetSecurityManager(filter.getOwner());
		
		List<Asset> linkedAssets = getLinkedAssets(em, filter, event.getTarget());
		
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
