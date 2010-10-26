package com.n4systems.services.safetyNetwork;

import java.util.Set;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.producttype.AssetTypeScheduleSaver;
import com.n4systems.services.safetyNetwork.catalog.CatalogInspectionTypeGroupHandler;
import com.n4systems.services.safetyNetwork.catalog.CatalogInspectionTypeImportHandler;
import com.n4systems.services.safetyNetwork.catalog.CatalogProductTypeGroupImportHandler;
import com.n4systems.services.safetyNetwork.catalog.CatalogProductTypeImportHandler;
import com.n4systems.services.safetyNetwork.catalog.CatalogProductTypeRelationshipsImportHandler;
import com.n4systems.services.safetyNetwork.catalog.CatalogStateSetsImportHandler;
import com.n4systems.services.safetyNetwork.catalog.summary.CatalogImportSummary;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;


public class ImportCatalogService {

	private final CatalogProductTypeRelationshipsImportHandler importProductTypeInspectionTypeRelations;
	private final CatalogProductTypeGroupImportHandler importProductTypeGroup;
	private final CatalogProductTypeImportHandler importProductType;
	private final CatalogInspectionTypeImportHandler importInspectionType;
	private final CatalogInspectionTypeGroupHandler importInspectionTypeGroup;
	private final CatalogStateSetsImportHandler importStateSets;
	private Set<Long> importProductTypeIds;
	private Set<Long> importInspectionTypeIds;
	private boolean importAllRelations = false;
	private CatalogImportSummary summary = new CatalogImportSummary();
	
	
	public ImportCatalogService(PersistenceManager persistenceManager, PrimaryOrg primaryOrg, CatalogService importCatalog, LegacyProductType legacyProductTypeManager) {
		super();
		importProductType = new CatalogProductTypeImportHandler(persistenceManager, primaryOrg.getTenant(), importCatalog, legacyProductTypeManager, summary.getAssetTypeImportSummary());
		importInspectionType = new CatalogInspectionTypeImportHandler(persistenceManager,  primaryOrg.getTenant(), importCatalog, summary.getInspectionTypeImportSummary());
		importInspectionTypeGroup = new CatalogInspectionTypeGroupHandler(persistenceManager,  primaryOrg.getTenant(), importCatalog, summary.getInspectionTypeGroupImportSummary());
		importStateSets = new CatalogStateSetsImportHandler(persistenceManager,  primaryOrg.getTenant(), importCatalog, summary.getStateSetImportSummary());
		importProductTypeGroup = new CatalogProductTypeGroupImportHandler(persistenceManager,  primaryOrg.getTenant(), importCatalog, summary.getAssetTypeGroupImportSummary());
		importProductTypeInspectionTypeRelations = new CatalogProductTypeRelationshipsImportHandler(persistenceManager, primaryOrg, importCatalog, new AssetTypeScheduleSaver(), summary.getProductTypeRelationshipsImportSummary());
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
		importProductTypeGroup.setProductTypeIds(importProductTypeIds).importCatalog();
		importProductType.setAssetGroupMapping(importProductTypeGroup.getImportMapping()).setImportAssetTypeIds(importProductTypeIds).importCatalog();
		importInspectionTypeGroup.setInspectionTypeIds(importInspectionTypeIds).importCatalog();
		importStateSets.setInspectionTypeIds(importInspectionTypeIds).importCatalog();
		importInspectionType.setOriginalInspectionTypeIds(importInspectionTypeIds).setImportedGroupMapping(importInspectionTypeGroup.getImportMapping()).setImportedStateSetMapping(importStateSets.getImportMapping()).importCatalog();

		if (importAllRelations) {
			importProductTypeInspectionTypeRelations.setImportedProductTypeMapping(importProductType.getImportedMap()).setImportedInspectionTypeMapping(importInspectionType.getImportMapping()).importCatalog();
		}
	}


	private void rollback() {
		importProductType.rollback();
		importInspectionType.rollback();
		importInspectionTypeGroup.rollback();
		importStateSets.rollback();
		importProductTypeGroup.rollback();
	}
	
	public CatalogImportSummary importSelectionSummary() {
		adjustWhatToImport();
		importProductTypeGroup.getSummaryForImport(importProductTypeIds);
		importProductType.getSummaryForImport(importProductTypeIds);
		importInspectionType.getSummaryForImport(importInspectionTypeIds);
		importInspectionTypeGroup.getSummaryForImport(importInspectionTypeIds);
		importStateSets.getSummaryForImport(importInspectionTypeIds);
		return summary;
	}


	private void adjustWhatToImport() {
		importProductTypeIds.addAll(importProductType.getAdditionalAssetTypes(importProductTypeIds));
		if (importAllRelations) {
			importInspectionTypeIds.addAll(importProductTypeInspectionTypeRelations.getAdditionalInspectionTypesForRelationships(importProductTypeIds, importInspectionTypeIds));
		}
	}
	
	
	public Set<Long> getImportProductTypeIds() {
		return importProductTypeIds;
	}

	public void setImportProductTypeIds(Set<Long> importProductTypeIds) {
		this.importProductTypeIds = importProductTypeIds;
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
