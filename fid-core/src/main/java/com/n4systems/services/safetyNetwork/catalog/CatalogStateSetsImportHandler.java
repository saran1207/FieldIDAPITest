package com.n4systems.services.safetyNetwork.catalog;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.StateSet;
import com.n4systems.model.Tenant;
import com.n4systems.model.utils.CleanStateSetFactory;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.StateSetImportSummary;
import com.n4systems.services.safetyNetwork.catalog.summary.BaseImportSummary.FailureType;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;

public class CatalogStateSetsImportHandler extends CatalogImportHandler {

	private StateSetImportSummary summary = new StateSetImportSummary();
	private Set<Long> inspectionTypeIds;
	
	public CatalogStateSetsImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog) {
		this(persistenceManager, tenant, importCatalog, new StateSetImportSummary());
	}
	
	public CatalogStateSetsImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog, StateSetImportSummary summary) {
		super(persistenceManager, tenant, importCatalog);
		this.summary = summary;
		
	}

	
	public void importCatalog() throws ImportFailureException {
		findStateSetsToImport(inspectionTypeIds);
		for (StateSet originalStateSet : summary.getStateSetsToCreate()) {
			try {
				importStateSet(originalStateSet);
			} catch (Exception e) {
				summary.setFailure(originalStateSet.getName(), FailureType.COULD_NOT_CREATE, e);
				throw new ImportFailureException(e);
			}
		}
	}
	
	private void importStateSet(StateSet originalStateSet) {
		Long originalId = originalStateSet.getId();
		new CleanStateSetFactory(originalStateSet, tenant).clean();
		persistenceManager.save(originalStateSet);
		summary.getImportMapping().put(originalId, originalStateSet);
		summary.createdStateSet(originalStateSet);
	}
	
	public BaseImportSummary getSummaryForImport(Set<Long> inspectionTypeIds) {
		findStateSetsToImport(inspectionTypeIds);
		return summary;
	}


	private void findStateSetsToImport(Set<Long> inspectionTypeIds) {
		List<StateSet> originalStateSets = getUsedStateSets(inspectionTypeIds);
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


	private List<StateSet> getUsedStateSets(Set<Long> inspectionTypeIds) {
		return importCatalog.getStateSetsUsedIn(inspectionTypeIds);
	}

	public Map<Long, StateSet> getImportMapping() {
		return summary.getImportMapping();
	}
	
	public void rollback() {
		for (StateSet stateSetToRemove : summary.getCreatedStateSets()) {
			persistenceManager.delete(stateSetToRemove);
		}
	}


	public CatalogStateSetsImportHandler setInspectionTypeIds(Set<Long> inspectionTypeIds) {
		this.inspectionTypeIds = inspectionTypeIds;
		return this;
	}
}
