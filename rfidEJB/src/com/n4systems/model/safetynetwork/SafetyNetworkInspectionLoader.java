package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;

import com.n4systems.model.Inspection;
import com.n4systems.model.SubInspection;
import com.n4systems.model.product.ProductWithNetworkIdExistsLoader;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.loaders.NonSecureIdLoader;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.persistence.utils.PostFetcher;

public class SafetyNetworkInspectionLoader extends SecurityFilteredLoader<Inspection> {
	private final NonSecureIdLoader<Inspection> inspectionLoader;
	private final ProductWithNetworkIdExistsLoader productExistsLoader;
	
	public SafetyNetworkInspectionLoader(SecurityFilter filter, NonSecureIdLoader<Inspection> inspectionLoader, ProductWithNetworkIdExistsLoader productExistsLoader) {
		super(filter);
		this.inspectionLoader = inspectionLoader;
		this.productExistsLoader = productExistsLoader;
	}
	
	public SafetyNetworkInspectionLoader(SecurityFilter filter) {
		this(filter, new NonSecureIdLoader<Inspection>(Inspection.class), new ProductWithNetworkIdExistsLoader(filter));
	}

	@Override
	public Inspection load(EntityManager em, SecurityFilter filter) {
		// we want this session to be read-only since enhancement may change fields on the entities
		PersistenceManager.setSessionReadOnly(em);
		
		//we pretty much always need to postfetch all fields
		inspectionLoader.setPostFetchPaths(Inspection.ALL_FIELD_PATHS);
		
		// to load this inspection we will first do an unsecured load by id
		Inspection inspection = inspectionLoader.load(em);
		
		if (inspection == null) {
			return null;
		}
		
		// XXX the following is a hack to post-load the fields for each sub inspection.
		// we need a better way of doing this
		PostFetcher.postFetchFields(inspection.getSubInspections(), SubInspection.ALL_FIELD_PATHS);
		
		// if the inspection is actually one ours, we can stop here
		if (filter.getOwner().allowsAccessFor(inspection.getOwner())) {
			return inspection;
		}
		
		// now we need to ensure we were actually allowed to load this inspection
		// we we will do this by making sure this inspections product is linked to one of
		// our products
		productExistsLoader.setNetworkId(inspection.getProduct().getNetworkId());
		
		if (!productExistsLoader.load(em, filter)) {
			throw new SecurityException("Network inspection failed security check");
		}
		
		// now we need to make sure the inspection is security enhanced
		Inspection enhancedInspection = EntitySecurityEnhancer.enhance(inspection, filter);
		return enhancedInspection;
	}

	public SafetyNetworkInspectionLoader setId(Long id) {
		inspectionLoader.setId(id);
		return this;
	}
}
