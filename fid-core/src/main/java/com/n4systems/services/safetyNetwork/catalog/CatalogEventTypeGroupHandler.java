package com.n4systems.services.safetyNetwork.catalog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.PrintOut;
import com.n4systems.model.Tenant;
import com.n4systems.model.PrintOut.PrintOutType;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.catalog.summary.InspectionTypeGroupImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;

public class CatalogEventTypeGroupHandler extends CatalogImportHandler {
	
	private List<ListingPair> inspectionTypesToBeCreated;
	private InspectionTypeGroupImportSummary summary;
	private Set<Long> inspectionTypeIds;
	
	public CatalogEventTypeGroupHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog) {
		this(persistenceManager, tenant, importCatalog, new InspectionTypeGroupImportSummary());
	}
	
	public CatalogEventTypeGroupHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog, InspectionTypeGroupImportSummary summary) {
		super(persistenceManager, tenant, importCatalog);
		this.summary = summary;
	}
	
	public void importCatalog() throws ImportFailureException{
		
		findGroupsToImport(inspectionTypeIds);
		for (ListingPair originalGroup : inspectionTypesToBeCreated) {
			EventTypeGroup importedGroup = copyGroup(originalGroup);
			try {
				setDefaultCerts(importedGroup);
				persistenceManager.save(importedGroup);
				summary.getImportMapping().put(originalGroup.getId(), importedGroup);
				summary.createdGroup(importedGroup);
			} catch (Exception e) {
				summary.setFailure(originalGroup.getName(), FailureType.COULD_NOT_CREATE, e);
				throw new ImportFailureException(e);
			}
		}
	}

	private EventTypeGroup copyGroup(ListingPair originalGroup) {
		EventTypeGroup importedGroup = new EventTypeGroup();
		importedGroup.setName(originalGroup.getName());
		importedGroup.setReportTitle(originalGroup.getName());
		importedGroup.setTenant(tenant);
		return importedGroup;
		
	}
	
	private void setDefaultCerts(EventTypeGroup importedGroup) {
		importedGroup.setPrintOut(getDefaultCert());
		importedGroup.setObservationPrintOut(getDefaultObservationCert());
	}

	private PrintOut getDefaultObservationCert() {
		QueryBuilder<PrintOut> queryObservation = new QueryBuilder<PrintOut>(PrintOut.class, new OpenSecurityFilter());
		queryObservation.addSimpleWhere("custom", false);
		queryObservation.addSimpleWhere("type", PrintOutType.OBSERVATION);
		queryObservation.addOrder("name");
		return persistenceManager.findAllPaged(queryObservation, 1, 1).getList().iterator().next();
	}

	private PrintOut getDefaultCert() {
		QueryBuilder<PrintOut> queryCert = new QueryBuilder<PrintOut>(PrintOut.class, new OpenSecurityFilter());
		queryCert.addSimpleWhere("custom", false);
		queryCert.addSimpleWhere("type", PrintOutType.CERT);
		queryCert.addOrder("name");
		return persistenceManager.findAllPaged(queryCert, 1, 1).getList().iterator().next();
	}
	
	public InspectionTypeGroupImportSummary getSummaryForImport(Set<Long> inspectionTypeIds) {
		if (inspectionTypeIds == null) { 
			throw new RuntimeException();
		}
				
		if (!inspectionTypeIds.isEmpty()) {
			summary.getInspectionsGroupsToCreate().addAll(findGroupsToImport(inspectionTypeIds));
		}
		
		return summary;
	}

	private List<ListingPair> findGroupsToImport(Set<Long> inspectionTypeIds) {
		List<ListingPair> existingGroups = getOriginialGroups(inspectionTypeIds);
		inspectionTypesToBeCreated = new ArrayList<ListingPair>();
		
		for (ListingPair existingInspectionType : existingGroups) {
			if (persistenceManager.uniqueNameAvailable(EventTypeGroup.class, existingInspectionType.getName(), null, tenant.getId())) {
				inspectionTypesToBeCreated.add(existingInspectionType);
			} else {
				summary.getImportMapping().put(existingInspectionType.getId(), persistenceManager.findByName(EventTypeGroup.class, tenant.getId(), existingInspectionType.getName()));
			}
		}
		return inspectionTypesToBeCreated;
	}

	
	private List<ListingPair> getOriginialGroups(Set<Long> inspectionTypeIds) {
		if (!inspectionTypeIds.isEmpty()) {
			List<ListingPair> existingInspectionTypes = new ArrayList<ListingPair>(importCatalog.getInspectionTypeGroupsFor(inspectionTypeIds));
			Collections.sort(existingInspectionTypes);
			return existingInspectionTypes;
		} 
		return new ArrayList<ListingPair>();
	}

	
	public Map<Long, EventTypeGroup> getImportMapping() {
		return summary.getImportMapping();
	}
	
	public void rollback() {
		for (EventTypeGroup groupToBeDeleted : summary.getCreatedGroups()) {
			persistenceManager.delete(groupToBeDeleted);
		}
	}

	public CatalogEventTypeGroupHandler setInspectionTypeIds(Set<Long> inspectionTypeIds) {
		this.inspectionTypeIds = inspectionTypeIds;
		return this;
	}
	
}
