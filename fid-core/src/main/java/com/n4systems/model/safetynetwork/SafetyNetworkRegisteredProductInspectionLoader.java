package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Inspection;
import com.n4systems.model.product.AssetWithNetworkIdExistsLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.NonSecureIdLoader;

public class SafetyNetworkRegisteredProductInspectionLoader extends SafetyNetworkInspectionLoader {
	private final AssetWithNetworkIdExistsLoader assetExistsLoader;
	
	public SafetyNetworkRegisteredProductInspectionLoader(SecurityFilter filter, NonSecureIdLoader<Inspection> inspectionLoader, AssetWithNetworkIdExistsLoader assetExistsLoader) {
		super(filter, inspectionLoader);
		this.assetExistsLoader = assetExistsLoader;
	}
	
	public SafetyNetworkRegisteredProductInspectionLoader(SecurityFilter filter) {
		super(filter);
		this.assetExistsLoader = new AssetWithNetworkIdExistsLoader(filter);
	}

	@Override
	protected boolean accessAllowed(EntityManager em, SecurityFilter filter, Inspection inspection) {
		// Access is allowed to this inspection if we have an asset that is linked to its asset
		assetExistsLoader.setNetworkId(inspection.getAsset().getNetworkId());
		
		boolean accessAllowed = assetExistsLoader.load(em, filter);
		return accessAllowed;
	}
	
}
