package com.n4systems.fieldid.service.asset;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.exceptions.*;
import com.n4systems.fieldid.LegacyMethod;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.ReportServiceHelper;
import com.n4systems.fieldid.service.event.LastEventDateService;
import com.n4systems.fieldid.service.event.NotifyEventAssigneeService;
import com.n4systems.fieldid.service.event.ProcedureAuditEventService;
import com.n4systems.fieldid.service.mixpanel.MixpanelService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.procedure.NotifyProcedureAssigneeService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.service.project.ProjectService;
import com.n4systems.fieldid.service.transaction.TransactionService;
import com.n4systems.model.*;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.asset.AssetSaver;
import com.n4systems.model.asset.ScheduleSummaryEntry;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.archivers.EventListArchiver;
import com.n4systems.persistence.utils.PostFetcher;
import com.n4systems.services.reporting.AssetsIdentifiedReportRecord;
import com.n4systems.services.reporting.AssetsStatusReportRecord;
import com.n4systems.services.tenant.Tenant30DayCountRecord;
import com.n4systems.util.AssetRemovalSummary;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.collections.PrioritizedList;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import rfid.ejb.entity.AddAssetHistory;
import rfid.ejb.entity.AssetCodeMapping;
import rfid.ejb.entity.AssetExtension;
import rfid.ejb.entity.InfoOptionBean;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

@Transactional
public class AssetService extends CrudService<Asset> {

	@Autowired private ReportServiceHelper reportServiceHelper;
    @Autowired private TransactionService transactionService;
    @Autowired private LastEventDateService lastEventDateService;
    @Autowired private AssetCodeMappingService assetCodeMappingService;
    @Autowired private AssetTypeService assetTypeService;
    @Autowired private OrgService orgService;
    @Autowired private MixpanelService mixpanelService;
    @Autowired private ProjectService projectService;
    @Autowired private ProcedureDefinitionService procedureDefinitionService;
    @Autowired private ProcedureService procedureService;
    @Autowired private ProcedureAuditEventService procedureAuditEventService;
    @Autowired private NotifyEventAssigneeService notifyEventAssigneeService;
    @Autowired private NotifyProcedureAssigneeService notifyProcedureAssigneeService;

	private Logger logger = Logger.getLogger(AssetService.class);

	public AssetService() {
		super(Asset.class);
	}

