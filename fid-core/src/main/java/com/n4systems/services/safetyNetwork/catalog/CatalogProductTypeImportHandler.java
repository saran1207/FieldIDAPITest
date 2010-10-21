package com.n4systems.services.safetyNetwork.catalog;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.model.Tenant;
import com.n4systems.model.utils.CleanProductTypeFactory;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.catalog.summary.ProductTypeImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;
import com.n4systems.util.ListingPair;

public class CatalogProductTypeImportHandler extends CatalogImportHandler {
	private static final Logger logger = Logger.getLogger(CatalogProductTypeImportHandler.class);
		
	private final LegacyProductType productTypeManager;

	private File copiedProductImage;
	private AssetType originalType;
	private AssetType importedAssetType;
	private ProductTypeImportSummary summary;
	private Map<Long, AssetTypeGroup> productGroupMapping;
	private Set<Long> importProductTypeIds;

	
	public CatalogProductTypeImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog, LegacyProductType productTypeManager) {
		this(persistenceManager, tenant, importCatalog, productTypeManager, new ProductTypeImportSummary());
	}
	 
	public CatalogProductTypeImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog, LegacyProductType productTypeManager, ProductTypeImportSummary summary) {
		super(persistenceManager, tenant, importCatalog);
		this.productTypeManager = productTypeManager;
		this.summary = summary;

	}

	public void importCatalog() throws ImportFailureException {
		getSummaryForImport(importProductTypeIds);

		importProductTypeIds.addAll(findMasterProductRequirements(importProductTypeIds));
		for (Long importProductType : importProductTypeIds) {
			importProductType(importProductType);
		}

		configureMasterProducts(importProductTypeIds);
	}

	public void configureMasterProducts(Set<Long> importProductTypeIds) throws ImportFailureException{
		for (Long productTypeId : importProductTypeIds) {
			try {
				List<Long> subProducts = importCatalog.getAllPublishedSubTypesFor(productTypeId);
				if (!subProducts.isEmpty()) {
					AssetType importedType = summary.getImportMapping().get(productTypeId);
					for (Long subProductId : subProducts) {
						if (summary.getImportMapping().get(subProductId) != null) {
							importedType.getSubTypes().add(summary.getImportMapping().get(subProductId));
						}
					}
					summary.getImportMapping().put(productTypeId, persistenceManager.update(importedType));
				}
			} catch (Exception e) {
				summary.setFailure(summary.getImportMapping().get(productTypeId).getName(), FailureType.COULD_NOT_CONNECT_SUB_PRODUCT,e);
				throw new ImportFailureException(e);
			}
		}
	}

	private void importProductType(Long productTypeId) throws ImportFailureException {
		originalType = importCatalog.getPublishedProductType(productTypeId, "infoFields");
		importedAssetType = importCatalog.getPublishedProductType(productTypeId, "infoFields");

		prepareProductImageForImport();
		copyProductType(importedAssetType);
		setUniqueName();
		applyGroup();
		setImage();

		try {
			saveProduct();
			try {
				processAutoattributes();
			} catch (Exception e) {
				summary.setFailure(originalType.getName(), FailureType.COULD_NOT_CREATE_AUTOATTRIBUTE, e);
				throw new ImportFailureException(e);
			}
		} catch (Exception e) {
			summary.setFailure(originalType.getName(), FailureType.COULD_NOT_CREATE, e);
			throw new ImportFailureException(e);
		}
	}

	private void processAutoattributes() throws ImportFailureException {
		new CatalogAutoAttributesImportHandler(persistenceManager, tenant, importCatalog).setOriginalType(originalType).setImportedProductType(importedAssetType).importCatalog();
	}

	private void saveProduct() throws FileAttachmentException, ImageAttachmentException {
		importedAssetType = productTypeManager.updateProductType(importedAssetType, null, copiedProductImage);
		summary.getImportMapping().put(originalType.getId(), importedAssetType);
	}

	private void setImage() {
		if (copiedProductImage != null) {
			importedAssetType.setImageName(copiedProductImage.getName());
		}
	}

	private void applyGroup() {
		if (originalType.getGroup() != null) {
			importedAssetType.setGroup(productGroupMapping.get(originalType.getGroup().getId()));
		}
	}

	private void setUniqueName() {
		importedAssetType.setName(createUniqueProductTypeName(importedAssetType.getName()));
	}

	private AssetType copyProductType(AssetType originalType) {
		return new CleanProductTypeFactory(originalType, tenant).clean();
	}

	private void prepareProductImageForImport() {
		try {
			if (importedAssetType.hasImage()) {
				File productTypeDirectory = PathHandler.getProductTypeImageFile(importedAssetType);
				File imageFile = new File(productTypeDirectory.getAbsolutePath() + '/' + importedAssetType.getImageName());

				File tmpDirectory = PathHandler.getTempRoot();
				copiedProductImage = new File(tmpDirectory.getAbsolutePath() + '/' + UUID.randomUUID().toString() + "/" + importedAssetType.getImageName());
				FileUtils.copyFile(imageFile, copiedProductImage);

			} else {
				copiedProductImage = null;
			}
		} catch (IOException e) {
			copiedProductImage = null;

		}
	}

	private String createUniqueProductTypeName(String productTypeName) {
		if (!persistenceManager.uniqueNameAvailable(AssetType.class, productTypeName, null, tenant.getId())) {
			int namePostFix = 1;
			productTypeName += " (" + importCatalog.getTenant().getName() + ")";
			String tmpProductTypeName = productTypeName; 
			while (!persistenceManager.uniqueNameAvailable(AssetType.class, tmpProductTypeName, null, tenant.getId())) {
				tmpProductTypeName = productTypeName + "(" + namePostFix + ")";
				namePostFix++;
			}
			summary.renamed();
			return tmpProductTypeName;
		}
		return productTypeName;
	}

	public Set<Long> getAdditionalProductTypes(Set<Long> productTypeIds) {
		return findMasterProductRequirements(productTypeIds);
	}

	public ProductTypeImportSummary getSummaryForImport(Set<Long> productTypeIds) {
		if (productTypeIds == null) {
			throw new RuntimeException();
		}

		productTypeIds.addAll(findMasterProductRequirements(productTypeIds));

		if (!productTypeIds.isEmpty()) {
			findAutoAttributeCount(productTypeIds);
			findProductTypeTargetNames(productTypeIds);
		}

		return summary;
	}

	private void findProductTypeTargetNames(Set<Long> productTypeIds) {
		List<ListingPair> productTypes = importCatalog.getPublishedProductTypesLP();
		for (ListingPair productType : productTypes) {
			if (productTypeIds.contains(productType.getId())) {
				summary.getImportMapping().put(productType.getId(), new AssetType(createUniqueProductTypeName(productType.getName())));
			}
		}
	}

	private void findAutoAttributeCount(Set<Long> productTypeIds) {
		for (Long productTypeId : productTypeIds) {
			summary.setAutoAttributeCountFor(productTypeId, importCatalog.getAutoAttributeCountFor(productTypeId));
		}
	}

	public AssetType getOriginalType() {
		return originalType;
	}

	public AssetType getImportedProductType() {
		return importedAssetType;
	}

	private Set<Long> findMasterProductRequirements(Set<Long> productTypeIds) {
		Set<Long> additionalProductTypes = new HashSet<Long>();
		if (!productTypeIds.isEmpty()) {
			additionalProductTypes.addAll(importCatalog.getAllPublishedSubTypesFor(productTypeIds));
			additionalProductTypes.removeAll(productTypeIds);
		}
		return additionalProductTypes;
	}

	public Map<Long, AssetType> getImportedMap() {
		return summary.getImportMapping();
	}

	public void rollback() {
		rollbackMasterProductConfiguration();
		rollbackProductType();
	}

	private void rollbackProductType() {
		for (AssetType assetTypeToDelete : summary.getImportMapping().values()) {
			try {
				new CatalogAutoAttributesImportHandler(persistenceManager, tenant, importCatalog).setImportedProductType(assetTypeToDelete).rollback();
				persistenceManager.delete(assetTypeToDelete);
			} catch (Exception e) {
				logger.error("failed to delete asset durning rollback");
			}
		}
	}

	private void rollbackMasterProductConfiguration() {
		for (AssetType assetTypeToClearSubProducts : summary.getImportMapping().values()) {
			try { 
				assetTypeToClearSubProducts.getSubTypes().clear();
				persistenceManager.update(assetTypeToClearSubProducts);
			} catch (Exception e) {
				logger.error("failed to clean asset durning rollback", e);
			}
		}
	}

	public CatalogProductTypeImportHandler setProductGroupMapping(Map<Long, AssetTypeGroup> productGroupMapping) {
		this.productGroupMapping = productGroupMapping;
		return this;
	}

	public CatalogProductTypeImportHandler setImportProductTypeIds(Set<Long> importProductTypeIds) {
		this.importProductTypeIds = importProductTypeIds;
		return this;
	}
}
