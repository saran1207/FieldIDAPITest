package com.n4systems.services.safetyNetwork.catalog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.assettype.AssetTypeScheduleSaver;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.catalog.summary.AssetTypeRelationshipsImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;

public class CatalogAssetTypeRelationshipsImportHandler extends CatalogImportHandler {

	private Map<Long, AssetType> importedAssetTypeMapping = new HashMap<Long, AssetType>();
	private Map<Long, EventType> importedInspectionTypeMapping = new HashMap<Long, EventType>();
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
			for (EventType connectedEventType : originalAssetType.getEventTypes()) {
				importConnectionsToInspectionTypes(originalAssetType, importedAssetType, connectedEventType);
				persistenceManager.update(importedAssetType);
			}
		} catch (Exception e) {
			summary.setFailure(importedAssetType.getName(), FailureType.COULD_NOT_LINK_ASSET_TYPE_TO_INSPECTION_TYPE, e);
			throw new ImportFailureException(e);
		}
	}


	private void importConnectionsToInspectionTypes(AssetType originalAssetType, AssetType importedAssetType, EventType connectedEventType) {
		if (importedInspectionTypeMapping.get(connectedEventType.getId()) != null) {
			persistenceManager.save(new AssociatedEventType(importedInspectionTypeMapping.get(connectedEventType.getId()), importedAssetType));
			importAssetTypeSchedules(originalAssetType, importedAssetType, connectedEventType);
		}
	}


	private void importAssetTypeSchedules(AssetType originalAssetType, AssetType importedAssetType, EventType connectedEventType) {
		if (originalAssetType.getDefaultSchedule(connectedEventType) != null) {
			AssetTypeSchedule originalSchedule = originalAssetType.getDefaultSchedule(connectedEventType);
			
			AssetTypeSchedule importedSchedule = copyAssetTypeSchedule(connectedEventType, originalSchedule);
			
			assetTypeScheduleSaver.save(importedSchedule);
		}
	}

	private AssetTypeSchedule copyAssetTypeSchedule(EventType connectedEventType, AssetTypeSchedule originalSchedule) {
		AssetTypeSchedule importedSchedule = new AssetTypeSchedule();
		importedSchedule.setAutoSchedule(originalSchedule.isAutoSchedule());
		importedSchedule.setFrequency(originalSchedule.getFrequency());
		importedSchedule.setEventType(importedInspectionTypeMapping.get(connectedEventType.getId()));
		importedSchedule.setAssetType(importedAssetTypeMapping.get(originalSchedule.getAssetType().getId()));
		importedSchedule.setOwner(primaryOrg);
		importedSchedule.setTenant(tenant);
		return importedSchedule;
	}
	
	
	public Set<Long> getAdditionalInspectionTypesForRelationships(Set<Long> assetTypeIds, Set<Long> inspectionTypeIds) {
		Set<Long> additionalInspectionTypeIds = new HashSet<Long>();
		if (!assetTypeIds.isEmpty()) {
			additionalInspectionTypeIds.addAll(importCatalog.getPublishedEventTypeIdsConnectedTo(assetTypeIds));
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


	public CatalogAssetTypeRelationshipsImportHandler setImportedInspectionTypeMapping(Map<Long, EventType> importedInspectionTypeMapping) {
		this.importedInspectionTypeMapping = importedInspectionTypeMapping;
		return this;
	}
	
}
