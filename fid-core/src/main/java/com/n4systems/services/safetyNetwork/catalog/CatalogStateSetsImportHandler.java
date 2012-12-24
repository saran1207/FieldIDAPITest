package com.n4systems.services.safetyNetwork.catalog;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.ButtonGroup;
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
		for (ButtonGroup originalButtonGroup : summary.getStateSetsToCreate()) {
			try {
				importStateSet(originalButtonGroup);
			} catch (Exception e) {
                logger.error("Error importing state sets", e);
				summary.setFailure(originalButtonGroup.getName(), FailureType.COULD_NOT_CREATE, e);
				throw new ImportFailureException(e);
			}
		}
	}
	
	private void importStateSet(ButtonGroup originalButtonGroup) {
		Long originalId = originalButtonGroup.getId();
		new CleanStateSetFactory(originalButtonGroup, tenant).clean();
		originalButtonGroup.setCreatedBy(importUser);
		persistenceManager.save(originalButtonGroup);
		summary.getImportMapping().put(originalId, originalButtonGroup);
		summary.createdStateSet(originalButtonGroup);
	}
	
	public BaseImportSummary getSummaryForImport(Set<Long> eventTypeIds) {
		findStateSetsToImport(eventTypeIds);
		return summary;
	}


	private void findStateSetsToImport(Set<Long> eventTypeIds) {
		Collection<ButtonGroup> originalButtonGroups = getUsedStateSets(eventTypeIds);
		List<ButtonGroup> currentButtonGroups = persistenceManager.findAll(ButtonGroup.class, tenant.getId());
			
		for (ButtonGroup originalButtonGroup : originalButtonGroups) {
			int index = currentButtonGroups.indexOf(originalButtonGroup);
			if (index == -1) {
				summary.getStateSetsToCreate().add(originalButtonGroup);
			} else {
				summary.getImportMapping().put(originalButtonGroup.getId(), currentButtonGroups.get(index));
			}
		}
	}


	private Collection<ButtonGroup> getUsedStateSets(Set<Long> eventTypeIds) {
		return importCatalog.getStateSetsUsedIn(eventTypeIds);
	}

	public Map<Long, ButtonGroup> getImportMapping() {
		return summary.getImportMapping();
	}
	
	public void rollback() {
		for (ButtonGroup buttonGroupToRemove : summary.getCreatedButtonGroups()) {
			persistenceManager.delete(buttonGroupToRemove);
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
