package com.n4systems.services.safetyNetwork.catalog;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.StateSet;
import com.n4systems.model.Tenant;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.CleanStateSetFactory;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.StateSetImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;
import org.apache.log4j.Logger;

public class CatalogStateSetsImportHandler extends CatalogImportHandler {

    private static final Logger logger = Logger.getLogger(CatalogStateSetsImportHandler.class);

	private StateSetImportSummary summary = new StateSetImportSummary();
	private Set<Long> eventTypeIds;
	private User importUser;
	
	public CatalogStateSetsImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog) {
		this(persistenceManager, tenant, importCatalog, new StateSetImportSummary());
	}
	
	public CatalogStateSetsImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog, StateSetImportSummary summary) {
		super(persistenceManager, tenant, importCatalog);
		this.summary = summary;
		
	}

	
	public void importCatalog() throws ImportFailureException {
		findStateSetsToImport(eventTypeIds);
		for (StateSet originalStateSet : summary.getStateSetsToCreate()) {
			try {
				importStateSet(originalStateSet);
			} catch (Exception e) {
                logger.error("Error importing state sets", e);
				summary.setFailure(originalStateSet.getName(), FailureType.COULD_NOT_CREATE, e);
				throw new ImportFailureException(e);
			}
		}
	}
	
	private void importStateSet(StateSet originalStateSet) {
		Long originalId = originalStateSet.getId();
		new CleanStateSetFactory(originalStateSet, tenant).clean();
		originalStateSet.setCreatedBy(importUser);
		persistenceManager.save(originalStateSet);
		summary.getImportMapping().put(originalId, originalStateSet);
		summary.createdStateSet(originalStateSet);
	}
	
	public BaseImportSummary getSummaryForImport(Set<Long> eventTypeIds) {
		findStateSetsToImport(eventTypeIds);
		return summary;
	}


	private void findStateSetsToImport(Set<Long> eventTypeIds) {
		Collection<StateSet> originalStateSets = getUsedStateSets(eventTypeIds);
		List<StateSet> currentStateSets = persistenceManager.findAll(StateSet.class, tenant.getId());
			
		for (StateSet originalStateSet : originalStateSets) {
			int index = currentStateSets.indexOf(originalStateSet);
			if (index == -1) {
				summary.getStateSetsToCreate().add(originalStateSet);
			} else {
				summary.getImportMapping().put(originalStateSet.getId(), currentStateSets.get(index));
			}
		}
	}


	private Collection<StateSet> getUsedStateSets(Set<Long> eventTypeIds) {
		return importCatalog.getStateSetsUsedIn(eventTypeIds);
	}

	public Map<Long, StateSet> getImportMapping() {
		return summary.getImportMapping();
	}
	
	public void rollback() {
		for (StateSet stateSetToRemove : summary.getCreatedStateSets()) {
			persistenceManager.delete(stateSetToRemove);
		}
	}


	public CatalogStateSetsImportHandler setEventTypeIds(Set<Long> eventTypeIds) {
		this.eventTypeIds = eventTypeIds;
		return this;
	}

	public CatalogStateSetsImportHandler setImportUser(User importUser) {
		this.importUser = importUser;
		return this;
	}
}
