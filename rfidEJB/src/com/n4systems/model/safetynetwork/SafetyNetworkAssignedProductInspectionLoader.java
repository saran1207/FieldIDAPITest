package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Inspection;
import com.n4systems.model.Product;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.NonSecureIdLoader;

public class SafetyNetworkAssignedProductInspectionLoader extends SafetyNetworkInspectionLoader {
	private final ProductsByNetworkId productsByNetworkIdLoader;

	public SafetyNetworkAssignedProductInspectionLoader(SecurityFilter filter, NonSecureIdLoader<Inspection> inspectionLoader, ProductsByNetworkId productsByNetworkIdLoader) {
		super(filter, inspectionLoader);
		this.productsByNetworkIdLoader = productsByNetworkIdLoader;
	}
	
	public SafetyNetworkAssignedProductInspectionLoader(SecurityFilter filter) {
		super(filter);
		this.productsByNetworkIdLoader = new ProductsByNetworkId(filter);
	}
	
	@Override
	protected boolean accessAllowed(EntityManager em, SecurityFilter filter, Inspection inspection) {
		SafetyNetworkProductSecurityManager securityManager = new SafetyNetworkProductSecurityManager(filter.getOwner());
		
		Product inspectionProduct = inspection.getProduct();
		
		// if we'er lucky enough to get an inspection for a product which is directly assigned we can stop here
		if (securityManager.isAssigned(inspectionProduct)) {
			return true;
		}
		
		// now we need to collect up all the products with the same network id as the inspections product.
		// then we can see if at least one of them was assigned to me
		productsByNetworkIdLoader.setNetworkId(inspectionProduct.getNetworkId());
		productsByNetworkIdLoader.setExcludeProductId(inspectionProduct.getId());
		
		// there's no need for these to be enhanced since they're just used for a security check
		productsByNetworkIdLoader.bypassSecurityEnhancement();
		
		List<Product> linkedProducts = productsByNetworkIdLoader.load(em, filter);
		boolean hasAssignedProduct = securityManager.listContainsAnAssignedProduct(linkedProducts);
		
		return hasAssignedProduct;
	}
	
}