    @Transactional(readOnly=true)
    public Long countAssets() {

        QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, securityContext.getTenantSecurityFilter());
        return persistenceService.count(builder);
    }

	@Transactional(readOnly=true)
	public List<AssetsIdentifiedReportRecord> getAssetsIdentified(ChartGranularity granularity, Date fromDate, Date toDate, BaseOrg org) {
		QueryBuilder<AssetsIdentifiedReportRecord> builder = new QueryBuilder<AssetsIdentifiedReportRecord>(Asset.class, securityContext.getUserSecurityFilter());

        NewObjectSelect select = new NewObjectSelect(AssetsIdentifiedReportRecord.class);
		List<String> args = Lists.newArrayList("COUNT(*)");
		args.addAll(reportServiceHelper.getSelectConstructorArgsForGranularity("identified", granularity));
		select.setConstructorArgs(args);

		builder.setSelectArgument(select);
        builder.addGroupByClauses(reportServiceHelper.getGroupByClausesByGranularity(granularity,"identified"));

		builder.addWhere(whereFromTo(fromDate, toDate));
		builder.applyFilter(new OwnerAndDownFilter(org));
		builder.addOrder("identified");

		return persistenceService.findAll(builder);
	}

	private WhereClause<?> whereFromTo(Date fromDate,Date toDate) {
		if (fromDate!=null && toDate!=null) {
			WhereParameterGroup filterGroup = new WhereParameterGroup("filtergroup");
			filterGroup.addClause(WhereClauseFactory.create(Comparator.GE, "from", "identified", fromDate, null, ChainOp.AND));
			filterGroup.addClause(WhereClauseFactory.create(Comparator.LT, "to", "identified", toDate, null, ChainOp.AND));
			return filterGroup;
		}
		if (fromDate!=null) {
			return new WhereParameter<Date>(WhereParameter.Comparator.GE, "from", fromDate);
		}
		if (toDate!=null) {
			return new WhereParameter<Date>(WhereParameter.Comparator.LT, "to", toDate);
		}
		return null;
	}

	/**
	 * Caveat : it is important to know that assets can have a null status.  in that case they will *not* be returned from this service call.
	 * i.e. the groupBy assetStatus.name won't include them.  this is working as designed - just an fyi because it might be confusing if you
	 * have 10 assets identified, but the asset status widget shows < 10 for the same date range.
	 */
	public List<AssetsStatusReportRecord> getAssetsStatus(Date fromDate, Date toDate, BaseOrg org) {
		QueryBuilder<AssetsStatusReportRecord> builder = new QueryBuilder<AssetsStatusReportRecord>(Asset.class, securityContext.getUserSecurityFilter());

		builder.setSelectArgument(new NewObjectSelect(AssetsStatusReportRecord.class, "assetStatus.name", "COUNT(*)"));
		builder.addWhere(whereFromTo(fromDate, toDate));
		builder.addGroupBy("assetStatus.name");
		builder.applyFilter(new OwnerAndDownFilter(org));

		return persistenceService.findAll(builder);
	}

    public List<ScheduleSummaryEntry> getAssetScheduleSummary(List<Long> assetIds) {
        List<Asset> assets = getAssets(assetIds);
        Map<AssetType, ScheduleSummaryEntry> summaryEntryMap = new HashMap<AssetType, ScheduleSummaryEntry>();
        for (Asset asset : assets) {
            AssetType assetType = asset.getType();

            if (!summaryEntryMap.containsKey(assetType)) {
                ScheduleSummaryEntry scheduleSummaryEntry = new ScheduleSummaryEntry();
                scheduleSummaryEntry.setAssetType(assetType);
                summaryEntryMap.put(assetType, scheduleSummaryEntry);
            }

            ScheduleSummaryEntry scheduleSummaryEntry = summaryEntryMap.get(assetType);
            scheduleSummaryEntry.getAssetIds().add(asset.getId());
        }

        return new ArrayList<ScheduleSummaryEntry>(summaryEntryMap.values());
    }

    public List<Asset> getAssets(List<Long> assetIds) {
        QueryBuilder<Asset> query = new QueryBuilder<Asset>(Asset.class, securityContext.getUserSecurityFilter());
        query.addWhere(WhereClauseFactory.create(Comparator.IN, "id", assetIds));
        return persistenceService.findAll(query);
    }

    public Asset findByMobileId(String mobileId) {
    	return findByMobileId(mobileId, false);
    }

    public Asset findByMobileId(String mobileId, boolean withArchived) {
    	QueryBuilder<Asset> builder = createUserSecurityBuilder(Asset.class, withArchived);
    	builder.addWhere(WhereClauseFactory.create("mobileGUID", mobileId));
    	Asset asset = persistenceService.find(builder);
    	return asset;
    }

    public Asset getAsset(Long assetId) {
        return persistenceService.find(Asset.class, assetId);
    }

    public Asset getAsset(Long id, String... postFetchFields) {
        QueryBuilder<Asset> query = createTenantSecurityBuilder(Asset.class);
        query.addSimpleWhere("id", id);
        return PostFetcher.postFetchFields(persistenceService.find(query), postFetchFields);
    }

	public Asset fillInSubAssetsOnAsset(Asset asset) {
		if (asset != null) {
			asset.setSubAssets(findSubAssets(asset));
		}
		return asset;
    }

    @Transactional(readOnly = true)
	public List<SubAsset> findSubAssets(Asset asset) {
		QueryBuilder<SubAsset> subAssetQuery = new QueryBuilder<SubAsset>(SubAsset.class, new OpenSecurityFilter()).addSimpleWhere("masterAsset", asset).addOrder("weight").addOrder("created").addOrder("id");
		List<SubAsset> subAssets = persistenceService.findAll(subAssetQuery);
		return subAssets;
	}

	public Asset parentAsset(Asset asset) {
		QueryBuilder<SubAsset> query = new QueryBuilder<SubAsset>(SubAsset.class, new OpenSecurityFilter()).addSimpleWhere("asset", asset);
		try {
			SubAsset p = persistenceService.find(query);
			if (p != null) {
				return p.getMasterAsset();
			}
			return null;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			logger.error("Could not check if sub asset", e);
			return null;
		}
	}


    public AssetStatus findAssetStatus(Long uniqueID, Long tenantId) {
        Query query = persistenceService.createQuery("FROM "+AssetStatus.class.getName()+" st WHERE st.id = :uniqueID AND st.tenant.id = :tenantId");
        query.setParameter("uniqueID", uniqueID);
        query.setParameter("tenantId", tenantId);
        AssetStatus obj = null;
        try {
            obj = (AssetStatus) query.getSingleResult();
        } catch (NoResultException e) {
            obj = null;
        }
        return obj;
    }

    public boolean rfidExists(String rfidNumber) {
        return rfidExists(rfidNumber, null);
    }

    public boolean rfidExists(String rfidNumber, Long excludingId) {
        return duplicateFieldExists("rfidNumber", rfidNumber, excludingId);

    }

    public boolean identifierExists(String identifier) {
        return duplicateFieldExists("identifier", identifier, null);
    }

    public boolean identifierExists(String identifier, Long excludingId) {
        return duplicateFieldExists("identifier", identifier, excludingId);
    }

    private boolean duplicateFieldExists(String field, String value, Long excludingId) {
        // null or zero-length rfidNumbers are never duplicates
        if (value == null || value.trim().length() == 0) {
            return false;
        }

        QueryBuilder<Asset> rfidQuery = createTenantSecurityBuilder(Asset.class);
        rfidQuery.addSimpleWhere(field, value.toUpperCase());

        if (excludingId != null) {
            rfidQuery.addWhere(WhereClauseFactory.create(Comparator.NE, "id", excludingId));
        }

        try {
            return persistenceService.exists(rfidQuery);
        } catch (NoResultException e) {
            return false;
        }
    }

    public Asset create(Asset asset, User modifiedBy) throws SubAssetUniquenessException {
        runAssetSavePreRecs(asset, modifiedBy);

        AssetSaver saver = new AssetSaver();
        saver.setModifiedBy(modifiedBy);

        asset = saver.update(getEntityManager(), asset);

        saveSubAssets(asset);

        asset.setSubAssets(findSubAssets(asset));

        mixpanelService.sendEvent(MixpanelService.NEW_ASSET_CREATED);

        return asset;
    }

    private void saveSubAssets(Asset asset) {
        for (SubAsset subAsset : asset.getSubAssets()) {
            persistenceService.update(subAsset);
        }
    }

    private void runAssetSavePreRecs(Asset asset, User modifiedBy) throws SubAssetUniquenessException {
        moveRfidFromAssets(asset, modifiedBy);
        processSubAssets(asset, modifiedBy);
    }

    public Asset update(Asset asset, User modifiedBy) throws SubAssetUniquenessException {
        asset.touch();
        runAssetSavePreRecs(asset, modifiedBy);

        saveSubAssets(asset);

        AssetSaver saver = new AssetSaver();
        saver.setModifiedBy(modifiedBy);
        asset = saver.update(getEntityManager(), asset);

        updateSchedulesOwnership(asset);
        return asset;
    }

    private void updateSchedulesOwnership(Asset asset) {
        // just load the list because in order to paginate
        QueryBuilder<Long> schedules = new QueryBuilder<Long>(Event.class, new TenantOnlySecurityFilter(asset.getTenant().getId()))
                .setSimpleSelect("id")
                .addSimpleWhere("asset", asset)
                .addSimpleWhere("state", Archivable.EntityState.ACTIVE)
                .addWhere(Comparator.EQ, "workflowState", "workflowState", WorkflowState.OPEN);

        for (Long id : persistenceService.findAll(schedules)) {
            // We must find with tenant here otherwise users in groups may not be able to update some schedules ownership
            QueryBuilder<ThingEvent> builder = createTenantSecurityBuilder(ThingEvent.class).addSimpleWhere("id", id);
            ThingEvent schedule = persistenceService.find(builder);

            schedule.setOwner(asset.getOwner());
            schedule.setAdvancedLocation(asset.getAdvancedLocation());

            persistenceService.update(schedule);
        }
    }

    private void processSubAssets(Asset asset, User modifiedBy) throws SubAssetUniquenessException {

        checkForUniqueSubAssets(asset);
        clearOldSubAssets(asset);

        long weight = 0;
        for (SubAsset subAsset : asset.getSubAssets()) {

            detachFromPreviousParent(asset, subAsset, modifiedBy);

            subAsset.getAsset().setOwner(asset.getOwner());
            subAsset.getAsset().setAdvancedLocation(asset.getAdvancedLocation());
            subAsset.getAsset().setAssignedUser(asset.getAssignedUser());
            subAsset.getAsset().setAssetStatus(asset.getAssetStatus());
            subAsset.setWeight(weight);

            AssetSaver saver = new AssetSaver();
            saver.setModifiedBy(modifiedBy);
            saver.update(getEntityManager(), subAsset.getAsset());

            weight++;
        }
    }

    //Delete subassets that are no longer in the subasset list
    private void clearOldSubAssets(Asset asset) {
        if (!asset.isNew()) {
            List<SubAsset> existingSubAssets = persistenceService.findAll(new QueryBuilder<SubAsset>(SubAsset.class, new OpenSecurityFilter()).addSimpleWhere("masterAsset", asset));
            for (SubAsset subAsset : existingSubAssets) {
                if (asset.getSubAssets().contains(subAsset)) {
                    SubAsset subAssetToUpdate = asset.getSubAssets().get(asset.getSubAssets().indexOf(subAsset));
                    subAssetToUpdate.setCreated(subAsset.getCreated());
                    subAssetToUpdate.setId(subAsset.getId());
                } else {
                    persistenceService.delete(subAsset);
                }
            }
        }
    }

    private void detachFromPreviousParent(Asset asset, SubAsset subAsset, User modifiedBy) {
        Asset parentAsset = parentAsset(subAsset.getAsset());

        if (parentAsset != null && !parentAsset.equals(asset)) {
            try {
                fillInSubAssetsOnAsset(parentAsset);
                QueryBuilder<SubAsset> query = new QueryBuilder<SubAsset>(SubAsset.class, new OpenSecurityFilter()).addSimpleWhere("asset", subAsset.getAsset());
                SubAsset subAssetToRemove = persistenceService.find(query);
                parentAsset.getSubAssets().remove(subAssetToRemove);
                persistenceService.delete(subAssetToRemove);
                update(parentAsset, modifiedBy);
            } catch (SubAssetUniquenessException e) {
                logger.error("parent asset is in an invalid state in the database", e);
                throw new RuntimeException("parent asset is in an invalid state in the database", e);
            }
        }
    }

    public void checkForUniqueSubAssets(Asset asset) throws SubAssetUniquenessException {
        Set<SubAsset> uniqueSubAssets = new HashSet<SubAsset>(asset.getSubAssets());
        if (asset.getSubAssets().size() != uniqueSubAssets.size()) {
            throw new SubAssetUniquenessException();
        }
    }

    /**
     * creates the asset serial and updates the given users add
     * assetHistory.
     */
    @LegacyMethod
    public Asset createWithHistory(Asset asset, User modifiedBy) throws SubAssetUniquenessException {
        asset = create(asset, modifiedBy);

        AddAssetHistory addAssetHistory = getAddAssetHistory(modifiedBy.getId());

        if (addAssetHistory == null) {
            addAssetHistory = new AddAssetHistory();
            addAssetHistory.setTenant(modifiedBy.getTenant());
            addAssetHistory.setUser(modifiedBy);
        }

        addAssetHistory.setOwner(asset.getOwner());
        addAssetHistory.setAssetType(asset.getType());
        addAssetHistory.setAssetStatus(asset.getAssetStatus());
        addAssetHistory.setPurchaseOrder(asset.getPurchaseOrder());
        addAssetHistory.setLocation(asset.getAdvancedLocation());
        addAssetHistory.setInfoOptions(new ArrayList<InfoOptionBean>(asset.getInfoOptions()));
        addAssetHistory.setAssignedUser(asset.getAssignedUser());

        getEntityManager().merge(addAssetHistory);

        return asset;
    }

    public List<Asset> findAssetByIdentifiersForNewSmartSearch(SecurityFilter filter, String searchValue, AssetType assetType) {
        String queryString = "SELECT * FROM assets p WHERE (MATCH (p.identifier, p.rfidNumber, p.customerRefNumber) AGAINST ('" + searchValue + "'))";

        if (assetType != null) {
            queryString += "AND p.type = " + assetType + " ";
        }

        queryString += "AND p.TENANT_ID = " + filter.getTenantId() + " AND p.state='ACTIVE' ORDER BY p.created";

        Query query = persistenceService.createSQLQuery(queryString, Asset.class);

        return query.getResultList();
    }

    private void moveRfidFromAssets(Asset asset, User modifiedBy) {
        AssetSaver saver = new AssetSaver();
        saver.setModifiedBy(modifiedBy);

        if (rfidExists(asset.getRfidNumber(), asset.getTenant().getId())) {
            Collection<Asset> duplicateRfidAssets = findAssetsByRfidNumber(asset.getRfidNumber());
            for (Asset duplicateRfidAsset : duplicateRfidAssets) {
                if (!duplicateRfidAsset.getId().equals(asset.getId())) {
                    duplicateRfidAsset.setRfidNumber(null);

                    saver.update(getEntityManager(), duplicateRfidAsset);

                    String auditMessage = "Moving RFID [" + asset.getRfidNumber() + "] from Asset [" + duplicateRfidAsset.getId() + ":" + duplicateRfidAsset.getIdentifier() + "] to [" + asset.getId() + ":"
                            + asset.getIdentifier() + "]";
                    logger.info(auditMessage);
                }
            }
        }
    }

    public List<Asset> findAssetsByRfidNumber(String rfidNumber) {
        if (rfidNumber == null) {
            return null;
        }

        QueryBuilder<Asset> qBuilder = createTenantSecurityBuilder(Asset.class);
        qBuilder.addSimpleWhere("rfidNumber", rfidNumber.trim());

        return persistenceService.findAll(qBuilder);
    }

    @LegacyMethod
    public AddAssetHistory getAddAssetHistory(Long rFieldidUser) {
        Query query = persistenceService.createQuery("from "+ AddAssetHistory.class.getName()+" aph where aph.user.id = :rFieldidUser");
        query.setParameter("rFieldidUser", rFieldidUser);

        @SuppressWarnings("unchecked")
        List<AddAssetHistory> addAssetHistoryList = query.getResultList();

        if (addAssetHistoryList != null) {
            if (addAssetHistoryList.size() > 0) {
                return addAssetHistoryList.get(0);
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @LegacyMethod
    public Collection<AssetExtension> getAssetExtensions(Long tenantId) {
        Query query = persistenceService.createQuery("from "+ AssetExtension.class.getName()+" ase where ase.tenantId = :tenantId");
        query.setParameter("tenantId", tenantId);

        return query.getResultList();
    }

    @LegacyMethod
    public boolean duplicateIdentifier(String identifier, Long uniqueID, Tenant tenant) {
        String queryString = "select count(a.id) from "+Asset.class.getName()+" a where a.tenant = :tenant " + " and lower(a.identifier) = :identifier";

        if (uniqueID != null) {
            queryString += " AND a.id <> :uniqueId ";
        }

        Query query = persistenceService.createQuery(queryString).setParameter("tenant", tenant).setParameter("identifier", identifier.trim().toLowerCase());

        if (uniqueID != null) {
            query.setParameter("uniqueId", uniqueID);
        }

        Long value = (Long) query.getSingleResult();

        return value != 0L;
    }

    @LegacyMethod
    public ThingEvent findLastEvents(Asset asset, SecurityFilter securityFilter) {
        Query eventQuery = createAllEventQuery(asset, securityFilter, false, true);
        ThingEvent event = null;
        try {
            event = (ThingEvent) eventQuery.getSingleResult();
        } catch (NoResultException e) {
        }
        return event;
    }

    @LegacyMethod
    public Long countAllEvents(Asset asset, SecurityFilter securityFilter) {
        Long count = countAllLocalEvents(asset, securityFilter);
        return count;
    }

    @LegacyMethod
    public Long countAllLocalEvents(Asset asset, SecurityFilter securityFilter) {
        Query eventQuery = createAllEventQuery(asset, securityFilter, true);
        return (Long)eventQuery.getSingleResult();
    }

    @LegacyMethod
    private Query createAllEventQuery(Asset asset, SecurityFilter securityFilter, boolean count) {
        return createAllEventQuery(asset, securityFilter, count, false);
    }

    @LegacyMethod
    private Query createAllEventQuery(Asset asset, SecurityFilter securityFilter, boolean count, boolean lastEvent) {
        String query = "from "+ThingEvent.class.getName()+" event  left join event.asset " + "WHERE  " + securityFilter.produceWhereClause(Event.class, "event")
                + " AND event.asset = :asset AND event.state= :activeState AND event.workflowState= :completed";
        if (count) {
            query = "SELECT count(event.id) " + query;
        } else {
            query = "SELECT event " + query;
        }

        User user = persistenceService.find(User.class, securityFilter.getUserId());

        Collection<User> visibleUsers = ThreadLocalInteractionContext.getInstance().getVisibleUsers();

        if (!user.getGroups().isEmpty()) {
            query += " AND event.performedBy  in (:visibleUsers) ";
            query += " AND (event.assignedGroup is null or event.assignedGroup in (:groupList) ) ";
        }

        if (!count)
            query += " ORDER BY event.completedDate DESC, event.created ASC";

        Query eventQuery = persistenceService.createQuery(query);

        if (lastEvent) {
            eventQuery.setMaxResults(1);
        }


        eventQuery.setParameter("asset", asset);
        securityFilter.applyParameters(eventQuery, Event.class);
        eventQuery.setParameter("activeState", Archivable.EntityState.ACTIVE);
        eventQuery.setParameter("completed", WorkflowState.COMPLETED);

        if (!user.getGroups().isEmpty()) {
            eventQuery.setParameter("visibleUsers", visibleUsers);
            eventQuery.setParameter("groupList", user.getGroups());
        }

        return eventQuery;
    }

    public Asset createAssetWithServiceTransaction(String transactionGUID, Asset asset, User modifiedBy) throws TransactionAlreadyProcessedException, SubAssetUniquenessException {
        asset = create(asset, modifiedBy);

        transactionService.completeAssetTransaction(transactionGUID, asset.getTenant());

        return asset;
    }

    public Asset findLocalAssetFor(Asset asset) {
        QueryBuilder<Asset> queryBuilder = createUserSecurityBuilder(Asset.class);

        queryBuilder.addSimpleWhere("networkId", asset.getNetworkId());

        return persistenceService.find(queryBuilder);
    }

    public List<Asset> search(String search, int threshold) {
        QueryBuilder<Asset> builder = createUserSecurityBuilder(Asset.class);
        String assetTypeTerm = getAssetTypeTerm(search);
        String term = getIdentifierTerm(search);

        if (StringUtils.isNotBlank(assetTypeTerm)) {
            WhereParameterGroup group = new WhereParameterGroup("type");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "type_name", "type.name", assetTypeTerm, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "type_group_name", "type.group.name", assetTypeTerm, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            builder.addWhere(group);
        }

        if (StringUtils.isNotBlank(term)) {
            WhereParameterGroup group = new WhereParameterGroup("smartsearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "identifier", "identifier", term, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "rfidNumber", "rfidNumber", term, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "customerRefNumber", "customerRefNumber", term, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            builder.addWhere(group);
        }

        builder.setLimit(threshold*4);
        List<Asset> results = persistenceService.findAll(builder);
        return new PrioritizedList<Asset>(results, threshold);
    }

    private String getAssetTypeTerm(String search) {
        int index = search.indexOf(":");
        if (index!=-1) {
            return search.substring(0,index);
        }
        return null;
    }

    private String getIdentifierTerm(String search) {
        int index = search.indexOf(":");
        if (index!=-1) {
            return search.substring(index+1);
        }
        return search;
    }

    public boolean hasAssetAttachments(Long id) {
        QueryBuilder<AssetAttachment> builder = new QueryBuilder<AssetAttachment>(AssetAttachment.class, securityContext.getTenantSecurityFilter());
        builder.addSimpleWhere("asset.id", id);

        Long size = persistenceService.count(builder);

        if(size > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<AssetAttachment> findAssetAttachments(Asset asset) {
        if (asset == null) {
            throw new InvalidArgumentException("you must have an asset to load asset attachments");
        }
        QueryBuilder<AssetAttachment> builder = new QueryBuilder<AssetAttachment>(AssetAttachment.class, securityContext.getTenantSecurityFilter());
        builder.addSimpleWhere("asset", asset).addOrder("id");

        return persistenceService.findAll(builder);
    }

    public List<Asset> search(int threshold) {
        // if no search term given for search, just pull up the most recently modified ones.  (arbitrary decision).
        QueryBuilder<Asset> builder = createUserSecurityBuilder(Asset.class);
        builder.setLimit(threshold*4);
        builder.getOrderArguments().add(new OrderClause("modified", false));
        List<Asset> results = persistenceService.findAll(builder);
        return new PrioritizedList<Asset>(results, threshold);
    }

    public boolean hasLinkedAssets(Asset asset) {
        QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
        builder.addWhere(WhereClauseFactory.create("networkId", asset.getNetworkId()));
        builder.addWhere(WhereClauseFactory.create(Comparator.NE, "id", asset.getId()));

        return persistenceService.exists(builder);
    }

    public Map<Long, Long> getTenantsLast30DaysCount(Map<Tenant,PrimaryOrg> tenants) {
        QueryBuilder<Tenant30DayCountRecord> builder = new QueryBuilder<Tenant30DayCountRecord>(Asset.class, new OpenSecurityFilter());

        NewObjectSelect select = new NewObjectSelect(Tenant30DayCountRecord.class);
        select.setConstructorArgs(Lists.newArrayList("obj.tenant", "COUNT(*)"));
        builder.setSelectArgument(select);

        builder.addWhere(WhereClauseFactory.create(Comparator.GT, "created", LocalDate.now().minusDays(30).toDate()));
        builder.addWhere(WhereClauseFactory.create(Comparator.IN, "tenant", tenants.keySet()));

        builder.addGroupBy("tenant.id");

        List<Tenant30DayCountRecord> data = persistenceService.findAll(builder);

        Map<Long, Long> result = Maps.newHashMap();

        for (Tenant30DayCountRecord record: data) {
            result.put(tenants.get(record.getTenant()).getId(), record.getCount());
        }

        return result;
    }

    public Asset createAssetWithHistory() {
        Asset asset = new Asset();

        AddAssetHistory history = getAddAssetHistory();

        if (history != null) {
            asset.setType(history.getAssetType());
            asset.setAssetStatus(history.getAssetStatus());
            asset.setAdvancedLocation(history.getLocation());
            asset.setOwner(history.getOwner());
            asset.setAssignedUser(history.getAssignedUser());
            asset.setInfoOptions(new TreeSet<InfoOptionBean>(history.getInfoOptions()));
            asset.setPurchaseOrder(history.getPurchaseOrder());
        } else {
            asset.setType(assetTypeService.getAssetTypes(null).iterator().next());
            asset.setOwner(getCurrentUser().getOwner());
        }

        asset.setPublished(orgService.getPrimaryOrgForTenant(getCurrentTenant().getId()).isAutoPublish());

        return asset;
    }

    public AddAssetHistory getAddAssetHistory() {
        QueryBuilder<AddAssetHistory> query = createUserSecurityBuilder(AddAssetHistory.class);
        query.addSimpleWhere("user", getCurrentUser());

        List<AddAssetHistory> addAssetHistoryList = persistenceService.findAll(query);

        if (addAssetHistoryList != null) {
            if (addAssetHistoryList.size() > 0) {
                return addAssetHistoryList.get(0);
            }
        }

        return null;
    }

    public Asset createAssetFromOrder(Long lineItemId) {

        LineItem lineItem = persistenceService.find(LineItem.class, lineItemId);

        // find the asset code mapping by asset code. This will return the
        // default asset code if one could not be found.
        AssetCodeMapping assetCodeMapping = assetCodeMappingService.getAssetCodeByAssetCodeAndTenant(lineItem.getAssetCode());
        Asset asset;

        if (assetCodeMapping.getAssetInfo() != null && !assetCodeMapping.getAssetInfo().getName().equals(ConfigEntry.DEFAULT_PRODUCT_TYPE_NAME.getDefaultValue())) {
            asset = new Asset();
            asset.setType(assetCodeMapping.getAssetInfo());

            if (assetCodeMapping.getInfoOptions() != null && !assetCodeMapping.getInfoOptions().isEmpty()) {
                asset.setInfoOptions(new TreeSet<InfoOptionBean>(assetCodeMapping.getInfoOptions()));
            }
        } else {
            asset = createAssetWithHistory();
        }

        asset.setCustomerRefNumber(assetCodeMapping.getCustomerRefNumber());
        asset.setShopOrder(lineItem);
        asset.setOwner(lineItem.getOrder().getOwner());
        asset.setPurchaseOrder(lineItem.getOrder().getPoNumber());
        asset.setPublished(orgService.getPrimaryOrgForTenant(getCurrentTenant().getId()).isAutoPublish());

        return asset;
    }

    public Asset findAssetWithInfoOptions(Long id) {
        Asset asset = getAsset(id, Asset.POST_FETCH_ALL_PATHS);
        List<InfoOptionBean> infoList = asset.getOrderedInfoOptionList();

        return asset;
    }

    public Asset archive(Asset asset, User archivedBy) throws UsedOnMasterEventException {
        asset = persistenceService.reattach(asset);
        asset = fillInSubAssetsOnAsset(asset);
        if (!testArchive(asset).validToDelete()) {
            throw new UsedOnMasterEventException();
        }

        if (asset.isMasterAsset()) {
            for (SubAsset subAsset : asset.getSubAssets()) {
                persistenceService.delete(subAsset);
            }
            asset.getSubAssets().clear();
        }

        Asset parentAsset = parentAsset(asset);
        if (parentAsset != null) {
            SubAsset subAssetToRemove = parentAsset.getSubAssets().get(parentAsset.getSubAssets().indexOf(new SubAsset(asset, parentAsset)));
            persistenceService.delete(subAssetToRemove);
            parentAsset.getSubAssets().remove(subAssetToRemove);
            save(parentAsset, archivedBy);
        }

        asset.archiveEntity();
        asset.archiveIdentifier();

        archiveEvents(asset);
        detachFromProjects(asset);

        archiveProcedures(asset);
        archiveProcedureAudits(asset);

        return save(asset, archivedBy);
    }

    private void archiveProcedureAudits(Asset asset) {
        if(asset.getType().hasProcedures()) {

            for (ProcedureAuditEvent procedureAuditEvent: procedureAuditEventService.getAuditProceduresByAsset(asset)) {
                procedureAuditEventService.retireEvent(procedureAuditEvent);
            }

        }
    }

    private void archiveProcedures(Asset asset) {
        if(asset.getType().hasProcedures()) {

            for (ProcedureDefinition procedureDefinition: procedureDefinitionService.getAllProcedureDefinitionsForAsset(asset)) {
                procedureDefinitionService.archiveProcedureDefinition(procedureDefinition);
            }

            for (Procedure procedure: procedureService.getAllProcedures(asset)) {
                procedureService.archiveProcedure(procedure);
                notifyProcedureAssigneeService.removeNotificationForProcedure(procedure);
            }
        }
    }

    protected Asset save(Asset asset, User modifiedBy) {
        AssetSaver assetSaver = new AssetSaver();
        assetSaver.setModifiedBy(modifiedBy);

        asset = assetSaver.update(getEntityManager(), asset);

        return asset;
    }

    public AssetRemovalSummary testArchive(Asset asset) {
        AssetRemovalSummary summary = new AssetRemovalSummary(asset);
        try {
            QueryBuilder<Event> eventCount = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter());
            eventCount.setCountSelect().addSimpleWhere("asset", asset).addSimpleWhere("state", Archivable.EntityState.ACTIVE);
            summary.setEventsToDelete(persistenceService.count(eventCount));

            QueryBuilder<Event> scheduleCount = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter());
            scheduleCount.setCountSelect().addSimpleWhere("asset", asset).addSimpleWhere("state", Archivable.EntityState.ACTIVE).addSimpleWhere("workflowState", WorkflowState.OPEN);
            summary.setSchedulesToDelete(persistenceService.count(scheduleCount));

            String subEventQuery = "select count(event) From " + Event.class.getName() + " event, IN( event.subEvents ) subEvent WHERE subEvent.asset = :asset AND event.state = :activeState ";
            Query subEventCount = getEntityManager().createQuery(subEventQuery);
            subEventCount.setParameter("asset", asset).setParameter("activeState", Archivable.EntityState.ACTIVE);
            summary.setAssetUsedInMasterEvent((Long) subEventCount.getSingleResult());
            asset = fillInSubAssetsOnAsset(asset);
            summary.setSubAssetsToDetach((long) asset.getSubAssets().size());

            summary.setDetachFromMaster(parentAsset(asset) != null);

            String partOfProjectQuery = "select count(p) From Project p, IN( p.assets ) s WHERE s = :asset";
            Query partOfProjectCount = getEntityManager().createQuery(partOfProjectQuery);
            partOfProjectCount.setParameter("asset", asset);
            summary.setProjectToDetachFrom((Long) partOfProjectCount.getSingleResult());

            QueryBuilder<ProcedureDefinition> procedureDefCount = createTenantSecurityBuilder(ProcedureDefinition.class);
            procedureDefCount.addSimpleWhere("asset", asset);
            summary.setProcedureDefinitionsToDelete(persistenceService.count(procedureDefCount));

            QueryBuilder<Procedure> procedureCount = createTenantSecurityBuilder(Procedure.class);
            procedureCount.addSimpleWhere("asset", asset);
            summary.setProceduresToDelete(persistenceService.count(procedureCount));

        } catch (InvalidQueryException e) {
            logger.error("bad summary query", e);
            summary = null;
        }
        return summary;
    }

    private void archiveEvents(Asset asset) {
        Set<Long> eventIdsForAsset = getEventIdsForAsset(asset);
        EventListArchiver archiver = new EventListArchiver(eventIdsForAsset);
        notifyEventAssigneeService.removeNotificationsForEvents(Lists.newArrayList(eventIdsForAsset));
        archiver.archive(getEntityManager());
    }

    private Set<Long> getEventIdsForAsset(Asset asset) {
        QueryBuilder<Long> idBuilder = new QueryBuilder<Long>(Event.class, new OpenSecurityFilter());
        idBuilder.setSimpleSelect("id");
        idBuilder.addWhere(WhereClauseFactory.create("asset.id", asset.getId()));

        return new TreeSet<Long>(persistenceService.findAll(idBuilder));
    }

    private void detachFromProjects(Asset asset) {
        for (Project project : asset.getProjects()) {
            projectService.detachAsset(asset, project);
        }
    }
}
