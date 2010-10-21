package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Inspection;
import com.n4systems.model.product.ProductWithNetworkIdExistsLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.NonSecureIdLoader;

public class SafetyNetworkRegisteredProductInspectionLoader extends SafetyNetworkInspectionLoader {
	private final ProductWithNetworkIdExistsLoader productExistsLoader;
	
	public SafetyNetworkRegisteredProductInspectionLoader(SecurityFilter filter, NonSecureIdLoader<Inspection> inspectionLoader, ProductWithNetworkIdExistsLoader productExistsLoader) {
		super(filter, inspectionLoader);
		this.productExistsLoader = productExistsLoader;
	}
	
	public SafetyNetworkRegisteredProductInspectionLoader(SecurityFilter filter) {
		super(filter);
		this.productExistsLoader = new ProductWithNetworkIdExistsLoader(filter);
	}

	@Override
	protected boolean accessAllowed(EntityManager em, SecurityFilter filter, Inspection inspection) {
		// Access is allowed to this inspection if we have a asset that is linked to its asset
		productExistsLoader.setNetworkId(inspection.getAsset().getNetworkId());
		
		boolean accessAllowed = productExistsLoader.load(em, filter); 
		return accessAllowed;
	}
	
}
