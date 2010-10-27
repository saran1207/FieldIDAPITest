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
import com.n4systems.model.assettype.AssetTypeScheduleSaver;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.catalog.summary.AssetTypeRelationshipsImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;

public class CatalogAssetTypeRelationshipsImportHandler extends CatalogImportHandler {

	private Map<Long, AssetType> importedAssetTypeMapping = new HashMap<Long, AssetType>();
	private Map<Long, InspectionType> importedInspectionTypeMapping = new HashMap<Long, InspectionType>();
	private AssetTypeRelationshipsImportSummary summary;
	private AssetTypeScheduleSaver assetTypeScheduleSaver;
	private PrimaryOrg primaryOrg;
	
	public CatalogAssetTypeRelationshipsImportHandler(PersistenceManager persistenceManager, PrimaryOrg primaryOrg, CatalogService importCatalog, AssetTypeScheduleSaver assetTypeScheduleSaver) {
		this(persistenceManager, primaryOrg, importCatalog, assetTypeScheduleSaver, new AssetTypeRelationshipsImportSummary());
	}
	
	public CatalogAssetTypeRelationshipsImportHandler(PersistenceManager persistenceManager, PrimaryOrg primaryOrg, CatalogService importCatalog, AssetTypeScheduleSaver assetTypeScheduleSaver, AssetTypeRelationshipsImportSummary summary) {
		super(persistenceManager, primaryOrg.getTenant(), importCatalog);
		this.summary = summary;
		this.assetTypeScheduleSaver = assetTypeScheduleSaver;
		this.primaryOrg = primaryOrg;
	}
	
	
	public void importCatalog() throws ImportFailureException {
		for (Long assetTypeId : importedAssetTypeMapping.keySet()) {
			importAssetTypeRelationships(importCatalog.getPublishedAssetType(assetTypeId, "inspectionTypes" , "schedules"));
		}
	}
	@SuppressWarnings("deprecation")
	public void importAssetTypeRelationships(AssetType originalAssetType) throws ImportFailureException {
		AssetType importedAssetType = importedAssetTypeMapping.get(originalAssetType.getId());
		try {
			for (InspectionType connectedInspectionType : originalAssetType.getInspectionTypes()) {
				importConnectionsToInspectionTypes(originalAssetType, importedAssetType, connectedInspectionType);
				persistenceManager.update(importedAssetType);
			}
		} catch (Exception e) {
			summary.setFailure(importedAssetType.getName(), FailureType.COULD_NOT_LINK_ASSET_TYPE_TO_INSPECTION_TYPE, e);
			throw new ImportFailureException(e);
		}
	}


	private void importConnectionsToInspectionTypes(AssetType originalAssetType, AssetType importedAssetType, InspectionType connectedInspectionType) {
		if (importedInspectionTypeMapping.get(connectedInspectionType.getId()) != null) {
			persistenceManager.save(new AssociatedInspectionType(importedInspectionTypeMapping.get(connectedInspectionType.getId()), importedAssetType));
			importAssetTypeSchedules(originalAssetType, importedAssetType, connectedInspectionType);
		}
	}


	private void importAssetTypeSchedules(AssetType originalAssetType, AssetType importedAssetType, InspectionType connectedInspectionType) {
		if (originalAssetType.getDefaultSchedule(connectedInspectionType) != null) {
			AssetTypeSchedule originalSchedule = originalAssetType.getDefaultSchedule(connectedInspectionType);
			
			AssetTypeSchedule importedSchedule = copyAssetTypeSchedule(connectedInspectionType, originalSchedule);
			
			assetTypeScheduleSaver.save(importedSchedule);
		}
	}

	private AssetTypeSchedule copyAssetTypeSchedule(InspectionType connectedInspectionType, AssetTypeSchedule originalSchedule) {
		AssetTypeSchedule importedSchedule = new AssetTypeSchedule();
		importedSchedule.setAutoSchedule(originalSchedule.isAutoSchedule());
		importedSchedule.setFrequency(originalSchedule.getFrequency());
		importedSchedule.setInspectionType(importedInspectionTypeMapping.get(connectedInspectionType.getId()));
		importedSchedule.setAssetType(importedAssetTypeMapping.get(originalSchedule.getAssetType().getId()));
		importedSchedule.setOwner(primaryOrg);
		importedSchedule.setTenant(tenant);
		return importedSchedule;
	}
	
	
	public Set<Long> getAdditionalInspectionTypesForRelationships(Set<Long> assetTypeIds, Set<Long> inspectionTypeIds) {
		Set<Long> additionalInspectionTypeIds = new HashSet<Long>();
		if (!assetTypeIds.isEmpty()) {
			additionalInspectionTypeIds.addAll(importCatalog.getPublishedInspectionTypeIdsConnectedTo(assetTypeIds));
			additionalInspectionTypeIds.removeAll(inspectionTypeIds);
		}
		
		return additionalInspectionTypeIds;
	}
	
	// there is nothing that can be directly undone here the other elements need to removed.
	public void rollback() {
	}


	public CatalogAssetTypeRelationshipsImportHandler setImportedAssetTypeMapping(Map<Long, AssetType> importedAssetTypeMapping) {
		this.importedAssetTypeMapping = importedAssetTypeMapping;
		return this;
	}


	public CatalogAssetTypeRelationshipsImportHandler setImportedInspectionTypeMapping(Map<Long, InspectionType> importedInspectionTypeMapping) {
		this.importedInspectionTypeMapping = importedInspectionTypeMapping;
		return this;
	}
	
}
