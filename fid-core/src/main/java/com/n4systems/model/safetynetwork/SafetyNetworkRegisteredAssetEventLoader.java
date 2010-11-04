package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Event;
import com.n4systems.model.asset.AssetWithNetworkIdExistsLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.NonSecureIdLoader;

public class SafetyNetworkRegisteredAssetEventLoader extends SafetyNetworkEventLoader {
	private final AssetWithNetworkIdExistsLoader assetExistsLoader;
	
	public SafetyNetworkRegisteredAssetEventLoader(SecurityFilter filter, NonSecureIdLoader<Event> eventLoader, AssetWithNetworkIdExistsLoader assetExistsLoader) {
		super(filter, eventLoader);
		this.assetExistsLoader = assetExistsLoader;
	}
	
	public SafetyNetworkRegisteredAssetEventLoader(SecurityFilter filter) {
		super(filter);
		this.assetExistsLoader = new AssetWithNetworkIdExistsLoader(filter);
	}

	@Override
	protected boolean accessAllowed(EntityManager em, SecurityFilter filter, Event event) {
		// Access is allowed to this event if we have an asset that is linked to its asset
		assetExistsLoader.setNetworkId(event.getAsset().getNetworkId());
		
		boolean accessAllowed = assetExistsLoader.load(em, filter);
		return accessAllowed;
	}
	
}
