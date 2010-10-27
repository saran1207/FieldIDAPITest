package com.n4systems.services.safetyNetwork.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.catalog.summary.AssetTypeGroupImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;
import com.n4systems.util.persistence.QueryBuilder;

public class CatalogAssetTypeGroupImportHandler extends CatalogImportHandler {

	
	private AssetTypeGroupImportSummary summary;
	private Set<Long> assetTypeIds;
	
	public CatalogAssetTypeGroupImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog) {
		this(persistenceManager, tenant, importCatalog, new AssetTypeGroupImportSummary());
	}
	
	public CatalogAssetTypeGroupImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog, AssetTypeGroupImportSummary summary) {
		super(persistenceManager, tenant, importCatalog);
		this.summary = summary; 
	}
	
	public CatalogAssetTypeGroupImportHandler setAssetTypeIds(Set<Long> assetTypeIds) {
		this.assetTypeIds = assetTypeIds;
		return this;
	}
	
	public void importCatalog() throws ImportFailureException {
		if (!assetTypeIds.isEmpty()) {
			findGroupsToImport(assetTypeIds);
		}
		
		for (AssetTypeGroup originalGroup : summary.getImportedAssetTypeGroupNames()) {
			try {
				importGroup(originalGroup);
			} catch (Exception e) {
				summary.setFailure(originalGroup.getName(), FailureType.COULD_NOT_CREATE, e);
				throw new ImportFailureException(e);
			}
		}
		
	}


	private void importGroup(AssetTypeGroup originalGroup) {
		AssetTypeGroup importedGroup = new AssetTypeGroup();
		importedGroup.setName(originalGroup.getName());
		importedGroup.setTenant(tenant);
		importedGroup.setOrderIdx(getNextAvailableIndex());
		
		persistenceManager.save(importedGroup);
		
		summary.getImportMapping().put(originalGroup.getId(), importedGroup);
		summary.createdGroup(importedGroup);
	}
	
	private Long getNextAvailableIndex() {
		QueryBuilder<AssetTypeGroup> query = new QueryBuilder<AssetTypeGroup>(AssetTypeGroup.class, new TenantOnlySecurityFilter(tenant.getId()));
		return persistenceManager.findCount(query);
	}
	
	
	public AssetTypeGroupImportSummary getSummaryForImport(Set<Long> assetTypeIds) {
		if (assetTypeIds == null) {
			throw new RuntimeException();
		}

		if (!assetTypeIds.isEmpty()) {
			findGroupsToImport(assetTypeIds);
		}
		
		return summary;
	}
	
	
	private void findGroupsToImport(Set<Long> assetTypeIds) {
		for (AssetTypeGroup existingAssetTypeGroup : getOriginialGroups(assetTypeIds)) {
			if (persistenceManager.uniqueNameAvailable(AssetTypeGroup.class, existingAssetTypeGroup.getName(), null, tenant.getId())) {
				summary.getImportedAssetTypeGroupNames().add(existingAssetTypeGroup);
			} else {
				summary.getImportMapping().put(existingAssetTypeGroup.getId(), persistenceManager.findByName(AssetTypeGroup.class, tenant.getId(), existingAssetTypeGroup.getName()));
			}
		}
	}
	
	
	private List<AssetTypeGroup> getOriginialGroups(Set<Long> assetTypeIds) {
		return new ArrayList<AssetTypeGroup>(importCatalog.getAssetTypeGroupsFor(assetTypeIds));
	}


	public Map<Long, AssetTypeGroup> getImportMapping() {
		return summary.getImportMapping();
	}
	
	public void rollback() {
		for (AssetTypeGroup groupToDelete : summary.getCreatedGroups()) {
			persistenceManager.delete(groupToDelete);
		}
	}
	
}
