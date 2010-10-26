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
import com.n4systems.services.safetyNetwork.catalog.summary.AssetTypeImportSummary;
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
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;
import com.n4systems.util.ListingPair;

public class CatalogProductTypeImportHandler extends CatalogImportHandler {
	private static final Logger logger = Logger.getLogger(CatalogProductTypeImportHandler.class);
		
	private final LegacyProductType productTypeManager;

	private File copiedAssetImage;
	private AssetType originalType;
	private AssetType importedAssetType;
	private AssetTypeImportSummary summary;
	private Map<Long, AssetTypeGroup> assetGroupMapping;
	private Set<Long> importAssetTypeIds;

	
	public CatalogProductTypeImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog, LegacyProductType productTypeManager) {
		this(persistenceManager, tenant, importCatalog, productTypeManager, new AssetTypeImportSummary());
	}
	 
	public CatalogProductTypeImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog, LegacyProductType productTypeManager, AssetTypeImportSummary summary) {
		super(persistenceManager, tenant, importCatalog);
		this.productTypeManager = productTypeManager;
		this.summary = summary;

	}

	public void importCatalog() throws ImportFailureException {
		getSummaryForImport(importAssetTypeIds);

		importAssetTypeIds.addAll(findMasterAssetRequirements(importAssetTypeIds));
		for (Long assetType : importAssetTypeIds) {
			importAssetType(assetType);
		}

		configureMasterProducts(importAssetTypeIds);
	}

	public void configureMasterProducts(Set<Long> importAssetTypeIds) throws ImportFailureException{
		for (Long assetTypeId : importAssetTypeIds) {
			try {
				List<Long> subAssets = importCatalog.getAllPublishedSubTypesFor(assetTypeId);
				if (!subAssets.isEmpty()) {
					AssetType importedType = summary.getImportMapping().get(assetTypeId);
					for (Long subAssetId : subAssets) {
						if (summary.getImportMapping().get(subAssetId) != null) {
							importedType.getSubTypes().add(summary.getImportMapping().get(subAssetId));
						}
					}
					summary.getImportMapping().put(assetTypeId, persistenceManager.update(importedType));
				}
			} catch (Exception e) {
				summary.setFailure(summary.getImportMapping().get(assetTypeId).getName(), FailureType.COULD_NOT_CONNECT_SUB_PRODUCT,e);
				throw new ImportFailureException(e);
			}
		}
	}

	private void importAssetType(Long assetTypeId) throws ImportFailureException {
		originalType = importCatalog.getPublishedAssetType(assetTypeId, "infoFields");
		importedAssetType = importCatalog.getPublishedAssetType(assetTypeId, "infoFields");

		prepareAssetImageForImport();
		copyAssetType(importedAssetType);
		setUniqueName();
		applyGroup();
		setImage();

		try {
			saveAsset();
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

	private void saveAsset() throws FileAttachmentException, ImageAttachmentException {
		importedAssetType = productTypeManager.updateProductType(importedAssetType, null, copiedAssetImage);
		summary.getImportMapping().put(originalType.getId(), importedAssetType);
	}

	private void setImage() {
		if (copiedAssetImage != null) {
			importedAssetType.setImageName(copiedAssetImage.getName());
		}
	}

	private void applyGroup() {
		if (originalType.getGroup() != null) {
			importedAssetType.setGroup(assetGroupMapping.get(originalType.getGroup().getId()));
		}
	}

	private void setUniqueName() {
		importedAssetType.setName(createUniqueAssetTypeName(importedAssetType.getName()));
	}

	private AssetType copyAssetType(AssetType originalType) {
		return new CleanProductTypeFactory(originalType, tenant).clean();
	}

	private void prepareAssetImageForImport() {
		try {
			if (importedAssetType.hasImage()) {
				File productTypeDirectory = PathHandler.getProductTypeImageFile(importedAssetType);
				File imageFile = new File(productTypeDirectory.getAbsolutePath() + '/' + importedAssetType.getImageName());

				File tmpDirectory = PathHandler.getTempRoot();
				copiedAssetImage = new File(tmpDirectory.getAbsolutePath() + '/' + UUID.randomUUID().toString() + "/" + importedAssetType.getImageName());
				FileUtils.copyFile(imageFile, copiedAssetImage);

			} else {
				copiedAssetImage = null;
			}
		} catch (IOException e) {
			copiedAssetImage = null;

		}
	}

	private String createUniqueAssetTypeName(String assetTypeName) {
		if (!persistenceManager.uniqueNameAvailable(AssetType.class, assetTypeName, null, tenant.getId())) {
			int namePostFix = 1;
			assetTypeName += " (" + importCatalog.getTenant().getName() + ")";
			String candidateAssetTypeName = assetTypeName;
			while (!persistenceManager.uniqueNameAvailable(AssetType.class, candidateAssetTypeName, null, tenant.getId())) {
				candidateAssetTypeName = assetTypeName + "(" + namePostFix + ")";
				namePostFix++;
			}
			summary.renamed();
			return candidateAssetTypeName;
		}
		return assetTypeName;
	}

	public Set<Long> getAdditionalAssetTypes(Set<Long> assetTypeIds) {
		return findMasterAssetRequirements(assetTypeIds);
	}

	public AssetTypeImportSummary getSummaryForImport(Set<Long> assetTypeIds) {
		if (assetTypeIds == null) {
			throw new RuntimeException();
		}

		assetTypeIds.addAll(findMasterAssetRequirements(assetTypeIds));

		if (!assetTypeIds.isEmpty()) {
			findAutoAttributeCount(assetTypeIds);
			findAssetTypeTargetNames(assetTypeIds);
		}

		return summary;
	}

	private void findAssetTypeTargetNames(Set<Long> productTypeIds) {
		List<ListingPair> assetTypesLP = importCatalog.getPublishedAssetTypesLP();
		for (ListingPair assetType : assetTypesLP) {
			if (productTypeIds.contains(assetType.getId())) {
				summary.getImportMapping().put(assetType.getId(), new AssetType(createUniqueAssetTypeName(assetType.getName())));
			}
		}
	}

	private void findAutoAttributeCount(Set<Long> assetTypeIds) {
		for (Long assetTypeId : assetTypeIds) {
			summary.setAutoAttributeCountFor(assetTypeId, importCatalog.getAutoAttributeCountFor(assetTypeId));
		}
	}

	public AssetType getOriginalType() {
		return originalType;
	}

	public AssetType getImportedAssetType() {
		return importedAssetType;
	}

	private Set<Long> findMasterAssetRequirements(Set<Long> assetTypeIds) {
		Set<Long> additionalAssetTypes = new HashSet<Long>();
		if (!assetTypeIds.isEmpty()) {
			additionalAssetTypes.addAll(importCatalog.getAllPublishedSubTypesFor(assetTypeIds));
			additionalAssetTypes.removeAll(assetTypeIds);
		}
		return additionalAssetTypes;
	}

	public Map<Long, AssetType> getImportedMap() {
		return summary.getImportMapping();
	}

	public void rollback() {
		rollbackMasterAssetConfiguration();
		rollbackAssetType();
	}

	private void rollbackAssetType() {
		for (AssetType assetTypeToDelete : summary.getImportMapping().values()) {
			try {
				new CatalogAutoAttributesImportHandler(persistenceManager, tenant, importCatalog).setImportedProductType(assetTypeToDelete).rollback();
				persistenceManager.delete(assetTypeToDelete);
			} catch (Exception e) {
				logger.error("failed to delete asset durning rollback");
			}
		}
	}

	private void rollbackMasterAssetConfiguration() {
		for (AssetType assetTypeToClearSubAssets : summary.getImportMapping().values()) {
			try { 
				assetTypeToClearSubAssets.getSubTypes().clear();
				persistenceManager.update(assetTypeToClearSubAssets);
			} catch (Exception e) {
				logger.error("failed to clean asset durning rollback", e);
			}
		}
	}

	public CatalogProductTypeImportHandler setAssetGroupMapping(Map<Long, AssetTypeGroup> assetGroupMapping) {
		this.assetGroupMapping = assetGroupMapping;
		return this;
	}

	public CatalogProductTypeImportHandler setImportAssetTypeIds(Set<Long> importAssetTypeIds) {
		this.importAssetTypeIds = importAssetTypeIds;
		return this;
	}
}
