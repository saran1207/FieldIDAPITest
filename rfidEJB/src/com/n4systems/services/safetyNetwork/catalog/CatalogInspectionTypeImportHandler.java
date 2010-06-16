package com.n4systems.services.safetyNetwork.catalog;

import static com.n4systems.model.inspectiontype.InspectionTypeCleanerFactory.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.StateSet;
import com.n4systems.model.Tenant;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.catalog.summary.InspectionTypeImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;
import com.n4systems.util.ListingPair;

public class CatalogInspectionTypeImportHandler extends CatalogImportHandler {

	private Map<Long, InspectionTypeGroup> importedGroupMapping;
	private Map<Long, StateSet> importedStateSetMapping;
	private Set<Long> originalInspectionTypeIds;
	private InspectionTypeImportSummary summary = new InspectionTypeImportSummary();
	
	
	
	public CatalogInspectionTypeImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog, InspectionTypeImportSummary summary) {
		super(persistenceManager, tenant, importCatalog);
		this.summary = summary;
	}

	public void importCatalog() throws ImportFailureException {
		for (Long originalInspetionTypeId : originalInspectionTypeIds) {
			importInspectionType(originalInspetionTypeId);
		}
	}
	
	private void importInspectionType(Long originalId) throws ImportFailureException {
		InspectionType importedInspectionType = importCatalog.getPublishedInspectionType(originalId);
		try {
			importIntoAccount(importedInspectionType);
			
			summary.getImportMapping().put(originalId, importedInspectionType);
		} catch (Exception e) {
			summary.setFailure(importedInspectionType.getName(), FailureType.COULD_NOT_CREATE, e);
			throw new ImportFailureException(e);
		}
	}

	private void importIntoAccount(InspectionType importedInspectionType) {
		clean(importedInspectionType);
		
		resolveGroup(importedInspectionType);
		
		produceUniqueName(importedInspectionType);
		
		mapStateSets(importedInspectionType);
		
		save(importedInspectionType);
	}

	private void save(InspectionType importedInspectionType) {
		persistenceManager.save(importedInspectionType);
	}

	private void produceUniqueName(InspectionType importedInspectionType) {
		importedInspectionType.setName(createUniqueInspectionTypeName(importedInspectionType.getName()));
	}

	private void resolveGroup(InspectionType importedInspectionType) {
		importedInspectionType.setGroup(importedGroupMapping.get(importedInspectionType.getGroup().getId()));
	}

	private void clean(InspectionType importedInspectionType) {
		cleanerFor(tenant).clean(importedInspectionType);
	}

	private void mapStateSets(InspectionType importedInspectionType) {
		for (CriteriaSection criteriaSection : importedInspectionType.getSections()) {
			for (Criteria criteria : criteriaSection.getCriteria()) {
				criteria.setStates(importedStateSetMapping.get(criteria.getStates().getId()));
			}
		}
	}
	
	
	public InspectionTypeImportSummary getSummaryForImport(Set<Long> inspectionTypeIds) {
		if (inspectionTypeIds == null) { 
			throw new RuntimeException();
		}
		
		List<ListingPair> inspectionTypes = importCatalog.getPublishedInspectionTypesLP();
		for (ListingPair inspectionType : inspectionTypes) {
			if (inspectionTypeIds.contains(inspectionType.getId())) {
				summary.getImportMapping().put(inspectionType.getId(), new InspectionType(createUniqueInspectionTypeName(inspectionType.getName())) );
			}
		}
		return summary;
	}
	
	
	private String createUniqueInspectionTypeName(String inspectionTypeName) {
		if (!persistenceManager.uniqueNameAvailable(InspectionType.class, inspectionTypeName, null, tenant.getId())) {
			int namePostFix = 1;
			inspectionTypeName += "(" + importCatalog.getTenant().getName() + ")";
			String tmpInspectionTypeName = inspectionTypeName; 
			while (!persistenceManager.uniqueNameAvailable(InspectionType.class, tmpInspectionTypeName, null, tenant.getId())) {
				tmpInspectionTypeName = inspectionTypeName + "(" + namePostFix + ")";
				namePostFix++;
			}
			summary.renamed();
			return tmpInspectionTypeName;
		}
		return inspectionTypeName;
	}


	public Map<Long, InspectionType> getImportMapping() {
		return summary.getImportMapping();
	}
	
	public void rollback() {
		for (InspectionType inspectionTypeToBeRemoved : summary.getImportMapping().values()) {
			persistenceManager.delete(inspectionTypeToBeRemoved);
		}
	}


	public CatalogInspectionTypeImportHandler setOriginalInspectionTypeIds(Set<Long> originalInspectionTypeIds) {
		this.originalInspectionTypeIds = originalInspectionTypeIds;
		return this;
	}


	public CatalogInspectionTypeImportHandler setImportedStateSetMapping(Map<Long, StateSet> importedStateSetMapping) {
		this.importedStateSetMapping = importedStateSetMapping;
		return this;
	}


	public CatalogInspectionTypeImportHandler setImportedGroupMapping(Map<Long, InspectionTypeGroup> importedGroupMapping) {
		this.importedGroupMapping = importedGroupMapping;
		return this;
	}
}
