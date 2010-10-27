package com.n4systems.ejb;

import java.util.List;
import java.util.SortedSet;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.SubAsset;
import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.exceptions.NonUniqueAssetException;
import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.ListingPair;
import com.n4systems.util.AssetRemovalSummary;
import com.n4systems.util.AssetTypeGroupRemovalSummary;
import com.n4systems.util.AssetTypeRemovalSummary;

public interface AssetManager {

	public List<Asset> findAssetByIdentifiers(SecurityFilter filter, String searchValue);

	public List<Asset> findAssetByIdentifiers(SecurityFilter filter, String searchValue, AssetType assetType);

	public Asset findAssetAllFields(Long id, SecurityFilter filter);


	public Asset findAsset(Long id, SecurityFilter filter);

	public Asset findAsset(Long id, SecurityFilter filter, String... postFetchFields);

	public Asset findAssetByGUID(String mobileGUID, SecurityFilter filter);

	public Asset findAssetBySerialNumber(String rawSerialNumber, Long tenantId, Long customerId) throws NonUniqueAssetException;

	public List<Asset> findAssetsByRfidNumber(String rfidNumber, SecurityFilter filter, String... postFetchFields);

	public Asset parentAsset(Asset asset);

	public List<ListingPair> getAllowedSubTypes(SecurityFilter filter, AssetType type);

	public boolean partOfAMasterAsset(Long typeId);

	public Asset archive(Asset asset, User archivedBy) throws UsedOnMasterInspectionException;
	
	public AssetRemovalSummary testArchive(Asset asset);

	public AssetType archive(AssetType assetType, Long archivedBy, String deletingPrefix);
	
	public void removeAsASubAssetType(AssetType assetType, Long archivedBy);
	
	public void removeAssetCodeMappingsThatUse(AssetType assetType);

	public AssetTypeRemovalSummary testArchive(AssetType assetType);
	
	
	/**
	 * Computes a set of {@link InfoFieldBean} names which are common to all the provided {@link com.n4systems.model.AssetType}s.  Name
	 * matches are exact (case sensitive and non-word char sensitive).  The list will be returned sorted ascending 
	 * alpha-numerically.
	 * @param assetTypeIds
	 * @return SortedSet of InfoField names
	 */
	public SortedSet<String> findAllCommonInfoFieldNames(List<Long> assetTypeIds);
	
	public void deleteAssetTypeGroup(AssetTypeGroup group);
	
	public AssetTypeGroupRemovalSummary testDelete(AssetTypeGroup group);
	
	public Asset fillInSubAssetsOnAsset(Asset asset);
	public List<SubAsset> findSubAssetsForAsset(Asset asset);
	
	
	// methods are just here to allow for a single transaction
	
	public Asset mergeAssets(Asset winningAsset, Asset losingAsset, User user);
	 


}
