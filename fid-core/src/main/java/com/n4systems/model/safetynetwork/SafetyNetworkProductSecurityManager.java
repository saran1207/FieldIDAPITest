package com.n4systems.model.safetynetwork;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Asset;
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
	 * Tests if an Asset is linked to myOrg
	 * @see BaseOrg#canAccess(BaseOrg)
	 * @param asset	A asset
	 * @return			true if the Asset is linked and its owner is linked to an Org that is visible to myOrg, false otherwise.
	 */
	public boolean isAssigned(Asset asset) {
		BaseOrg productOwner = asset.getOwner();
		
		// if the products owner is not linked, it couldn't be assigned
		if (!productOwner.isLinked()) {
			return false;
		}
		
		InternalOrg productLinkedOrg = ((ExternalOrg)productOwner).getLinkedOrg();
		
		// now test if the owner that the asset is linked to is one I can see
		return myOrg.canAccess(productLinkedOrg);
	}
	
	/**
	 * Tests if at least one Asset in the list is assigned to myOrg
	 * @see SafetyNetworkProductSecurityManager#isAssigned(BaseOrg, com.n4systems.model.Asset)
	 * @param product	A list of Products
	 * @return			true if at least one Asset in the list is assigned to myOrg, false otherwise.
	 */
	public boolean listContainsAnAssignedProduct(List<Asset> assets) {
		for (Asset asset : assets) {
			if (isAssigned(asset)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes Products whose owner is an ExternalOrg and is not assigned to myOrg.  Products for InternalOrgs are NOT filtered.
	 * @see SafetyNetworkProductSecurityManager#isAssigned(BaseOrg, com.n4systems.model.Asset)
	 * @param product	A list of Products
	 * @return			A list of Products whose owners are either InteralOrgs or are linked to myOrg
	 */
	public List<Asset> filterOutExternalNotAssignedProducts(List<Asset> unsecuredProducts) {
		List<Asset> securedProducts = new ArrayList<Asset>();

		for (Asset unsecureProduct: unsecuredProducts) {
			
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

	public boolean listContainsAnAssetPubliclyPublished(List<Asset> assets) {
		for (Asset asset : assets) {
			if (asset.getOwner().isInternal()) {
				return true;
			}
		}
		return false;
	}
	
}
