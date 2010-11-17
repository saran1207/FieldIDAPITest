package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.services.TenantCache;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.exceptions.NoAccessToTenantException;
import com.n4systems.fieldid.actions.helpers.AssetTypeLister;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.Tenant;
import com.n4systems.security.Permissions;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.ImportCatalogService;
import com.n4systems.services.safetyNetwork.SafetyNetworkAccessService;
import com.n4systems.services.safetyNetwork.catalog.summary.CatalogImportSummary;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.CatalogImportTask;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ListingPair;

@UserPermissionFilter(userRequiresOneOf = { Permissions.ManageSafetyNetwork })
public class PublishedCatalogCrud extends SafetyNetwork {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(PublishedCatalogCrud.class);

	private Tenant linkedTenant;
	private CatalogService linkedCatalogAccess;
	private LegacyAssetType assetTypeManager;
	private boolean quickSetupWizardCatalogImport = false;

	private Map<String, Boolean> importAssetTypeIds = new HashMap<String, Boolean>();

	public void setImportAssetTypeIds(Map<String, Boolean> importAssetTypeIds) {
		this.importAssetTypeIds = importAssetTypeIds;
	}

	private Map<String, Boolean> importEventTypeIds = new HashMap<String, Boolean>();
	private boolean usingPackage = false;

	private CatalogImportSummary summary;
	private Map<Long, List<ListingPair>> cacheSubTypes = new HashMap<Long, List<ListingPair>>();
	private Map<Long, List<ListingPair>> cacheInpsectionTypes = new HashMap<Long, List<ListingPair>>();

	public PublishedCatalogCrud(PersistenceManager persistenceManager, LegacyAssetType assetTypeManager) {
		super(persistenceManager);
		this.assetTypeManager = assetTypeManager;

	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		linkedTenant = persistenceManager.find(Tenant.class, uniqueId);

	}

	public PrimaryOrg getCustomer() {
		return getPrimaryOrgForThisTenant();
	}

	public PrimaryOrg getVendor() {
		return getPrimaryOrgForThisTenant();
	}

	protected PrimaryOrg getPrimaryOrgForThisTenant() {
		return TenantCache.getInstance().findPrimaryOrg(uniqueID);
	}

	public String doShow() {
		if (isQuickSetupWizardCatalogImport()) {
			getCatalogConnection();
		}

		testPreconditions();
		SafetyNetworkAccessService safetyNetwork = new SafetyNetworkAccessService(persistenceManager, getSecurityFilter());
		try {
			linkedCatalogAccess = safetyNetwork.getCatalogAccess(linkedTenant);
		} catch (NoAccessToTenantException e) {
			logger.warn(getLogLinePrefix() + "attempt to access non linked tenant catalog for " + linkedTenant.getName() + " by " + getTenant().getName());
			addActionErrorText("error.no_access_to_linked_catalog");
			throw new MissingEntityException();
		}
		return SUCCESS;
	}
	
	public String doShowLoadingPage(){
		return SUCCESS;
	}

	public String doConfirm() {
		doShow();
		Set<Long> importTheseAssetTypeIds = covertSelectedIdsToSet(importAssetTypeIds);
		Set<Long> importTheseEventTypeIds = covertSelectedIdsToSet(importEventTypeIds);

		try {
			ImportCatalogService importCatalogService = new ImportCatalogService(persistenceManager, getPrimaryOrg(), linkedCatalogAccess, assetTypeManager);
			importCatalogService.setImportAssetTypeIds(importTheseAssetTypeIds);
			importCatalogService.setImportEventTypeIds(importTheseEventTypeIds);
			importCatalogService.setImportAllRelations(usingPackage);
			summary = importCatalogService.importSelectionSummary();
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + " could create import confirm page for " + getTenant().getName() + " on catalog " + linkedTenant.getName(), e);
			addActionErrorText("error.importing_catalog");
			return ERROR;
		}

