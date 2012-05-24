package com.n4systems.fieldid.service.asset;

import com.google.common.collect.Lists;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.fieldid.LegacyMethod;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.ReportServiceHelper;
import com.n4systems.fieldid.service.transaction.TransactionService;
import com.n4systems.model.*;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.asset.AssetSaver;
import com.n4systems.model.asset.ScheduleSummaryEntry;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.reporting.AssetsIdentifiedReportRecord;
import com.n4systems.services.reporting.AssetsStatusReportRecord;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.collections.PrioritizedList;
import com.n4systems.util.collections.UniquePrioritizer;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import rfid.ejb.entity.AddAssetHistory;
import rfid.ejb.entity.AssetExtension;
import rfid.ejb.entity.InfoOptionBean;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

@Transactional
public class AssetService extends FieldIdPersistenceService {
	
	@Autowired private ReportServiceHelper reportServiceHelper;
    @Autowired private TransactionService transactionService;
	
	private Logger logger = Logger.getLogger(AssetService.class);
			
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
		args.addAll(reportServiceHelper.getSelectConstructorArgsForGranularity("identified", granularity, null));
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
    	QueryBuilder<Asset> builder = createUserSecurityBuilder(Asset.class);
    	builder.addWhere(WhereClauseFactory.create("mobileGUID", mobileId));
    	Asset asset = persistenceService.find(builder);
    	return asset;
    }
    
    public Asset getAsset(Long assetId) {
        return persistenceService.find(Asset.class, assetId);
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

    @LegacyMethod
    public boolean rfidExists(String rfidNumber, Long tenantId) {
        return rfidExists(rfidNumber, tenantId, null);
    }

    @LegacyMethod
    public boolean rfidExists(String rfidNumber, Long tenantId, Long uniqueID) {
        long rfidCount = 0;
        String uniqueIDClause = "";
        // null or zero-length rfidNumbers are never duplicates
        if (rfidNumber == null || rfidNumber.trim().length() == 0) {
            return false;
        }

        if (uniqueID != null) {
            uniqueIDClause = " and p.id <> :id";
        }

        Query query = persistenceService.createQuery("select count(p) from "+Asset.class.getName()+" p where p.state = :activeState AND p.rfidNumber = :rfidNumber" + uniqueIDClause
                + " and p.tenant.id = :tenantId group by p.rfidNumber");

        query.setParameter("rfidNumber", rfidNumber.toUpperCase());
        query.setParameter("tenantId", tenantId);
        query.setParameter("activeState", Archivable.EntityState.ACTIVE);

        if (uniqueID != null) {
            query.setParameter("id", uniqueID);
        }

        try {
            rfidCount = (Long) query.getSingleResult();
        } catch (NoResultException e) {
            rfidCount = 0;
        }

        return (rfidCount > 0) ? true : false;
    }

    public Asset create(Asset asset, User modifiedBy) throws SubAssetUniquenessException {
        runAssetSavePreRecs(asset, modifiedBy);

        AssetSaver saver = new AssetSaver();
        saver.setModifiedBy(modifiedBy);

        asset = saver.update(getEntityManager(), asset);

        saveSubAssets(asset);

        asset.setSubAssets(findSubAssets(asset));

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
        QueryBuilder<Long> schedules = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter())
                .setSimpleSelect("id")
                .addSimpleWhere("asset", asset)
                .addWhere(Comparator.NE, "status", "status", EventSchedule.ScheduleStatus.COMPLETED);

        for (Long id : persistenceService.findAll(schedules)) {
            EventSchedule schedule = persistenceService.find(EventSchedule.class, id);

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
    public Event findLastEvents(Asset asset, SecurityFilter securityFilter) {
        Query eventQuery = createAllEventQuery(asset, securityFilter, false, true);
        Event event = null;
        try {
            event = (Event) eventQuery.getSingleResult();
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
        String query = "from "+Event.class.getName()+" event  left join event.asset " + "WHERE  " + securityFilter.produceWhereClause(Event.class, "event")
                + " AND event.asset = :asset AND event.state= :activeState";
        if (count) {
            query = "SELECT count(event.id) " + query;
        } else {
            query = "SELECT event " + query;
        }

        if (!count)
            query += " ORDER BY event.schedule.completedDate DESC, event.created ASC";

        Query eventQuery = persistenceService.createQuery(query);

        if (lastEvent) {
            eventQuery.setMaxResults(1);
        }

        eventQuery.setParameter("asset", asset);
        securityFilter.applyParameters(eventQuery, Event.class);
        eventQuery.setParameter("activeState", Archivable.EntityState.ACTIVE);

        return eventQuery;
    }

    public Asset createAssetWithServiceTransaction(String transactionGUID, Asset asset, User modifiedBy) throws TransactionAlreadyProcessedException, SubAssetUniquenessException {
        asset = create(asset, modifiedBy);

        transactionService.completeAssetTransaction(transactionGUID, asset.getTenant());

        return asset;
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
        return new PrioritizedList<Asset>(results, new UniquePrioritizer("type"),threshold);
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
        return new PrioritizedList<Asset>(results, new UniquePrioritizer("type"),threshold);
    }

    public boolean hasLinkedAssets(Asset asset) {
        QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
        builder.addWhere(WhereClauseFactory.create("networkId", asset.getNetworkId()));
        builder.addWhere(WhereClauseFactory.create(Comparator.NE, "id", asset.getId()));

        return persistenceService.exists(builder);
    }
}
