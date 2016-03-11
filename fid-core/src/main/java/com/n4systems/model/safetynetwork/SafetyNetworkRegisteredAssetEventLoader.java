package com.n4systems.model.safetynetwork;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Asset;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.asset.AssetWithNetworkIdExistsLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.NonSecureIdLoader;

import javax.persistence.EntityManager;

public class SafetyNetworkRegisteredAssetEventLoader extends SafetyNetworkEventLoader {
	private final AssetWithNetworkIdExistsLoader assetExistsLoader;
	
	public SafetyNetworkRegisteredAssetEventLoader(SecurityFilter filter, NonSecureIdLoader<ThingEvent> eventLoader, AssetWithNetworkIdExistsLoader assetExistsLoader) {
		super(filter, eventLoader);
		this.assetExistsLoader = assetExistsLoader;
	}
	
	public SafetyNetworkRegisteredAssetEventLoader(SecurityFilter filter) {
		super(filter);
		this.assetExistsLoader = new AssetWithNetworkIdExistsLoader(filter);
	}

	@Override
	protected boolean accessAllowed(EntityManager em, SecurityFilter filter, AbstractEvent<?,Asset> event) {
		// Access is allowed to this event if we have an asset that is linked to its asset
		assetExistsLoader.setNetworkId(event.getTarget().getNetworkId());
		
		boolean accessAllowed = assetExistsLoader.load(em, filter);
		return accessAllowed;
	}
	
}
