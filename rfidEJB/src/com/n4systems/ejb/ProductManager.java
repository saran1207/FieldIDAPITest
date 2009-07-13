package com.n4systems.ejb;

import java.util.List;
import java.util.SortedSet;

import javax.ejb.Local;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.NonUniqueProductException;
import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeGroup;
import com.n4systems.model.SubProduct;
import com.n4systems.util.ListingPair;
import com.n4systems.util.ProductRemovalSummary;
import com.n4systems.util.ProductTypeGroupRemovalSummary;
import com.n4systems.util.ProductTypeRemovalSummary;
import com.n4systems.util.SecurityFilter;

@Local
public interface ProductManager {

	public List<Product> findProductByIdentifiers(SecurityFilter filter, String searchValue);

	public List<Product> findProductByIdentifiers(SecurityFilter filter, String searchValue, ProductType productType);

	public Product findProductAllFields(Long id, SecurityFilter filter);

	public Product findProduct(Long uniqueID);

	public Product findProduct(Long id, SecurityFilter filter);

	public Product findProduct(Long id, SecurityFilter filter, String... postFetchFields);

	public Product findProductByGUID(String mobileGUID, SecurityFilter filter);

	public Product findProductBySerialNumber(String rawSerialNumber, Long tenantId, Long customerId) throws NonUniqueProductException;

	public List<Product> findProductsByRfidNumber(String rfidNumber, SecurityFilter filter, String... postFetchFields);

	public Product parentProduct(Product product);

	public List<ListingPair> getAllowedSubTypes(SecurityFilter filter, ProductType type);

	public boolean partOfAMasterProduct(Long typeId);

	public Product archive(Product product, UserBean archivedBy) throws UsedOnMasterInspectionException;
	
	public ProductRemovalSummary testArchive(Product product);

	public ProductType archive(ProductType productType, Long archivedBy, String deletingPrefix);
	
	public void removeAsASubProductType(ProductType productType, Long archivedBy);
	
	public void removeProductCodeMappingsThatUse(ProductType productType);

	public ProductTypeRemovalSummary testArchive(ProductType productType);
	
	/**
	 * Computes a set of {@link InfoFieldBean} names which are common to all {@link ProductType}s for a Tenant.
	 * @see #findAllCommonInfoFieldNames(List)
	 * @param filter	A SecurityFilter
	 * @return			SortedSet of InfoField names
	 */
	public SortedSet<String> findAllCommonInfoFieldNames(SecurityFilter filter);
	
	/**
	 * Computes a set of {@link InfoFieldBean} names which are common to all the provided {@link ProductType}s.  Name
	 * matches are exact (case sensitive and non-word char sensitive).  The list will be returned sorted ascending 
	 * alpha-numerically.
	 * @param productTypes	A List of ProductTypes
	 * @return				SortedSet of InfoField names
	 */
	public SortedSet<String> findAllCommonInfoFieldNames(List<ProductType> productTypes);
	
	public void deleteProductTypeGroup(ProductTypeGroup group);
	
	public ProductTypeGroupRemovalSummary testDelete(ProductTypeGroup group);
	
	public Product fillInSubProductsOnProduct(Product product);
	public List<SubProduct> findSubProductsForProduct(Product product);
	
	
	// methods are just here to allow for a single transaction
	
	public Product mergeProducts(Product winningProduct, Product losingProduct, UserBean user);
	 


}
