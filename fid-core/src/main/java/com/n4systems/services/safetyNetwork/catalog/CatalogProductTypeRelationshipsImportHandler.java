package com.n4systems.services.safetyNetwork.catalog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.producttype.ProductTypeScheduleSaver;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.catalog.summary.ProductTypeRelationshipsImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;

public class CatalogProductTypeRelationshipsImportHandler extends CatalogImportHandler {

	private Map<Long, AssetType> importedProductTypeMapping = new HashMap<Long, AssetType>();
	private Map<Long, InspectionType> importedInspectionTypeMapping = new HashMap<Long, InspectionType>();
	private ProductTypeRelationshipsImportSummary summary;
	private ProductTypeScheduleSaver productTypeScheduleSaver;
	private PrimaryOrg primaryOrg;
	
	public CatalogProductTypeRelationshipsImportHandler(PersistenceManager persistenceManager, PrimaryOrg primaryOrg, CatalogService importCatalog, ProductTypeScheduleSaver productTypeScheduleSaver) {
		this(persistenceManager, primaryOrg, importCatalog, productTypeScheduleSaver, new ProductTypeRelationshipsImportSummary());
	}
	
	public CatalogProductTypeRelationshipsImportHandler(PersistenceManager persistenceManager, PrimaryOrg primaryOrg, CatalogService importCatalog, ProductTypeScheduleSaver productTypeScheduleSaver, ProductTypeRelationshipsImportSummary summary) {
		super(persistenceManager, primaryOrg.getTenant(), importCatalog);
		this.summary = summary;
		this.productTypeScheduleSaver = productTypeScheduleSaver;
		this.primaryOrg = primaryOrg;
	}
	
	
	public void importCatalog() throws ImportFailureException {
		for (Long productTypeId : importedProductTypeMapping.keySet()) {
			importProductTypeRelationships(importCatalog.getPublishedProductType(productTypeId, "inspectionTypes" , "schedules"));
		}
	}
	@SuppressWarnings("deprecation")
	public void importProductTypeRelationships(AssetType originalAssetType) throws ImportFailureException {
		AssetType importedAssetType = importedProductTypeMapping.get(originalAssetType.getId());
		try {
			for (InspectionType connectedInspectionType : originalAssetType.getInspectionTypes()) {
				importConnectionsToInspectionTypes(originalAssetType, importedAssetType, connectedInspectionType);
				persistenceManager.update(importedAssetType);
			}
		} catch (Exception e) {
			summary.setFailure(importedAssetType.getName(), FailureType.COULD_NOT_LINK_PRODUCT_TYPE_TO_INSPECTION_TYPE, e);
			throw new ImportFailureException(e);
		}
	}


	private void importConnectionsToInspectionTypes(AssetType originalAssetType, AssetType importedAssetType, InspectionType connectedInspectionType) {
		if (importedInspectionTypeMapping.get(connectedInspectionType.getId()) != null) {
			persistenceManager.save(new AssociatedInspectionType(importedInspectionTypeMapping.get(connectedInspectionType.getId()), importedAssetType));
			importProductTypeSchedules(originalAssetType, importedAssetType, connectedInspectionType);
		}
	}


	private void importProductTypeSchedules(AssetType originalAssetType, AssetType importedAssetType, InspectionType connectedInspectionType) {
		if (originalAssetType.getDefaultSchedule(connectedInspectionType) != null) {
			AssetTypeSchedule originalSchedule = originalAssetType.getDefaultSchedule(connectedInspectionType);
			
			AssetTypeSchedule importedSchedule = copyProductTypeSchedule(connectedInspectionType, originalSchedule);
			
			productTypeScheduleSaver.save(importedSchedule);
		}
	}

	private AssetTypeSchedule copyProductTypeSchedule(InspectionType connectedInspectionType, AssetTypeSchedule originalSchedule) {
		AssetTypeSchedule importedSchedule = new AssetTypeSchedule();
		importedSchedule.setAutoSchedule(originalSchedule.isAutoSchedule());
		importedSchedule.setFrequency(originalSchedule.getFrequency());
		importedSchedule.setInspectionType(importedInspectionTypeMapping.get(connectedInspectionType.getId()));
		importedSchedule.setAssetType(importedProductTypeMapping.get(originalSchedule.getAssetType().getId()));
		importedSchedule.setOwner(primaryOrg);
		importedSchedule.setTenant(tenant);
		return importedSchedule;
	}
	
	
	public Set<Long> getAdditionalInspectionTypesForRelationships(Set<Long> productTypeIds, Set<Long> inspectionTypeIds) {
		Set<Long> additionalInspectionTypeIds = new HashSet<Long>();
		if (!productTypeIds.isEmpty()) {
			additionalInspectionTypeIds.addAll(importCatalog.getPublishedInspectionTypeIdsConnectedTo(productTypeIds));
			additionalInspectionTypeIds.removeAll(inspectionTypeIds);
		}
		
		return additionalInspectionTypeIds;
	}
	
	// there is nothing that can be directly undone here the other elements need to removed.
	public void rollback() {
	}


	public CatalogProductTypeRelationshipsImportHandler setImportedProductTypeMapping(Map<Long, AssetType> importedProductTypeMapping) {
		this.importedProductTypeMapping = importedProductTypeMapping;
		return this;
	}


	public CatalogProductTypeRelationshipsImportHandler setImportedInspectionTypeMapping(Map<Long, InspectionType> importedInspectionTypeMapping) {
		this.importedInspectionTypeMapping = importedInspectionTypeMapping;
		return this;
	}
	
}
