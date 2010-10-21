package com.n4systems.ejb;

import java.util.List;
import java.util.SortedSet;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.exceptions.NonUniqueProductException;
import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.model.SubProduct;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.ListingPair;
import com.n4systems.util.ProductRemovalSummary;
import com.n4systems.util.ProductTypeGroupRemovalSummary;
import com.n4systems.util.ProductTypeRemovalSummary;

public interface ProductManager {

	public List<Asset> findProductByIdentifiers(SecurityFilter filter, String searchValue);

	public List<Asset> findProductByIdentifiers(SecurityFilter filter, String searchValue, AssetType assetType);

	public Asset findProductAllFields(Long id, SecurityFilter filter);


	public Asset findProduct(Long id, SecurityFilter filter);

	public Asset findProduct(Long id, SecurityFilter filter, String... postFetchFields);

	public Asset findProductByGUID(String mobileGUID, SecurityFilter filter);

	public Asset findProductBySerialNumber(String rawSerialNumber, Long tenantId, Long customerId) throws NonUniqueProductException;

	public List<Asset> findProductsByRfidNumber(String rfidNumber, SecurityFilter filter, String... postFetchFields);

	public Asset parentProduct(Asset asset);

	public List<ListingPair> getAllowedSubTypes(SecurityFilter filter, AssetType type);

	public boolean partOfAMasterProduct(Long typeId);

	public Asset archive(Asset asset, User archivedBy) throws UsedOnMasterInspectionException;
	
	public ProductRemovalSummary testArchive(Asset asset);

	public AssetType archive(AssetType assetType, Long archivedBy, String deletingPrefix);
	
	public void removeAsASubProductType(AssetType assetType, Long archivedBy);
	
	public void removeProductCodeMappingsThatUse(AssetType assetType);

	public ProductTypeRemovalSummary testArchive(AssetType assetType);
	
	
	/**
	 * Computes a set of {@link InfoFieldBean} names which are common to all the provided {@link com.n4systems.model.AssetType}s.  Name
	 * matches are exact (case sensitive and non-word char sensitive).  The list will be returned sorted ascending 
	 * alpha-numerically.
	 * @param productTypeIds 
	 * @return SortedSet of InfoField names
	 */
	public SortedSet<String> findAllCommonInfoFieldNames(List<Long> productTypeIds);
	
	public void deleteProductTypeGroup(AssetTypeGroup group);
	
	public ProductTypeGroupRemovalSummary testDelete(AssetTypeGroup group);
	
	public Asset fillInSubProductsOnProduct(Asset asset);
	public List<SubProduct> findSubProductsForProduct(Asset asset);
	
	
	// methods are just here to allow for a single transaction
	
	public Asset mergeProducts(Asset winningAsset, Asset losingAsset, User user);
	 


}
