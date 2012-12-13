package com.n4systems.services.safetyNetwork;

import java.util.Set;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.assettype.AssetTypeScheduleSaver;
import com.n4systems.services.safetyNetwork.catalog.*;
import com.n4systems.services.safetyNetwork.catalog.summary.CatalogImportSummary;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;
import org.apache.log4j.Logger;

public class ImportCatalogService {

    private static final Logger logger = Logger.getLogger(ImportCatalogService.class);

	private final CatalogAssetTypeRelationshipsImportHandler importAssetTypeEventTypeRelations;
	private final CatalogAssetTypeGroupImportHandler importAssetTypeGroup;
	private final CatalogAssetTypeImportHandler importAssetType;
	private final CatalogEventTypeImportHandler importEventType;
	private final CatalogEventTypeGroupHandler importEventTypeGroup;
	private final CatalogStateSetsImportHandler importStateSets;
    private final CatalogScoreGroupImportHandler importScoreGroups;
	private Set<Long> importAssetTypeIds;
	private Set<Long> importEventTypeIds;
	private boolean importAllRelations = false;
	private CatalogImportSummary summary = new CatalogImportSummary();
	private User importUser;
	
	
	public ImportCatalogService(PersistenceManager persistenceManager, PrimaryOrg primaryOrg, CatalogService importCatalog, LegacyAssetType legacyAssetTypeManager) {
		importAssetType = new CatalogAssetTypeImportHandler(persistenceManager, primaryOrg.getTenant(), importCatalog, legacyAssetTypeManager, summary.getAssetTypeImportSummary());
		importEventType = new CatalogEventTypeImportHandler(persistenceManager,  primaryOrg.getTenant(), importCatalog, summary.getEventTypeImportSummary());
		importEventTypeGroup = new CatalogEventTypeGroupHandler(persistenceManager,  primaryOrg.getTenant(), importCatalog, summary.getEventTypeGroupImportSummary());
		importStateSets = new CatalogStateSetsImportHandler(persistenceManager,  primaryOrg.getTenant(), importCatalog, summary.getStateSetImportSummary());
		importAssetTypeGroup = new CatalogAssetTypeGroupImportHandler(persistenceManager,  primaryOrg.getTenant(), importCatalog, summary.getAssetTypeGroupImportSummary());
		importAssetTypeEventTypeRelations = new CatalogAssetTypeRelationshipsImportHandler(persistenceManager, primaryOrg, importCatalog, new AssetTypeScheduleSaver(), summary.getAssetTypeRelationshipsImportSummary());
        importScoreGroups = new CatalogScoreGroupImportHandler(persistenceManager,  primaryOrg.getTenant(), importCatalog);
	}
	
	
	public boolean importSelection() {
		adjustWhatToImport();
		try {
			importCatalog();
			return true;
		} catch (ImportFailureException e) {
            logger.error("Error importing catalog, rolling back", e);
			rollback();
			return false;
		}
	}


	private void importCatalog() throws ImportFailureException {
		importAssetTypeGroup.setAssetTypeIds(importAssetTypeIds).setImportUser(importUser).importCatalog();
		importAssetType.setAssetGroupMapping(importAssetTypeGroup.getImportMapping()).setImportAssetTypeIds(importAssetTypeIds).setImportUser(importUser).importCatalog();
		importEventTypeGroup.setEventTypeIds(importEventTypeIds).setImportUser(importUser).importCatalog();
		importStateSets.setEventTypeIds(importEventTypeIds).setImportUser(importUser).importCatalog();
        importScoreGroups.setEventTypIds(importEventTypeIds).setImportUser(importUser).importCatalog();
		importEventType.setOriginalEventTypeIds(importEventTypeIds).setImportedGroupMapping(importEventTypeGroup.getImportMapping()).setImportedStateSetMapping(importStateSets.getImportMapping()).setImportedScoreGroups(importScoreGroups.getMappedScoreGroups()).setImportUser(importUser).importCatalog();


		if (importAllRelations) {
			importAssetTypeEventTypeRelations.setImportedAssetTypeMapping(importAssetType.getImportedMap()).setImportedEventTypeMapping(importEventType.getImportMapping()).importCatalog();
		}
	}


	private void rollback() {
		importAssetType.rollback();
		importEventType.rollback();
		importEventTypeGroup.rollback();
		importStateSets.rollback();
		importAssetTypeGroup.rollback();
        importScoreGroups.rollback();
	}
	
	public CatalogImportSummary importSelectionSummary() {
		adjustWhatToImport();
		importAssetTypeGroup.getSummaryForImport(importAssetTypeIds);
		importAssetType.getSummaryForImport(importAssetTypeIds);
		importEventType.getSummaryForImport(importEventTypeIds);
		importEventTypeGroup.getSummaryForImport(importEventTypeIds);
		importStateSets.getSummaryForImport(importEventTypeIds);
		return summary;
	}


	private void adjustWhatToImport() {
		importAssetTypeIds.addAll(importAssetType.getAdditionalAssetTypes(importAssetTypeIds));
		if (importAllRelations) {
			importEventTypeIds.addAll(importAssetTypeEventTypeRelations.getAdditionalEventTypesForRelationships(importAssetTypeIds, importEventTypeIds));
		}
	}
	
	
	public Set<Long> getImportAssetTypeIds() {
		return importAssetTypeIds;
	}

	public void setImportAssetTypeIds(Set<Long> importAssetTypeIds) {
		this.importAssetTypeIds = importAssetTypeIds;
	}

	public Set<Long> getImportEventTypeIds() {
		return importEventTypeIds;
	}

	public void setImportEventTypeIds(Set<Long> importEventTypeIds) {
		this.importEventTypeIds = importEventTypeIds;
	}


	public void setImportAllRelations(boolean importAllRelations) {
		this.importAllRelations = importAllRelations;
	}


	public CatalogImportSummary getSummary() {
		return summary;
	}


	public void setImportUser(User user) {
		this.importUser = user;
	}

}
