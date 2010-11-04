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
import com.n4systems.services.safetyNetwork.catalog.summary.EventTypeGroupImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;

public class CatalogEventTypeGroupHandler extends CatalogImportHandler {
	
	private List<ListingPair> eventTypesToBeCreated;
	private EventTypeGroupImportSummary summary;
	private Set<Long> eventTypeIds;
	
	public CatalogEventTypeGroupHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog) {
		this(persistenceManager, tenant, importCatalog, new EventTypeGroupImportSummary());
	}
	
	public CatalogEventTypeGroupHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog, EventTypeGroupImportSummary summary) {
		super(persistenceManager, tenant, importCatalog);
		this.summary = summary;
	}
	
	public void importCatalog() throws ImportFailureException{
		
		findGroupsToImport(eventTypeIds);
		for (ListingPair originalGroup : eventTypesToBeCreated) {
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
	
	public EventTypeGroupImportSummary getSummaryForImport(Set<Long> eventTypeIds) {
		if (eventTypeIds == null) {
			throw new RuntimeException();
		}
				
		if (!eventTypeIds.isEmpty()) {
			summary.getEventGroupsToCreate().addAll(findGroupsToImport(eventTypeIds));
		}
		
		return summary;
	}

	private List<ListingPair> findGroupsToImport(Set<Long> eventTypeIds) {
		List<ListingPair> existingGroups = getOriginialGroups(eventTypeIds);
		eventTypesToBeCreated = new ArrayList<ListingPair>();
		
		for (ListingPair existingEventType : existingGroups) {
			if (persistenceManager.uniqueNameAvailable(EventTypeGroup.class, existingEventType.getName(), null, tenant.getId())) {
				eventTypesToBeCreated.add(existingEventType);
			} else {
				summary.getImportMapping().put(existingEventType.getId(), persistenceManager.findByName(EventTypeGroup.class, tenant.getId(), existingEventType.getName()));
			}
		}
		return eventTypesToBeCreated;
	}

	
	private List<ListingPair> getOriginialGroups(Set<Long> eventTypeIds) {
		if (!eventTypeIds.isEmpty()) {
			List<ListingPair> existingEventTypes = new ArrayList<ListingPair>(importCatalog.getEventTypeGroupsFor(eventTypeIds));
			Collections.sort(existingEventTypes);
			return existingEventTypes;
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

	public CatalogEventTypeGroupHandler setEventTypeIds(Set<Long> eventTypeIds) {
		this.eventTypeIds = eventTypeIds;
		return this;
	}
	
}
