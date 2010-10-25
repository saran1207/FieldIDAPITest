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
import com.n4systems.services.safetyNetwork.catalog.summary.ProductTypeGroupImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;
import com.n4systems.util.persistence.QueryBuilder;

public class CatalogProductTypeGroupImportHandler extends CatalogImportHandler {

	
	private ProductTypeGroupImportSummary summary;
	private Set<Long> productTypeIds;
	
	public CatalogProductTypeGroupImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog) {
		this(persistenceManager, tenant, importCatalog, new ProductTypeGroupImportSummary());;
	}
	
	public CatalogProductTypeGroupImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog, ProductTypeGroupImportSummary summary) {
		super(persistenceManager, tenant, importCatalog);
		this.summary = summary; 
	}
	
	public CatalogProductTypeGroupImportHandler setProductTypeIds(Set<Long> productTypeIds) {
		this.productTypeIds = productTypeIds;
		return this;
	}
	
	public void importCatalog() throws ImportFailureException {
		if (!productTypeIds.isEmpty()) {
			findGroupsToImport(productTypeIds);
		}
		
		for (AssetTypeGroup originalGroup : summary.getImportedProductTypeGroupNames()) {
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
	
	
	public ProductTypeGroupImportSummary getSummaryForImport(Set<Long> productTypeIds) {
		if (productTypeIds == null) { 
			throw new RuntimeException();
		}

		if (!productTypeIds.isEmpty()) {
			findGroupsToImport(productTypeIds);
		}
		
		return summary;
	}
	
	
	private void findGroupsToImport(Set<Long> productTypeIds) {
		for (AssetTypeGroup existingAssetTypeGroup : getOriginialGroups(productTypeIds)) {
			if (persistenceManager.uniqueNameAvailable(AssetTypeGroup.class, existingAssetTypeGroup.getName(), null, tenant.getId())) {
				summary.getImportedProductTypeGroupNames().add(existingAssetTypeGroup);
			} else {
				summary.getImportMapping().put(existingAssetTypeGroup.getId(), persistenceManager.findByName(AssetTypeGroup.class, tenant.getId(), existingAssetTypeGroup.getName()));
			}
		}
	}
	
	
	private List<AssetTypeGroup> getOriginialGroups(Set<Long> productTypeIds) {
		return new ArrayList<AssetTypeGroup>(importCatalog.getAssetTypeGroupsFor(productTypeIds));
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
