package com.n4systems.services.safetyNetwork;

import java.util.Set;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.assettype.AssetTypeScheduleSaver;
import com.n4systems.services.safetyNetwork.catalog.CatalogAssetTypeGroupImportHandler;
import com.n4systems.services.safetyNetwork.catalog.CatalogInspectionTypeGroupHandler;
import com.n4systems.services.safetyNetwork.catalog.CatalogInspectionTypeImportHandler;
import com.n4systems.services.safetyNetwork.catalog.CatalogAssetTypeImportHandler;
import com.n4systems.services.safetyNetwork.catalog.CatalogAssetTypeRelationshipsImportHandler;
import com.n4systems.services.safetyNetwork.catalog.CatalogStateSetsImportHandler;
import com.n4systems.services.safetyNetwork.catalog.summary.CatalogImportSummary;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;


public class ImportCatalogService {

	private final CatalogAssetTypeRelationshipsImportHandler importAssetTypeInspectionTypeRelations;
	private final CatalogAssetTypeGroupImportHandler importAssetTypeGroup;
	private final CatalogAssetTypeImportHandler importAssetType;
	private final CatalogInspectionTypeImportHandler importInspectionType;
	private final CatalogInspectionTypeGroupHandler importInspectionTypeGroup;
	private final CatalogStateSetsImportHandler importStateSets;
	private Set<Long> importAssetTypeIds;
	private Set<Long> importInspectionTypeIds;
	private boolean importAllRelations = false;
	private CatalogImportSummary summary = new CatalogImportSummary();
	
	
	public ImportCatalogService(PersistenceManager persistenceManager, PrimaryOrg primaryOrg, CatalogService importCatalog, LegacyAssetType legacyAssetTypeManager) {
		importAssetType = new CatalogAssetTypeImportHandler(persistenceManager, primaryOrg.getTenant(), importCatalog, legacyAssetTypeManager, summary.getAssetTypeImportSummary());
		importInspectionType = new CatalogInspectionTypeImportHandler(persistenceManager,  primaryOrg.getTenant(), importCatalog, summary.getInspectionTypeImportSummary());
		importInspectionTypeGroup = new CatalogInspectionTypeGroupHandler(persistenceManager,  primaryOrg.getTenant(), importCatalog, summary.getInspectionTypeGroupImportSummary());
		importStateSets = new CatalogStateSetsImportHandler(persistenceManager,  primaryOrg.getTenant(), importCatalog, summary.getStateSetImportSummary());
		importAssetTypeGroup = new CatalogAssetTypeGroupImportHandler(persistenceManager,  primaryOrg.getTenant(), importCatalog, summary.getAssetTypeGroupImportSummary());
		importAssetTypeInspectionTypeRelations = new CatalogAssetTypeRelationshipsImportHandler(persistenceManager, primaryOrg, importCatalog, new AssetTypeScheduleSaver(), summary.getAssetTypeRelationshipsImportSummary());
	}
	
	
	public boolean importSelection() {
		adjustWhatToImport();
		try {
			importCatalog();
			return true;
		} catch (ImportFailureException e) {
			rollback();
			return false;
		}
		
	}


	private void importCatalog() throws ImportFailureException {
		importAssetTypeGroup.setAssetTypeIds(importAssetTypeIds).importCatalog();
		importAssetType.setAssetGroupMapping(importAssetTypeGroup.getImportMapping()).setImportAssetTypeIds(importAssetTypeIds).importCatalog();
		importInspectionTypeGroup.setInspectionTypeIds(importInspectionTypeIds).importCatalog();
		importStateSets.setInspectionTypeIds(importInspectionTypeIds).importCatalog();
		importInspectionType.setOriginalInspectionTypeIds(importInspectionTypeIds).setImportedGroupMapping(importInspectionTypeGroup.getImportMapping()).setImportedStateSetMapping(importStateSets.getImportMapping()).importCatalog();

		if (importAllRelations) {
			importAssetTypeInspectionTypeRelations.setImportedAssetTypeMapping(importAssetType.getImportedMap()).setImportedInspectionTypeMapping(importInspectionType.getImportMapping()).importCatalog();
		}
	}


	private void rollback() {
		importAssetType.rollback();
		importInspectionType.rollback();
		importInspectionTypeGroup.rollback();
		importStateSets.rollback();
		importAssetTypeGroup.rollback();
	}
	
	public CatalogImportSummary importSelectionSummary() {
		adjustWhatToImport();
		importAssetTypeGroup.getSummaryForImport(importAssetTypeIds);
		importAssetType.getSummaryForImport(importAssetTypeIds);
		importInspectionType.getSummaryForImport(importInspectionTypeIds);
		importInspectionTypeGroup.getSummaryForImport(importInspectionTypeIds);
		importStateSets.getSummaryForImport(importInspectionTypeIds);
		return summary;
	}


	private void adjustWhatToImport() {
		importAssetTypeIds.addAll(importAssetType.getAdditionalAssetTypes(importAssetTypeIds));
		if (importAllRelations) {
			importInspectionTypeIds.addAll(importAssetTypeInspectionTypeRelations.getAdditionalInspectionTypesForRelationships(importAssetTypeIds, importInspectionTypeIds));
		}
	}
	
	
	public Set<Long> getImportAssetTypeIds() {
		return importAssetTypeIds;
	}

	public void setImportAssetTypeIds(Set<Long> importAssetTypeIds) {
		this.importAssetTypeIds = importAssetTypeIds;
	}

	public Set<Long> getImportInspectionTypeIds() {
		return importInspectionTypeIds;
	}

	public void setImportInspectionTypeIds(Set<Long> importInspectionTypeIds) {
		this.importInspectionTypeIds = importInspectionTypeIds;
	}


	public void setImportAllRelations(boolean importAllRelations) {
		this.importAllRelations = importAllRelations;
	}


	public CatalogImportSummary getSummary() {
		return summary;
	}

}
