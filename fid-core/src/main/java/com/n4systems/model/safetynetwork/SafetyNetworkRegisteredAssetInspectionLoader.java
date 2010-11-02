package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Event;
import com.n4systems.model.asset.AssetWithNetworkIdExistsLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.NonSecureIdLoader;

public class SafetyNetworkRegisteredAssetInspectionLoader extends SafetyNetworkInspectionLoader {
	private final AssetWithNetworkIdExistsLoader assetExistsLoader;
	
	public SafetyNetworkRegisteredAssetInspectionLoader(SecurityFilter filter, NonSecureIdLoader<Event> inspectionLoader, AssetWithNetworkIdExistsLoader assetExistsLoader) {
		super(filter, inspectionLoader);
		this.assetExistsLoader = assetExistsLoader;
	}
	
	public SafetyNetworkRegisteredAssetInspectionLoader(SecurityFilter filter) {
		super(filter);
		this.assetExistsLoader = new AssetWithNetworkIdExistsLoader(filter);
	}

	@Override
	protected boolean accessAllowed(EntityManager em, SecurityFilter filter, Event event) {
		// Access is allowed to this inspection if we have an asset that is linked to its asset
		assetExistsLoader.setNetworkId(event.getAsset().getNetworkId());
		
		boolean accessAllowed = assetExistsLoader.load(em, filter);
		return accessAllowed;
	}
	
}