		return SUCCESS;
	}

	private void testPreconditions() {
		if (linkedTenant == null) {
			addFlashErrorText("error.you_must_select_a_linked_company_to_import_a_catalog_from");
			throw new MissingEntityException("linked company required");
		}

	}

	private Set<Long> covertSelectedIdsToSet(Map<String, Boolean> idSet) {
		Set<Long> importIds = new HashSet<Long>();
		for (Entry<String, Boolean> entry : idSet.entrySet()) {
			if (entry.getValue()) {
				importIds.add(Long.parseLong(entry.getKey()));
			}
		}
		return importIds;
	}

	public String doImport() {
		doShow();

		try {
			CatalogImportTask importTask = new CatalogImportTask();

			importTask.setImportEventTypeIds(covertSelectedIdsToSet(importEventTypeIds));
			importTask.setImportAssetTypeIds(covertSelectedIdsToSet(importAssetTypeIds));
			importTask.setPrimaryOrg(getPrimaryOrg());
			importTask.setLinkedTenant(getLinkedTenant());
			importTask.setUsingPackages(usingPackage);
			importTask.setUser(fetchCurrentUser());

			TaskExecutor.getInstance().execute(importTask);

			logger.info(getLogLinePrefix() + "secheduled imported asset types from " + linkedTenant.getName() + " catalog");

		} catch (Exception e) {
			logger.error(getLogLinePrefix() + " could not import asset types from " + linkedTenant.getName() + " catalog", e);
			addActionErrorText("error.importing_catalog");
			return ERROR;
		}

		return SUCCESS;
	}

	public Tenant getLinkedTenant() {
		return linkedTenant;
	}

	public List<ListingPair> getPublishedAssetTypes() {
		return linkedCatalogAccess.getPublishedAssetTypesLP();
	}

	public Set<AssetTypeGroup> getPublishedAssetTypeGroups() {
		return linkedCatalogAccess.getPublishedAssetTypeGroups();
	}

	public Map<String, Boolean> getImportEventTypeIds() {
		return importEventTypeIds;
	}

	public List<ListingPair> getPublishedEventTypes() {
		return linkedCatalogAccess.getPublishedEventTypesLP();

	}

	public Map<String, Boolean> getImportAssetTypeIds() {
		return importAssetTypeIds;
	}

	public List<ListingPair> getEventTypesFor(Long assetTypeId) {
		if (!cacheInpsectionTypes.containsKey(assetTypeId)) {
			Set<Long> ids = new HashSet<Long>();
			ids.add(assetTypeId);
			Set<Long> eventTypeIds = linkedCatalogAccess.getPublishedEventTypeIdsConnectedTo(ids);
			List<ListingPair> eventTypes = new ArrayList<ListingPair>();
			for (ListingPair eventType : getPublishedEventTypes()) {
				if (eventTypeIds.contains(eventType.getId())) {
					eventTypes.add(eventType);
				}
			}
			cacheInpsectionTypes.put(assetTypeId, eventTypes);
		}
		return cacheInpsectionTypes.get(assetTypeId);
	}

	public List<ListingPair> getSubTypesFor(Long assetTypeId) {
		if (!cacheSubTypes.containsKey(assetTypeId)) {
			List<Long> subTypeIds = linkedCatalogAccess.getAllPublishedSubTypesFor(assetTypeId);
			List<ListingPair> subTypes = new ArrayList<ListingPair>();
			for (ListingPair assetType : getPublishedAssetTypes()) {
				if (subTypeIds.contains(assetType.getId())) {
					subTypes.add(assetType);
				}
			}
			cacheSubTypes.put(assetTypeId, subTypes);
		}
		return cacheSubTypes.get(assetTypeId);
	}

	public void getCatalogConnection() {
		for (TypedOrgConnection connection : getConnections()) {
			if (connection.getConnectedOrg().getTenant().getName().equals(getConfigContext().getString(ConfigEntry.HOUSE_ACCOUNT_NAME))) {
				linkedTenant = connection.getConnectedOrg().getTenant();

			}
		}
	}

	public CatalogImportSummary getSummary() {
		return summary;
	}

	public boolean isUsingPackage() {
		return usingPackage;
	}

	public void setUsingPackage(boolean usingPackage) {
		this.usingPackage = usingPackage;
	}

	public Date getNow() {
		return new Date();
	}

	public int getEstimatedImportTime() {
		return getConfigContext().getInteger(ConfigEntry.ESTIMATED_CATALOG_IMPORT_TIME_IN_MINUTES);
	}

	public boolean isQuickSetupWizardCatalogImport() {
		return quickSetupWizardCatalogImport;
	}

	public void setQuickSetupWizardCatalogImport(boolean quickSetupWizardCatalogImport) {
		this.quickSetupWizardCatalogImport = quickSetupWizardCatalogImport;
	}

	public List<ListingPair> getPublishedAssetTypesForGroup(String groupName) {
		AssetType retrievedAssetType;

		List<ListingPair> groupedTypes = new ArrayList<ListingPair>();

		for (ListingPair assetType : getPublishedAssetTypes()) {
			retrievedAssetType = linkedCatalogAccess.getPublishedAssetType(assetType.getId(), "infoFields");
			if (retrievedAssetType.getGroup() != null) {
				if (retrievedAssetType.getGroup().getName().equals(groupName)){
					groupedTypes.add(assetType);
				}
			}
		}

		return groupedTypes;
	}

	public List<ListingPair> getPublishedAssetTypesUngrouped() {
		AssetType retrievedAssetType;

		List<ListingPair> unGroupedTypes = new ArrayList<ListingPair>();

		for (ListingPair assetType : getPublishedAssetTypes()) {
			retrievedAssetType = linkedCatalogAccess.getPublishedAssetType(assetType.getId(), "infoFields");
			if (retrievedAssetType.getGroup() == null) {
				unGroupedTypes.add(assetType);
			}
		}
		return unGroupedTypes;
	}
}
