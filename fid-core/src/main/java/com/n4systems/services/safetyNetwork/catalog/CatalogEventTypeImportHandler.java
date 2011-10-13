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
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.StateSet;
import com.n4systems.model.Tenant;
import com.n4systems.model.user.User;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.catalog.summary.EventTypeImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;
import com.n4systems.util.ListingPair;
import org.apache.log4j.Logger;

public class CatalogEventTypeImportHandler extends CatalogImportHandler {

    private static final Logger logger = Logger.getLogger(CatalogEventTypeImportHandler.class);

	private Map<Long, EventTypeGroup> importedGroupMapping;
	private Map<Long, StateSet> importedStateSetMapping;
	private Set<Long> originalEventTypeIds;
	private EventTypeImportSummary summary = new EventTypeImportSummary();
	private User importUser;
	
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
            logger.error("Error importing event type", e);
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
        persistenceManager.save(importedEventType.getEventForm());

		importedEventType.setCreatedBy(importUser);
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
        if (importedEventType.getEventForm() != null) {
            for (CriteriaSection criteriaSection : importedEventType.getEventForm().getSections()) {
                for (Criteria criteria : criteriaSection.getCriteria()) {
                    if (criteria instanceof OneClickCriteria) {
                        OneClickCriteria oneClickCriteria = (OneClickCriteria) criteria;
                        oneClickCriteria.setStates(importedStateSetMapping.get(oneClickCriteria.getStates().getId()));
                    }
                }
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

	public CatalogEventTypeImportHandler setImportUser(User importUser) {
		this.importUser = importUser;
		return this;
	}

}
