package com.n4systems.services.safetyNetwork.catalog;

import static com.n4systems.model.eventtype.EventTypeCleanerFactory.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.StateSet;
import com.n4systems.model.Tenant;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.catalog.summary.EventTypeImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;
import com.n4systems.util.ListingPair;

public class CatalogEventTypeImportHandler extends CatalogImportHandler {

	private Map<Long, EventTypeGroup> importedGroupMapping;
	private Map<Long, StateSet> importedStateSetMapping;
	private Set<Long> originalEventTypeIds;
	private EventTypeImportSummary summary = new EventTypeImportSummary();
	
	public CatalogEventTypeImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog, EventTypeImportSummary summary) {
		super(persistenceManager, tenant, importCatalog);
		this.summary = summary;
	}

	public void importCatalog() throws ImportFailureException {
		for (Long originEventTypeId : originalEventTypeIds) {
			importEventType(originEventTypeId);
		}
	}
	
	private void importEventType(Long originalId) throws ImportFailureException {
		EventType importedEventType = importCatalog.getPublishedEventType(originalId);
		try {
			importIntoAccount(importedEventType);
			
			summary.getImportMapping().put(originalId, importedEventType);
		} catch (Exception e) {
			summary.setFailure(importedEventType.getName(), FailureType.COULD_NOT_CREATE, e);
			throw new ImportFailureException(e);
		}
	}

	private void importIntoAccount(EventType importedEventType) {
		clean(importedEventType);
		
		resolveGroup(importedEventType);
		
		produceUniqueName(importedEventType);
		
		mapStateSets(importedEventType);
		
		save(importedEventType);
	}

	private void save(EventType importedEventType) {
		persistenceManager.save(importedEventType);
	}

	private void produceUniqueName(EventType importedEventType) {
		importedEventType.setName(createUniqueEventTypeName(importedEventType.getName()));
	}

	private void resolveGroup(EventType importedEventType) {
		importedEventType.setGroup(importedGroupMapping.get(importedEventType.getGroup().getId()));
	}

	private void clean(EventType importedEventType) {
		cleanerFor(tenant).clean(importedEventType);
	}

	private void mapStateSets(EventType importedEventType) {
		for (CriteriaSection criteriaSection : importedEventType.getSections()) {
			for (Criteria criteria : criteriaSection.getCriteria()) {
				criteria.setStates(importedStateSetMapping.get(criteria.getStates().getId()));
			}
		}
	}
	
	
	public EventTypeImportSummary getSummaryForImport(Set<Long> eventTypeIds) {
		if (eventTypeIds == null) {
			throw new RuntimeException();
		}
		
		List<ListingPair> eventTypesLP = importCatalog.getPublishedEventTypesLP();
		for (ListingPair eventType : eventTypesLP) {
			if (eventTypeIds.contains(eventType.getId())) {
				summary.getImportMapping().put(eventType.getId(), new EventType(createUniqueEventTypeName(eventType.getName())) );
			}
		}
		return summary;
	}
	
	
	private String createUniqueEventTypeName(String eventTypeName) {
		if (!persistenceManager.uniqueNameAvailable(EventType.class, eventTypeName, null, tenant.getId())) {
			int namePostFix = 1;
			eventTypeName += "(" + importCatalog.getTenant().getName() + ")";
			String tmpEventTypeName = eventTypeName;
			while (!persistenceManager.uniqueNameAvailable(EventType.class, tmpEventTypeName, null, tenant.getId())) {
				tmpEventTypeName = eventTypeName + "(" + namePostFix + ")";
				namePostFix++;
			}
			summary.renamed();
			return tmpEventTypeName;
		}
		return eventTypeName;
	}


	public Map<Long, EventType> getImportMapping() {
		return summary.getImportMapping();
	}
	
	public void rollback() {
		for (EventType eventTypeToBeRemoved : summary.getImportMapping().values()) {
			persistenceManager.delete(eventTypeToBeRemoved);
		}
	}


	public CatalogEventTypeImportHandler setOriginalEventTypeIds(Set<Long> originalEventTypeIds) {
		this.originalEventTypeIds = originalEventTypeIds;
		return this;
	}


	public CatalogEventTypeImportHandler setImportedStateSetMapping(Map<Long, StateSet> importedStateSetMapping) {
		this.importedStateSetMapping = importedStateSetMapping;
		return this;
	}


	public CatalogEventTypeImportHandler setImportedGroupMapping(Map<Long, EventTypeGroup> importedGroupMapping) {
		this.importedGroupMapping = importedGroupMapping;
		return this;
	}
}
