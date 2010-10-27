package com.n4systems.model.safetynetwork;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Asset;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.ExternalOrg;
import com.n4systems.model.orgs.InternalOrg;

/**
 * Contains the logic for testing Safety Network security against assets.
 */
public class SafetyNetworkAssetSecurityManager {
	private BaseOrg myOrg;
	
	public SafetyNetworkAssetSecurityManager(BaseOrg myOrg) {
		this.myOrg = myOrg;
	}
	
	/**
	 * Tests if an Asset is linked to myOrg
	 * @see BaseOrg#canAccess(BaseOrg)
	 * @param asset	A asset
	 * @return			true if the Asset is linked and its owner is linked to an Org that is visible to myOrg, false otherwise.
	 */
	public boolean isAssigned(Asset asset) {
		BaseOrg assetOwner = asset.getOwner();
		
		// if the asset's owner is not linked, it couldn't be assigned
		if (!assetOwner.isLinked()) {
			return false;
		}
		
		InternalOrg assetLinkedOrg = ((ExternalOrg)assetOwner).getLinkedOrg();
		
		// now test if the owner that the asset is linked to is one I can see
		return myOrg.canAccess(assetLinkedOrg);
	}
	
	/**
	 * Tests if at least one Asset in the list is assigned to myOrg
	 * @see SafetyNetworkAssetSecurityManager#isAssigned(BaseOrg, com.n4systems.model.Asset)
	 * @param assets	A list of Assets
	 * @return			true if at least one Asset in the list is assigned to myOrg, false otherwise.
	 */
	public boolean listContainsAnAssignedAsset(List<Asset> assets) {
		for (Asset asset : assets) {
			if (isAssigned(asset)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes Assets whose owner is an ExternalOrg and is not assigned to myOrg.  Assets for InternalOrgs are NOT filtered.
	 * @see SafetyNetworkAssetSecurityManager#isAssigned(BaseOrg, com.n4systems.model.Asset)
	 * @param unsecuredAssets	A list of Assets
	 * @return			A list of Assets whose owners are either InteralOrgs or are linked to myOrg
	 */
	public List<Asset> filterOutExternalNotAssignedAssets(List<Asset> unsecuredAssets) {
		List<Asset> securedAssets = new ArrayList<Asset>();

		for (Asset unsecureAsset: unsecuredAssets) {
			
			if (unsecureAsset.getOwner().isInternal()) {
				// InternalOrg are always allowed
				securedAssets.add(unsecureAsset);

			} else if (isAssigned(unsecureAsset)) {
				// External Orgs have to be assigned to me
				securedAssets.add(unsecureAsset);
			}
		}
		
		return securedAssets;
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
