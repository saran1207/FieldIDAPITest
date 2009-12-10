package com.n4systems.model.safetynetwork;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Product;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.ExternalOrg;
import com.n4systems.model.orgs.InternalOrg;

/**
 * Contains the logic for testing Safety Network security against products.
 */
public class SafetyNetworkProductSecurityManager {
	private BaseOrg myOrg;
	
	public SafetyNetworkProductSecurityManager(BaseOrg myOrg) {
		this.myOrg = myOrg;
	}
	
	/**
	 * Tests if a Product is linked to myOrg
	 * @see BaseOrg#canAccess(BaseOrg)
	 * @param product	A product
	 * @return			true if the Product is linked and its owner is linked to an Org that is visible to myOrg, false otherwise.
	 */
	public boolean isAssigned(Product product) {
		BaseOrg productOwner = product.getOwner();
		
		// if the products owner is not linked, it couldn't be assigned
		if (!productOwner.isLinked()) {
			return false;
		}
		
		InternalOrg productLinkedOrg = ((ExternalOrg)productOwner).getLinkedOrg();
		
		// now test if the owner that the product is linked to is one I can see
		return myOrg.canAccess(productLinkedOrg);
	}
	
	/**
	 * Tests if at least one Product in the list is assigned to myOrg
	 * @see SafetyNetworkProductSecurityManager#isAssigned(BaseOrg, Product)
	 * @param product	A list of Products
	 * @return			true if at least one Product in the list is assigned to myOrg, false otherwise.
	 */
	public boolean listContainsAnAssignedProduct(List<Product> products) {
		for (Product product: products) {
			if (isAssigned(product)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes Products whose owner is an ExternalOrg and is not assigned to myOrg.  Products for InternalOrgs are NOT filtered.
	 * @see SafetyNetworkProductSecurityManager#isAssigned(BaseOrg, Product)
	 * @param product	A list of Products
	 * @return			A list of Products whose owners are either InteralOrgs or are linked to myOrg
	 */
	public List<Product> filterOutExternalNotAssignedProducts(List<Product> unsecuredProducts) {
		List<Product> securedProducts = new ArrayList<Product>();

		for (Product unsecureProduct: unsecuredProducts) {
			
			if (unsecureProduct.getOwner().isInternal()) {
				// InternalOrg are always allowed
				securedProducts.add(unsecureProduct);				

			} else if (isAssigned(unsecureProduct)) {
				// External Orgs have to be assigned to me
				securedProducts.add(unsecureProduct);
			}
		}
		
		return securedProducts;
	}
	
}
