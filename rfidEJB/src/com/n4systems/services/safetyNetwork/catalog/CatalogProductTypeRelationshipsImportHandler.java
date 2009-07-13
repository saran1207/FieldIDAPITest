package com.n4systems.services.safetyNetwork.catalog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.model.TenantOrganization;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.catalog.summary.ProductTypeRelationshipsImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;

public class CatalogProductTypeRelationshipsImportHandler extends CatalogImportHandler {

	private Map<Long, ProductType> importedProductTypeMapping = new HashMap<Long, ProductType>();
	private Map<Long, InspectionType> importedInspectionTypeMapping = new HashMap<Long, InspectionType>();
	private ProductTypeRelationshipsImportSummary summary;
	
	public CatalogProductTypeRelationshipsImportHandler(PersistenceManager persistenceManager, TenantOrganization tenant, CatalogService importCatalog) {
		this(persistenceManager, tenant, importCatalog, new ProductTypeRelationshipsImportSummary());
	}
	
	public CatalogProductTypeRelationshipsImportHandler(PersistenceManager persistenceManager, TenantOrganization tenant, CatalogService importCatalog, ProductTypeRelationshipsImportSummary summary) {
		super(persistenceManager, tenant, importCatalog);
		this.summary = summary;
	}
	
	
	public void importCatalog() throws ImportFailureException {
		for (Long productTypeId : importedProductTypeMapping.keySet()) {
			importProductTypeRelationships(importCatalog.getPublishedProductType(productTypeId, "inspectionTypes" , "schedules"));
		}
	}
	
	public void importProductTypeRelationships(ProductType originalProductType) throws ImportFailureException {
		ProductType importedProductType = importedProductTypeMapping.get(originalProductType.getId());
		try {
			for (InspectionType connectedInspectionType : originalProductType.getInspectionTypes()) {
				importConnectionsToInspectionTypes(originalProductType, importedProductType, connectedInspectionType);
				persistenceManager.update(importedProductType);
			}
		} catch (Exception e) {
			summary.setFailure(importedProductType.getName(), FailureType.COULD_NOT_LINK_PRODUCT_TYPE_TO_INSPECTION_TYPE, e);
			throw new ImportFailureException(e);
		}
	}


	private void importConnectionsToInspectionTypes(ProductType originalProductType, ProductType importedProductType, InspectionType connectedInspectionType) {
		if (importedInspectionTypeMapping.get(connectedInspectionType.getId()) != null) {
			importedProductType.getInspectionTypes().add(importedInspectionTypeMapping.get(connectedInspectionType.getId()));
			importProductTypeSchedules(originalProductType, importedProductType, connectedInspectionType);
		}
	}


	private void importProductTypeSchedules(ProductType originalProductType, ProductType importedProductType, InspectionType connectedInspectionType) {
		if (originalProductType.getSchedule(connectedInspectionType, null) != null) {
			ProductTypeSchedule originalSchedule = originalProductType.getSchedule(connectedInspectionType, null);
			ProductTypeSchedule importedSchedule = new ProductTypeSchedule();
			importedSchedule.setAutoSchedule(originalSchedule.isAutoSchedule());
			importedSchedule.setFrequency(originalSchedule.getFrequency());
			importedSchedule.setInspectionType(importedInspectionTypeMapping.get(connectedInspectionType.getId()));
			importedSchedule.setProductType(importedProductType);
			importedSchedule.setTenant(tenant);
			importedProductType.getSchedules().add(importedSchedule);
		}
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


	public CatalogProductTypeRelationshipsImportHandler setImportedProductTypeMapping(Map<Long, ProductType> importedProductTypeMapping) {
		this.importedProductTypeMapping = importedProductTypeMapping;
		return this;
	}


	public CatalogProductTypeRelationshipsImportHandler setImportedInspectionTypeMapping(Map<Long, InspectionType> importedInspectionTypeMapping) {
		this.importedInspectionTypeMapping = importedInspectionTypeMapping;
		return this;
	}
	
}
