package com.n4systems.fieldid.service.procedure;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.exceptions.loto.AnnotatedImageGenerationException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.ReportServiceHelper;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.service.uuid.AtomicLongService;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.*;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.security.Permissions;
import com.n4systems.services.date.DateService;
import com.n4systems.util.DateHelper;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.DateChartable;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import rfid.ejb.entity.InfoOptionBean;

import java.util.*;


public class ProcedureDefinitionService extends FieldIdPersistenceService {

    private static final Logger logger=Logger.getLogger(ProcedureDefinitionService.class);

    @Autowired private ReportServiceHelper reportServiceHelper;
    @Autowired private UserGroupService userGroupService;
    @Autowired private S3Service s3Service;
    @Autowired private DateService dateService;
    @Autowired private AtomicLongService atomicLongService;
    @Autowired private RecurringScheduleService recurringScheduleService;
    @Autowired private SvgGenerationService svgGenerationService;
    @Autowired private AssetService assetService;
    @Autowired private UserService userService;


    public Boolean hasPublishedProcedureDefinition(Asset asset) {
        return persistenceService.exists(getPublishedProcedureDefinitionQuery(asset));
    }

    public ProcedureDefinition findProcedureDefinitionByMobileId(String mobileId) {
        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("mobileId", mobileId);

        ProcedureDefinition returnMe = persistenceService.find(query);

        return returnMe;
    }

    public ProcedureDefinition getPublishedProcedureDefinition(Asset asset, Long familyId) {
        return persistenceService.find(getPublishedProcedureDefinitionQuery(asset, familyId));
    }

    public List<ProcedureDefinition> getAllPublishedProcedures(Asset asset) {
          return persistenceService.findAll(getPublishedProcedureDefinitionQuery(asset));
    }

    /**
     * A convenience method to retrieve a ProcedureDefinition by its ID.
     *
     * @param id - A <b>Long</b> value representing the ID of the ProcedureDefinition you wish to retrieve.
     * @return A <b>ProcedureDefinition</b> object representing the desired definition.
     */
    public ProcedureDefinition getProcedureDefinitionById(Long id) {
        return persistenceService.find(ProcedureDefinition.class, id);
    }

    public Boolean hasPublishedProcedureCode(ProcedureDefinition procedureDefinition) {
        QueryBuilder<ProcedureDefinition> query = getPublishedProcedureDefinitionQuery(procedureDefinition.getAsset());
        query.addSimpleWhere("procedureCode", procedureDefinition.getProcedureCode());
        return persistenceService.exists(query);
    }

    public Boolean hasRecurringSchedule(ProcedureDefinition procedureDefinition) {
        Boolean returnMe = false;

        QueryBuilder<Procedure> procedureQuery = createTenantSecurityBuilder(Procedure.class);
        procedureQuery.addSimpleWhere("type",
                                      procedureDefinition);
        //This should result in the query only looking for Procedures where recurringEvent is NOT null (ie. scheduled)
        procedureQuery.addWhere(WhereParameter.Comparator.NOTNULL,
                                "recurringEvent",
                                "recurringEvent",
                                null);

        //Doesn't matter if there's 1 or 1 million... anything scheduled is what we're looking for.
        procedureQuery.setLimit(1);

        returnMe = persistenceService.exists(procedureQuery);

        if(!returnMe) {
            QueryBuilder<ProcedureAuditEvent> procedureAuditEventQuery =
                    createTenantSecurityBuilder(ProcedureAuditEvent.class);

            procedureAuditEventQuery.addSimpleWhere("procedureDefinition",
                                                    procedureDefinition);

            procedureAuditEventQuery.addWhere(WhereParameter.Comparator.NOTNULL,
                                              "recurringEvent",
                                              "recurringEvent",
                                              null);

            procedureQuery.setLimit(1);

            returnMe = persistenceService.exists(procedureAuditEventQuery);
        }

        return returnMe;
    }

    private QueryBuilder<ProcedureDefinition> getPublishedProcedureDefinitionQuery(Asset asset) {
        return getPublishedProcedureDefinitionQuery(asset, null);
    }

    private QueryBuilder<ProcedureDefinition> getPublishedProcedureDefinitionQuery(Asset asset, Long familyId) {
        QueryBuilder<ProcedureDefinition> query = createTenantSecurityBuilder(ProcedureDefinition.class);

        query.addSimpleWhere("asset", asset);
        if(familyId != null) {
            query.addSimpleWhere("familyId", familyId);
        }
        query.addSimpleWhere("publishedState", PublishedState.PUBLISHED);

        return query;
    }

    public void saveProcedureDefinitionDraft(ProcedureDefinition procedureDefinition) {
        if (procedureDefinition.getRevisionNumber() == null) {
            procedureDefinition.setRevisionNumber(generateRevisionNumber(procedureDefinition));
        }
        if (procedureDefinition.getFamilyId() == null) {
            procedureDefinition.setFamilyId(generateFamilyId(procedureDefinition.getAsset()));
        }

        //increase the count if it's a newly created procedure definition
        if(procedureDefinition.getID() == null) {
            assetService.increaseProcedureCount(procedureDefinition.getAsset());
        }
        persistenceService.saveOrUpdate(procedureDefinition);
        for (ProcedureDefinitionImage image:procedureDefinition.getImages()) {
            s3Service.finalizeProcedureDefinitionImageUpload(image);
        }
    }

    public void saveProcedureDefinition(ProcedureDefinition procedureDefinition) throws AnnotatedImageGenerationException {
        saveProcedureDefinitionDraft(procedureDefinition);

        if (isProcedureApprovalRequiredForCurrentUser()) {
            procedureDefinition.setPublishedState(PublishedState.WAITING_FOR_APPROVAL);
            procedureDefinition.setAuthorizationNotificationSent(false);
            persistenceService.update(procedureDefinition);
        } else {
            publishProcedureDefinition(procedureDefinition);
        }
    }

    public void saveProcedureDefinitionRejection(ProcedureDefinition procedureDefinition, String rejectedReason) {

      procedureDefinition.setPublishedState(PublishedState.REJECTED);
      procedureDefinition.setRejectedBy(getCurrentUser());
      procedureDefinition.setRejectedDate(dateService.nowUTC().toDate());
      procedureDefinition.setRejectedReason(rejectedReason);
      persistenceService.update(procedureDefinition);
      //increase the count if it's a newly created procedure definition
      if(procedureDefinition.getID() == null) {
          assetService.increaseProcedureCount(procedureDefinition.getAsset());
      }

    }

    /*package protected for testing purposes*/
    Long generateRevisionNumber(ProcedureDefinition procedureDefinition) {
        QueryBuilder<Long> query = new QueryBuilder<Long>(ProcedureDefinition.class, securityContext.getTenantSecurityFilter());
        query.addSimpleWhere("asset", procedureDefinition.getAsset());
        if(procedureDefinition.getFamilyId() != null) {
            query.addSimpleWhere("familyId", procedureDefinition.getFamilyId());
            query.setSelectArgument(new MaxSelect("revisionNumber"));
            Long biggestRevision = persistenceService.find(query);
            return biggestRevision+1;
        } else
            return 1L;
    }

    Long generateFamilyId(Asset asset) {
        QueryBuilder<Long> query = new QueryBuilder<Long>(ProcedureDefinition.class, securityContext.getTenantSecurityFilter());
        query.addSimpleWhere("asset", asset);
        query.setSelectArgument(new MaxSelect("familyId"));
        Long lastFamilyId = persistenceService.find(query);
        return lastFamilyId == null? 1: lastFamilyId + 1;
    }

    public List<ProcedureDefinition> getAllProcedureDefinitionsForAsset(Asset asset) {
        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("asset", asset);

        return persistenceService.findAll(query);
    }

    public Long getAllProcedureDefinitionsForAssetTypeCount(AssetType assetType) {
        return persistenceService.count(getProcedureDefinitionForAssetTypeQueryBuilder(assetType));
    }

    public List<ProcedureDefinition> getAllProcedureDefinitionsForAssetType(AssetType assetType) {
        return persistenceService.findAll(getProcedureDefinitionForAssetTypeQueryBuilder(assetType));
    }

    private QueryBuilder<ProcedureDefinition> getProcedureDefinitionForAssetTypeQueryBuilder(AssetType assetType) {
        QueryBuilder<ProcedureDefinition> query = new QueryBuilder<ProcedureDefinition>(ProcedureDefinition.class, securityContext.getTenantSecurityFilter());
        query.addSimpleWhere("asset.type.id", assetType.getId());
        return query;
    }

    public void archiveProcedureDefinitionsForAssetType(AssetType assetType) {
        List<ProcedureDefinition> procedureDefinitionList = getAllProcedureDefinitionsForAssetType(assetType);

        for (ProcedureDefinition procedureDefinition : procedureDefinitionList) {
            archiveProcedureDefinition(procedureDefinition);
        }
    }

    private QueryBuilder<ProcedureDefinition> createActiveProcedureDefinitionQuery(Asset asset) {
        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("asset", asset);
        query.addWhere(WhereParameter.Comparator.IN, "publishedState", "publishedState", Arrays.asList(PublishedState.ACTIVE_STATES));
        return query;
    }

    public boolean hasActiveProcedureDefinitions(Asset asset) {
        return persistenceService.exists(createActiveProcedureDefinitionQuery(asset));
    }

    public List<ProcedureDefinition> getActiveProcedureDefinitionsForAsset(Asset asset) {
        return persistenceService.findAll(createActiveProcedureDefinitionQuery(asset));
    }

    public List<ProcedureDefinition> getPreviouslyPublishedProceduresForAsset(Asset asset) {
        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("asset", asset);
        query.addSimpleWhere("publishedState", PublishedState.PREVIOUSLY_PUBLISHED);

        return persistenceService.findAll(query);
    }

    @Transactional(readOnly = true)
    public List<DateChartable> getPublishedProceduresForWidget(Date fromDate, Date toDate, ChartGranularity granularity) {
        // UGGH : hack.   this is a small, focused approach to fixing yet another time zone bug.
        // this should be reverted when a complete, system wide approach to handling time zones is implemented.
        // see WEB-2836
        TimeZone timeZone = getCurrentUser().getTimeZone();

        QueryBuilder<DateChartable> builder = new QueryBuilder<DateChartable>(ProcedureDefinition.class, securityContext.getUserSecurityFilter());

        NewObjectSelect select = new NewObjectSelect(DateChartable.class);
        List<String> args = Lists.newArrayList("COUNT(*)");
        args.addAll(reportServiceHelper.getSelectConstructorArgsForGranularityTimezoneAdjusted("created", granularity, timeZone, fromDate));
        select.setConstructorArgs(args);
        builder.setSelectArgument(select);

        builder.addWhere(whereFromToForCompletedEvents(fromDate, toDate, "created", timeZone));
        builder.addSimpleWhere("publishedState", PublishedState.PUBLISHED);

        Date sampleDate = fromDate;
        builder.addGroupByClauses(reportServiceHelper.getGroupByClausesByGranularity(granularity, "created", timeZone, sampleDate));

        builder.addOrder("created");

        return persistenceService.findAll(builder);
    }

    // XXX : converting to UTC is probably the correct way.  other widgets might want to use this...
    private WhereClause<?> whereFromToForCompletedEvents(Date fromDate, Date toDate, String property, TimeZone timeZone) {
        if (timeZone!=null) {
            fromDate = DateHelper.convertToUTC(fromDate, timeZone);
            toDate = DateHelper.convertToUTC(toDate, timeZone);
        }

        if (fromDate!=null && toDate!=null) {
            WhereParameterGroup filterGroup = new WhereParameterGroup("filtergroup");
            filterGroup.addClause(WhereClauseFactory.create(WhereParameter.Comparator.GE, "fromDate", property, fromDate, null, WhereClause.ChainOp.AND));
            filterGroup.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LT, "toDate", property, toDate, null, WhereClause.ChainOp.AND));
            return filterGroup;
        } else if (fromDate!=null) {
            return new WhereParameter<Date>(WhereParameter.Comparator.GE, property, fromDate);
        } else if (toDate!=null) {
            return new WhereParameter<Date>(WhereParameter.Comparator.LT, property, toDate);
        }
        // CAVEAT : we don't want results to include values with null dates. they are ignored.  (this makes sense for EventSchedules
        //   because null dates are used when representing AdHoc events).
        return new WhereParameter<Date>(WhereParameter.Comparator.NOTNULL, property);
    }


    public List<ProcedureDefinition> getAllPublishedProcedures(String sTerm, String order, boolean ascending, int first, int count) {
        String searchTerm = "";

        if(sTerm != null) {
            searchTerm = sTerm;
        }

        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("publishedState", PublishedState.PUBLISHED);

        if(!searchTerm.trim().equals("")) {
            WhereParameterGroup group = new WhereParameterGroup("procedureSearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "procedureCode", "procedureCode", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "equipmentNumber", "equipmentNumber", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            query.addWhere(group);
        }

        // "performedBy.fullName"...split('.')  a.b  pb.name....order by a, order by a.b
        // HACK : we need to do a *special* order by when chaining attributes together when the parent might be null.
        // so if we order by performedBy.firstName we need to add this NULLS LAST clause otherwise events with null performedBy values
        // will not be returned in the result list.
        // this should be handled more elegantly in the future but i'm fixing at the last second.
        boolean needsSortJoin = false;
        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                if (subOrder.startsWith("developedBy")) {
                    subOrder = subOrder.replaceAll("developedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;
                } else {
                    query.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoin) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "developedBy", "sortJoin", true));
        }

        return persistenceService.findAllPaginated(query,first,count);
    }

    public List<ProcedureDefinition> getSelectedPublishedProcedures(String procedureCode, Asset asset, boolean isAsset, String order, boolean ascending, int first, int count) {

        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("publishedState", PublishedState.PUBLISHED);

        query.addSimpleWhere("asset", asset);

        if(!isAsset) {
            query.addSimpleWhere("procedureCode", procedureCode.trim());
        }

        // "performedBy.fullName"...split('.')  a.b  pb.name....order by a, order by a.b
        // HACK : we need to do a *special* order by when chaining attributes together when the parent might be null.
        // so if we order by performedBy.firstName we need to add this NULLS LAST clause otherwise events with null performedBy values
        // will not be returned in the result list.
        // this should be handled more elegantly in the future but i'm fixing at the last second.
        boolean needsSortJoin = false;
        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                if (subOrder.startsWith("developedBy")) {
                    subOrder = subOrder.replaceAll("developedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;
                } else {
                    query.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoin) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "developedBy", "sortJoin", true));
        }

        return persistenceService.findAllPaginated(query,first,count);
    }

    public List<ProcedureDefinition> getPublishedProceduresWithoutAudits(int pageNumber, int pageSize, BaseOrg owner) {

        QueryBuilder<ProcedureDefinition> query = new QueryBuilder<ProcedureDefinition>(ProcedureDefinition.class, securityContext.getUserSecurityFilter());

        query.addSimpleWhere("publishedState", PublishedState.PUBLISHED);
        List<Long> idList = getProcedureDefinitionIdFromProcedureAuditEvents();
        if(idList.size()>0) {
            query.addWhere(WhereParameter.Comparator.NOTIN, "id", "id", idList);
        }
        query.setOrder("equipmentLocation", true);
        query.addOrder("procedureCode", true);
        query.applyFilter(new OwnerAndDownFilter(owner));
        query.setLimit(25);
        return persistenceService.findAll(query, pageNumber, pageSize);
    }

    public Long countPublishedProceduresWithoutAudits(BaseOrg owner) {

        QueryBuilder<Long> query = new QueryBuilder<Long>(ProcedureDefinition.class, securityContext.getUserSecurityFilter());

        query.addSimpleWhere("publishedState", PublishedState.PUBLISHED);
        List<Long> idList = getProcedureDefinitionIdFromProcedureAuditEvents();
        if(idList.size()>0) {
            query.addWhere(WhereParameter.Comparator.NOTIN, "id", "id", idList);
        }
        query.setOrder("equipmentLocation", true);
        query.addOrder("procedureCode", true);
        query.applyFilter(new OwnerAndDownFilter(owner));
        query.setCountSelect();
        return persistenceService.find(query);
    }

    public List<Long> getProcedureDefinitionIdFromProcedureAuditEvents() {

        QueryBuilder<Long> query = new QueryBuilder<Long>(ProcedureAuditEvent.class, securityContext.getUserSecurityFilter());
        query.setSimpleSelect("procedureDefinition.id");
        List<Long> list = persistenceService.findAll(query);

        return list;
    }


    public List<ProcedureDefinition> getAllPreviouslyPublishedProcedures(String sTerm, String order, boolean ascending, int first, int count) {

        String searchTerm = "";

        if(sTerm != null) {
            searchTerm = sTerm;
        }

        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("publishedState", PublishedState.PREVIOUSLY_PUBLISHED);

        if(!searchTerm.trim().equals("")) {
            WhereParameterGroup group = new WhereParameterGroup("procedureSearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "procedureCode", "procedureCode", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "equipmentNumber", "equipmentNumber", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            query.addWhere(group);
        }

        // "performedBy.fullName"...split('.')  a.b  pb.name....order by a, order by a.b
        // HACK : we need to do a *special* order by when chaining attributes together when the parent might be null.
        // so if we order by performedBy.firstName we need to add this NULLS LAST clause otherwise events with null performedBy values
        // will not be returned in the result list.
        // this should be handled more elegantly in the future but i'm fixing at the last second.
        boolean needsSortJoin = false;
        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                if (subOrder.startsWith("developedBy")) {
                    subOrder = subOrder.replaceAll("developedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;
                } else {
                    query.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoin) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "developedBy", "sortJoin", true));
        }

        return persistenceService.findAllPaginated(query, first, count);
    }

    public List<ProcedureDefinition> getSelectedPreviouslyPublishedProcedures(String procedureCode, Asset asset, boolean isAsset, String order, boolean ascending, int first, int count) {

        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("publishedState", PublishedState.PREVIOUSLY_PUBLISHED);

        query.addSimpleWhere("asset", asset);

        if(!isAsset) {
            query.addSimpleWhere("procedureCode", procedureCode.trim());
        }

        // "performedBy.fullName"...split('.')  a.b  pb.name....order by a, order by a.b
        // HACK : we need to do a *special* order by when chaining attributes together when the parent might be null.
        // so if we order by performedBy.firstName we need to add this NULLS LAST clause otherwise events with null performedBy values
        // will not be returned in the result list.
        // this should be handled more elegantly in the future but i'm fixing at the last second.
        boolean needsSortJoin = false;
        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                if (subOrder.startsWith("developedBy")) {
                    subOrder = subOrder.replaceAll("developedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;
                } else {
                    query.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoin) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "developedBy", "sortJoin", true));
        }

        return persistenceService.findAllPaginated(query, first, count);
    }

    public List<ProcedureDefinition> getAllDraftProcedures(String sTerm, String order, boolean ascending, int first, int count) {

        String searchTerm = "";

        if(sTerm != null) {
            searchTerm = sTerm;
        }

        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("publishedState", PublishedState.DRAFT);

        if(!searchTerm.trim().equals("")) {
            WhereParameterGroup group = new WhereParameterGroup("procedureSearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "procedureCode", "procedureCode", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "equipmentNumber", "equipmentNumber", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            query.addWhere(group);
        }

        // "performedBy.fullName"...split('.')  a.b  pb.name....order by a, order by a.b
        // HACK : we need to do a *special* order by when chaining attributes together when the parent might be null.
        // so if we order by performedBy.firstName we need to add this NULLS LAST clause otherwise events with null performedBy values
        // will not be returned in the result list.
        // this should be handled more elegantly in the future but i'm fixing at the last second.
        boolean needsSortJoin = false;
        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                if (subOrder.startsWith("developedBy")) {
                    subOrder = subOrder.replaceAll("developedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;
                } else {
                    query.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoin) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "developedBy", "sortJoin", true));
        }

        return persistenceService.findAllPaginated(query,first,count);
    }

    public List<ProcedureDefinition> getSelectedDraftProcedures(String procedureCode, Asset asset, boolean isAsset, String order, boolean ascending, int first, int count) {
        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("publishedState", PublishedState.DRAFT);

        query.addSimpleWhere("asset", asset);
        if(!isAsset) {
            query.addSimpleWhere("procedureCode", procedureCode.trim());
        }

        // "performedBy.fullName"...split('.')  a.b  pb.name....order by a, order by a.b
        // HACK : we need to do a *special* order by when chaining attributes together when the parent might be null.
        // so if we order by performedBy.firstName we need to add this NULLS LAST clause otherwise events with null performedBy values
        // will not be returned in the result list.
        // this should be handled more elegantly in the future but i'm fixing at the last second.
        boolean needsSortJoin = false;
        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                if (subOrder.startsWith("developedBy")) {
                    subOrder = subOrder.replaceAll("developedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;
                } else {
                    query.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoin) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "developedBy", "sortJoin", true));
        }

        return persistenceService.findAllPaginated(query, first, count);
    }


    public Long getPublishedCount(String searchTerm) {
        QueryBuilder<ProcedureDefinition> procedureDefinitionCountQuery = createUserSecurityBuilder(ProcedureDefinition.class);
        procedureDefinitionCountQuery.addSimpleWhere("publishedState", PublishedState.PUBLISHED);

        if(!searchTerm.trim().equals("")) {
            WhereParameterGroup group = new WhereParameterGroup("procedureSearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "procedureCode", "procedureCode", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "equipmentNumber", "equipmentNumber", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            procedureDefinitionCountQuery.addWhere(group);
        }

        return persistenceService.count(procedureDefinitionCountQuery);
    }

    public Long getDraftCount(String searchTerm) {
        QueryBuilder<ProcedureDefinition> procedureDefinitionCountQuery = createUserSecurityBuilder(ProcedureDefinition.class);
        procedureDefinitionCountQuery.addSimpleWhere("publishedState", PublishedState.DRAFT);

        if(!searchTerm.trim().equals("")) {
            WhereParameterGroup group = new WhereParameterGroup("procedureSearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "procedureCode", "procedureCode", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "equipmentNumber", "equipmentNumber", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            procedureDefinitionCountQuery.addWhere(group);
        }

        return persistenceService.count(procedureDefinitionCountQuery);
    }

    public Long getPreviouslyPublishedCount(String searchTerm) {
        QueryBuilder<ProcedureDefinition> procedureDefinitionCountQuery = createUserSecurityBuilder(ProcedureDefinition.class);
        procedureDefinitionCountQuery.addSimpleWhere("publishedState", PublishedState.PREVIOUSLY_PUBLISHED);

        if(!searchTerm.trim().equals("")) {
            WhereParameterGroup group = new WhereParameterGroup("procedureSearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "procedureCode", "procedureCode", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "equipmentNumber", "equipmentNumber", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            procedureDefinitionCountQuery.addWhere(group);
        }

        return persistenceService.count(procedureDefinitionCountQuery);
    }

    public Long getSelectedPublishedCount(String procedureCode, Asset asset, boolean isAsset){

        QueryBuilder<ProcedureDefinition> procedureDefinitionCountQuery = createUserSecurityBuilder(ProcedureDefinition.class);
        procedureDefinitionCountQuery.addSimpleWhere("publishedState", PublishedState.PUBLISHED);

        procedureDefinitionCountQuery.addSimpleWhere("asset", asset);

        if(!isAsset) {
            procedureDefinitionCountQuery.addSimpleWhere("procedureCode", procedureCode.trim());
        }

        return persistenceService.count(procedureDefinitionCountQuery);
    }

    public Long getSelectedDraftCount(String procedureCode, Asset asset, boolean isAsset){

        QueryBuilder<ProcedureDefinition> procedureDefinitionCountQuery = createUserSecurityBuilder(ProcedureDefinition.class);
        procedureDefinitionCountQuery.addSimpleWhere("publishedState", PublishedState.DRAFT);

        procedureDefinitionCountQuery.addSimpleWhere("asset", asset);

        if(!isAsset) {
            procedureDefinitionCountQuery.addSimpleWhere("procedureCode", procedureCode.trim());
        }

        return persistenceService.count(procedureDefinitionCountQuery);
    }

    public Long getSelectedPreviouslyPublishedCount(String procedureCode, Asset asset, boolean isAsset){

        QueryBuilder<ProcedureDefinition> procedureDefinitionCountQuery = createUserSecurityBuilder(ProcedureDefinition.class);
        procedureDefinitionCountQuery.addSimpleWhere("publishedState", PublishedState.PREVIOUSLY_PUBLISHED);

        procedureDefinitionCountQuery.addSimpleWhere("asset", asset);

        if(!isAsset) {
            procedureDefinitionCountQuery.addSimpleWhere("procedureCode", procedureCode.trim());
        }
        return persistenceService.count(procedureDefinitionCountQuery);
    }

    public List<String> getPreConfiguredDeviceList(IsolationPointSourceType sourceType) {
        QueryBuilder<String> query = new QueryBuilder(PreconfiguredDevice.class, new TenantOnlySecurityFilter(getCurrentTenant().getId()));
        query.setSimpleSelect("device");

        if(sourceType != null) {
            WhereParameterGroup sourceGroup = new WhereParameterGroup("isolationPointSourceType");
            sourceGroup.addClause(WhereClauseFactory.createIsNull("isolationPointSourceType"));
            sourceGroup.addClause(WhereClauseFactory.create("isolationPointSourceType", sourceType, WhereClause.ChainOp.OR));
            query.addWhere(sourceGroup);
        } else {
            query.addWhere(WhereClauseFactory.createIsNull("isolationPointSourceType"));
        }
        query.addOrder("device");
        return persistenceService.findAll(query);
    }

    public List<PreconfiguredDevice> getAllPreConfiguredDevices() {
        QueryBuilder<PreconfiguredDevice> query = createTenantSecurityBuilder(PreconfiguredDevice.class);
        return persistenceService.findAll(query);
    }

    public List<PreconfiguredDevice> getPreConfiguredDevices(IsolationPointSourceType sourceType) {
        QueryBuilder<PreconfiguredDevice> query = createTenantSecurityBuilder(PreconfiguredDevice.class);
        if(sourceType != null) {
            query.addSimpleWhere("isolationPointSourceType", sourceType);
        } else
            query.addWhere(WhereClauseFactory.createIsNull("isolationPointSourceType"));
        return persistenceService.findAll(query);
    }

    public PreconfiguredDevice getPreConfiguredDevice(String device, IsolationPointSourceType sourceType) {
        QueryBuilder<PreconfiguredDevice> query = createTenantSecurityBuilder(PreconfiguredDevice.class);
        query.addSimpleWhere("device", device);

        WhereParameterGroup sourceGroup = new WhereParameterGroup("isolationPointSourceType");
        sourceGroup.addClause(WhereClauseFactory.createIsNull("isolationPointSourceType"));
        sourceGroup.addClause(WhereClauseFactory.create("isolationPointSourceType", sourceType, WhereClause.ChainOp.OR));
        query.addWhere(sourceGroup);

        return persistenceService.find(query);
    }

    public Boolean isPredefinedDeviceNameExists(String device, IsolationPointSourceType sourceType, Long id) {
        QueryBuilder<PreconfiguredDevice> query = createTenantSecurityBuilder(PreconfiguredDevice.class);
        query.addSimpleWhere("device", device);
        query.addSimpleWhere("isolationPointSourceType", sourceType);

        if(id != null) {
            query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE,  "id", id));
        }
        return persistenceService.exists(query);
    }

    public List<PreconfiguredEnergySource> getAllPreconfiguredEnergySource() {
        QueryBuilder<PreconfiguredEnergySource> query = new QueryBuilder<PreconfiguredEnergySource>(PreconfiguredEnergySource.class);
        query.addOrder("isolationPointSourceType");
        return persistenceService.findAll(query);
    }

    public List<String> getPreConfiguredEnergySource(IsolationPointSourceType sourceType) {
        QueryBuilder<String> query = new QueryBuilder<String>(PreconfiguredEnergySource.class);
        query.setSimpleSelect("source");
        query.addSimpleWhere("isolationPointSourceType", sourceType);
        query.addOrder("source");
        return persistenceService.findAll(query);
    }

    public void publishProcedureDefinition(ProcedureDefinition definition) throws AnnotatedImageGenerationException {
        try {

            if (definition.getAnnotationType().equals(AnnotationType.CALL_OUT_STYLE)) {
                svgGenerationService.generateAndUploadAnnotatedSvgs(definition);
            } else {
                svgGenerationService.generateAndUploadArrowStyleAnnotatedSvgs(definition);
            }

            ProcedureDefinition previousDefinition = getPublishedProcedureDefinition(definition.getAsset(), definition.getFamilyId());
            if (previousDefinition != null) {
                previousDefinition.setPublishedState(PublishedState.PREVIOUSLY_PUBLISHED);
                previousDefinition.setRetireDate(dateService.nowUTC().toDate());
                persistenceService.update(previousDefinition);
                assetService.decreaseProcedureCount(previousDefinition.getAsset());
            }
            definition.setPublishedState(PublishedState.PUBLISHED);
            definition.setOriginDate(dateService.nowUTC().toDate());
            persistenceService.update(definition);
            //increase the count if it's a newly created procedure definition
            if(definition.getID() == null) {
                assetService.increaseProcedureCount(definition.getAsset());
            }
        } catch (Exception e) {
            logger.error("Failed to generate annotated svgs for Procedure Definition:" + definition.getId());
            throw new AnnotatedImageGenerationException(e);
        }
    }

    /**
     * This method unpublishes a <b>ProcedureDefinition</b> and also removes any scheduled Procedures of this type and
     * any Audits of this <b>ProcedureDefinition</b>.
     *
     * @param definition - An initialized <b>ProcedureDefinition</b> object representing the definition you wish to unpublish.
     */
    public void unpublishProcedureDefinition(ProcedureDefinition definition) {
        //First, we need to make sure that we clear up all Procedure Audits...
        QueryBuilder<ProcedureAuditEvent> auditEventQuery = createTenantSecurityBuilder(ProcedureAuditEvent.class);
        auditEventQuery.addSimpleWhere("procedureDefinition", definition);

        List<ProcedureAuditEvent> auditResults = persistenceService.findAll(auditEventQuery);

        for(ProcedureAuditEvent auditEvent : auditResults) {
            logger.debug("Retiring Procedure Audit Event " + auditEvent.getId());

            //Not sure if this ends up doubling up some of our work
            if(auditEvent.getRecurringEvent() != null) {
                logger.debug("Purging Recurring Schedule for Procedure Audit Event " + auditEvent.getId());
                recurringScheduleService.purgeRecurringEvent(auditEvent.getRecurringEvent());
            }
            auditEvent.retireEntity();
            persistenceService.update(auditEvent);
        }


        //Next, we need to do the same with any Procedures.
        QueryBuilder<Procedure> procedureEventQuery = createTenantSecurityBuilder(Procedure.class);

        procedureEventQuery.addSimpleWhere("type", definition);

        List<Procedure> procedureResults = persistenceService.findAll(procedureEventQuery);

        for(Procedure procedureEvent : procedureResults) {
            logger.debug("Archiving Procedure Event " + procedureEvent.getId());
            if(procedureEvent.getRecurringEvent() != null) {
                logger.debug("Purging Recurring Schedule for Procedure " + procedureEvent.getId());
                recurringScheduleService.purgeRecurringEvent(procedureEvent.getRecurringEvent());
            }
            persistenceService.archive(procedureEvent);
        }

        //Now that we're all cleaned up, we can proceed with unpublishing the definition.
        definition.setPublishedState(PublishedState.PREVIOUSLY_PUBLISHED);
        definition.setRetireDate(dateService.nowUTC().toDate());
        definition.setUnpublishedDate(dateService.nowUTC().toDate());
        definition.setUnpublishedBy(getCurrentUser());
        persistenceService.update(definition);
        assetService.decreaseProcedureCount(definition.getAsset());
    }

    public boolean isCurrentUserAuthor(ProcedureDefinition definition) {
        if (definition.getDevelopedBy().equals(getCurrentUser())) {
            return true;
        }
        return false;
    }

    public boolean isProcedureApprovalRequiredForCurrentUser() {
        return isApprovalRequired() && !canCurrentUserApprove();
    }

    public boolean canCurrentUserApprove() {
        return Permissions.hasOneOf(getCurrentUser().getPermissions(), Permissions.CERTIFY_PROCEDURE);
    }

    public boolean isApprovalRequired() {
        return !userService.getCertifierUsers().isEmpty();
    }

    @Transactional(readOnly=true)
    public void deleteProcedureDefinition(ProcedureDefinition procedureDefinition) {
        Preconditions.checkArgument(procedureDefinition.getPublishedState().isPreApproval(), "can't delete a procedure that has been published");
        s3Service.removeProcedureDefinitionImages(procedureDefinition);
        for (IsolationPoint isolationPoint: procedureDefinition.getLockIsolationPoints()) {
            ImageAnnotation imageAnnotation = isolationPoint.getAnnotation();

            if (null != imageAnnotation) {
                //Making sure that the imageAnnotation object is not detached
                if(!persistenceService.contains(imageAnnotation)) {
                    imageAnnotation = (ImageAnnotation) persistenceService.merge(imageAnnotation);
                }
                persistenceService.remove(imageAnnotation);
            }
            isolationPoint.setAnnotation(null);
            persistenceService.update(isolationPoint);
        }
        archiveProcedureDefinition(procedureDefinition);
    }

    public ProcedureDefinition cloneProcedureDefinition(ProcedureDefinition source) {
        Preconditions.checkArgument(source != null, "can't use null procedure definitions when cloning.");
        ProcedureDefinition to = new ProcedureDefinition();
        to.setAsset(source.getAsset());
        to.setTenant(source.getTenant());
        to.setProcedureCode(source.getProcedureCode());
        to.setElectronicIdentifier(source.getElectronicIdentifier());
        to.setWarnings(source.getWarnings());
        to.setDevelopedBy(getCurrentUser());
        to.setEquipmentNumber(source.getEquipmentNumber());
        to.setEquipmentLocation(source.getEquipmentLocation());
        to.setBuilding(source.getBuilding());
        to.setEquipmentDescription(source.getEquipmentDescription());
        to.setPublishedState(PublishedState.DRAFT);
        to.setFamilyId(source.getFamilyId());
        to.setProcedureType(source.getProcedureType());
        to.setApplicationProcess(source.getApplicationProcess());
        to.setRemovalProcess(source.getRemovalProcess());
        to.setTestingAndVerification(source.getTestingAndVerification());

        Map<String, ProcedureDefinitionImage> clonedImages = cloneImages(source,to);
        to.setImages(Lists.newArrayList(clonedImages.values()));

        for(IsolationPoint isolationPoint: source.getLockIsolationPoints()) {
            IsolationPoint copiedIsolationPoint = cloneIsolationPoint(isolationPoint, clonedImages);
            to.addIsolationPoint(copiedIsolationPoint);
        }

        to.setAnnotationType(source.getAnnotationType());

        return to;
    }

    public ProcedureDefinition cloneProcedureDefinitionForCopy(ProcedureDefinition source) {
        return cloneProcedureDefinitionForCopy(source, source.getAsset());
    }

    public ProcedureDefinition cloneProcedureDefinitionForCopy(ProcedureDefinition source, Asset asset) {
        Preconditions.checkArgument(source != null, "can't use null procedure definitions when cloning.");
        ProcedureDefinition to = new ProcedureDefinition();
        to.setAsset(asset);
        to.setTenant(source.getTenant());
        to.setProcedureCode(source.getProcedureCode()+" Copy");
        to.setElectronicIdentifier(source.getElectronicIdentifier());
        to.setWarnings(source.getWarnings());

        to.setApplicationProcess(source.getApplicationProcess());
        to.setRemovalProcess(source.getRemovalProcess());
        to.setTestingAndVerification(source.getTestingAndVerification());

        to.setDevelopedBy(getCurrentUser());
        if(source.getAsset().getId() == asset.getId()) {
            to.setEquipmentNumber(source.getEquipmentNumber());
        } else {
            to.setEquipmentNumber(asset.getIdentifier());
        }
        to.setEquipmentLocation(source.getEquipmentLocation());
        to.setBuilding(source.getBuilding());
        to.setEquipmentDescription(source.getEquipmentDescription());
        to.setPublishedState(PublishedState.DRAFT);
        to.setProcedureType(ProcedureType.SUB);

        to.setFamilyId(generateFamilyId(source.getAsset()));
        to.setRevisionNumber(1L);

        Map<String, ProcedureDefinitionImage> clonedImages = cloneImages(source,to);
        to.setImages(Lists.newArrayList(clonedImages.values()));

        for(IsolationPoint isolationPoint: source.getLockIsolationPoints()) {
            IsolationPoint copiedIsolationPoint = cloneIsolationPoint(isolationPoint, clonedImages);
            to.addIsolationPoint(copiedIsolationPoint);
        }

        to.setAnnotationType(source.getAnnotationType());

        return to;
    }


    private IsolationPoint cloneIsolationPoint(IsolationPoint source, Map<String, ProcedureDefinitionImage> clonedImages) {
        Preconditions.checkArgument(source != null , "can't use null isolation points when cloning.");

        IsolationPoint isolationPoint = new IsolationPoint();
        isolationPoint.setIdentifier(source.getIdentifier());
        isolationPoint.setLocation(source.getLocation());
        isolationPoint.setMethod(source.getMethod());
        isolationPoint.setCheck(source.getCheck());
        isolationPoint.setSourceType(source.getSourceType());
        isolationPoint.setTenant(source.getTenant());
        isolationPoint.setSourceText(source.getSourceText());
        isolationPoint.setElectronicIdentifier(source.getElectronicIdentifier());
        if(source.getDeviceDefinition() != null) {
            isolationPoint.setDeviceDefinition(cloneIsolationDeviceDescription(source.getDeviceDefinition(), new IsolationDeviceDescription()));
        }
        if(source.getLockDefinition() != null) {
            isolationPoint.setLockDefinition(cloneIsolationDeviceDescription(source.getLockDefinition(), new IsolationDeviceDescription()));
        }
        if(source.getAnnotation() != null) {
            isolationPoint.setAnnotation(cloneImageAnnotation(source.getAnnotation(), clonedImages));
        }
        isolationPoint.setFwdIdx(source.getFwdIdx());
        isolationPoint.setRevIdx(source.getRevIdx());
        return isolationPoint;
    }

    private IsolationDeviceDescription cloneIsolationDeviceDescription(IsolationDeviceDescription from, IsolationDeviceDescription to) {
        Preconditions.checkArgument(from != null && to != null, "can't use null isolation deviceDescription when cloning.");
        to.setFreeformDescription(from.getFreeformDescription());
        to.setAssetType(from.getAssetType());
        to.setAttributeValues(new ArrayList<InfoOptionBean>());
        //TODO: Deal with copying the list later...currently not used.
        //to.setAttributeValues(Lists.newArrayList(from.getAttributeValues()));
        return to;
    }

    private ImageAnnotation cloneImageAnnotation(ImageAnnotation from, Map<String, ProcedureDefinitionImage> clonedImages) {
        Preconditions.checkArgument(from != null, "can't use null isolation annotation when cloning.");

        ImageAnnotation to = cloneImageAnnotation(from, clonedImages.get(from.getImage().getFileName()) );
        to.setTempId(atomicLongService.getNext());

        return to;
    }

    public ImageAnnotation cloneImageAnnotation(ImageAnnotation from, EditableImage image) {
        ImageAnnotation to = new ImageAnnotation();
        to.setTenant(from.getTenant());
        to.setType(from.getType());
        to.setText(from.getText());
        to.setX(from.getX());
        to.setY(from.getY());
        to.setX_tail(from.getX_tail());
        to.setY_tail(from.getY_tail());
        to.setImage(image);
        image.addAnnotation(to);
        return to;
    }

    private Map<String,ProcedureDefinitionImage> cloneImages(ProcedureDefinition from, ProcedureDefinition to) {
        Map<String, ProcedureDefinitionImage> result = Maps.newHashMap();
        for (ProcedureDefinitionImage image:from.getImages()) {
            ProcedureDefinitionImage imageCopy = cloneEditableImage(image);
            imageCopy.setProcedureDefinition(to);
            result.put(image.getFileName(), imageCopy);
            s3Service.copyProcedureDefImageToTemp(image, imageCopy);
        }
        return result;
    }

    private ProcedureDefinitionImage cloneEditableImage(ProcedureDefinitionImage from) {
        Preconditions.checkArgument(from != null, "can't use null isolation annotation when cloning.");
        ProcedureDefinitionImage to = new ProcedureDefinitionImage();
        to.setTenant(from.getTenant());
        to.setFileName(from.getFileName());
        return to;
    }

    /**
     * used to copy data backing wicket model. this result is re-attached and peristed.
     * if you want to actually make a copy, use the cloning methods.
     */
    public IsolationPoint copyIsolationPointForEditing(IsolationPoint from, IsolationPoint to) {
        Preconditions.checkArgument(from != null && to != null, "can't use null isolation points when copying.");

        to.setId(from.getId());
        // normalize isolation point id === annotation text.
        if (from.getAnnotation()!=null) {
            from.getAnnotation().setText(from.getIdentifier());
        }
        to.setAnnotation(from.getAnnotation());
        to.setIdentifier(from.getIdentifier());
        to.setLocation(from.getLocation());
        to.setMethod(from.getMethod());
        to.setCheck(from.getCheck());
        to.setSourceType(from.getSourceType());
        to.setTenant(from.getTenant());
        to.setSourceText(from.getSourceText());
        to.setDeviceDefinition(from.getDeviceDefinition());
        to.setLockDefinition(from.getLockDefinition());
        to.setElectronicIdentifier(from.getElectronicIdentifier());
        to.setFwdIdx(from.getFwdIdx());
        to.setRevIdx(from.getRevIdx());

        return to;
    }

    public ImageAnnotation addImageAnnotationToImage(EditableImage image, ImageAnnotation annotation) {
        if(annotation.getImage()!=null && annotation.getImage()!=image && annotation.getImage().hasAnnotation(annotation)) {
            annotation.getImage().removeAnnotation(annotation);
        }
        image.addAnnotation(annotation);
        annotation.setImage(image);
        annotation.setTenant(image.getTenant());
        return annotation;
    }

    @Transactional(readOnly=true)
    public List<ProcedureDefinition> findByPublishedState(PublishedState publishedState) {
        QueryBuilder<ProcedureDefinition> procedureDefinitionQuery = createUserSecurityBuilder(ProcedureDefinition.class);
        procedureDefinitionQuery.addSimpleWhere("publishedState", publishedState);
        return persistenceService.findAll(procedureDefinitionQuery);
    }

    @Transactional(readOnly=true)
    public Long getWaitingApprovalsCount(String searchTerm) {
        QueryBuilder<ProcedureDefinition> procedureDefinitionCountQuery = createUserSecurityBuilder(ProcedureDefinition.class);
        procedureDefinitionCountQuery.addSimpleWhere("publishedState", PublishedState.WAITING_FOR_APPROVAL);

        if(!searchTerm.trim().equals("")) {
            WhereParameterGroup group = new WhereParameterGroup("procedureSearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "procedureCode", "procedureCode", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "equipmentNumber", "equipmentNumber", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            procedureDefinitionCountQuery.addWhere(group);
        }

        return persistenceService.count(procedureDefinitionCountQuery);
    }

    @Transactional(readOnly=true)
    public Long getSelectedWaitingApprovalsCount(String procedureCode, Asset asset, boolean isAsset) {
        QueryBuilder<ProcedureDefinition> procedureDefinitionCountQuery = createUserSecurityBuilder(ProcedureDefinition.class);
        procedureDefinitionCountQuery.addSimpleWhere("publishedState", PublishedState.WAITING_FOR_APPROVAL);

        procedureDefinitionCountQuery.addSimpleWhere("asset", asset);

        if(!isAsset) {
            procedureDefinitionCountQuery.addSimpleWhere("procedureCode", procedureCode.trim());
        }

        return persistenceService.count(procedureDefinitionCountQuery);
    }

    @Transactional(readOnly=true)
    public Long getRejectedApprovalsCount(String searchTerm) {
        QueryBuilder<ProcedureDefinition> procedureDefinitionCountQuery = createUserSecurityBuilder(ProcedureDefinition.class);
        procedureDefinitionCountQuery.addSimpleWhere("publishedState", PublishedState.REJECTED);

        if(!searchTerm.trim().equals("")) {
            WhereParameterGroup group = new WhereParameterGroup("procedureSearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "procedureCode", "procedureCode", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "equipmentNumber", "equipmentNumber", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            procedureDefinitionCountQuery.addWhere(group);
        }

        return persistenceService.count(procedureDefinitionCountQuery);
    }

    @Transactional(readOnly=true)
    public Long getSelectedRejectedApprovalsCount(String procedureCode, Asset asset, boolean isAsset) {
        QueryBuilder<ProcedureDefinition> procedureDefinitionCountQuery = createUserSecurityBuilder(ProcedureDefinition.class);
        procedureDefinitionCountQuery.addSimpleWhere("publishedState", PublishedState.REJECTED);

        procedureDefinitionCountQuery.addSimpleWhere("asset", asset);

        if(!isAsset) {
            procedureDefinitionCountQuery.addSimpleWhere("procedureCode", procedureCode.trim());
        }

        return persistenceService.count(procedureDefinitionCountQuery);
    }

    public List<ProcedureDefinition> getProcedureDefinitionsFor(String searchTerm, PublishedState publishedState, String order, boolean ascending, int first, int count) {
        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("publishedState", publishedState);

        if(!searchTerm.trim().equals("")) {
            WhereParameterGroup group = new WhereParameterGroup("procedureSearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "procedureCode", "procedureCode", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "equipmentNumber", "equipmentNumber", searchTerm.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            query.addWhere(group);
        }
        // "performedBy.fullName"...split('.')  a.b  pb.name....order by a, order by a.b
        // HACK : we need to do a *special* order by when chaining attributes together when the parent might be null.
        // so if we order by performedBy.firstName we need to add this NULLS LAST clause otherwise events with null performedBy values
        // will not be returned in the result list.
        // this should be handled more elegantly in the future but i'm fixing at the last second.
        boolean needsSortJoin = false;
        boolean needsRejectedSortJoin = false;
        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                if (subOrder.startsWith("developedBy")) {
                    subOrder = subOrder.replaceAll("developedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;

                } else if (subOrder.startsWith("rejectedBy")) {
                    subOrder = subOrder.replaceAll("developedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsRejectedSortJoin = true;

                } else {
                    query.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoin) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "developedBy", "sortJoin", true));
        }

        if (needsRejectedSortJoin) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "rejectedBy", "sortJoin", true));
        }

        return persistenceService.findAllPaginated(query,first,count);
    }

    public List<ProcedureDefinition> getSelectedProcedureDefinitionsFor(String procedureCode, Asset asset, boolean isAsset, PublishedState publishedState, String order, boolean ascending, int first, int count) {
        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("publishedState", publishedState);

        query.addSimpleWhere("asset", asset);
        if(!isAsset) {
            query.addSimpleWhere("procedureCode", procedureCode.trim());
        }

        // "performedBy.fullName"...split('.')  a.b  pb.name....order by a, order by a.b
        // HACK : we need to do a *special* order by when chaining attributes together when the parent might be null.
        // so if we order by performedBy.firstName we need to add this NULLS LAST clause otherwise events with null performedBy values
        // will not be returned in the result list.
        // this should be handled more elegantly in the future but i'm fixing at the last second.
        boolean needsSortJoin = false;
        boolean needsRejectedSortJoin = false;
        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                if (subOrder.startsWith("developedBy")) {
                    subOrder = subOrder.replaceAll("developedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;

                } else if (subOrder.startsWith("rejectedBy")) {
                    subOrder = subOrder.replaceAll("developedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsRejectedSortJoin = true;

                } else {
                    query.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoin) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "developedBy", "sortJoin", true));
        }

        if (needsRejectedSortJoin) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "rejectedBy", "sortJoin", true));
        }

        return persistenceService.findAllPaginated(query,first,count);
    }


    public void archiveProcedureDefinition(ProcedureDefinition procedureDefinition) {
        //first remove any recurring schedules for this procedure definition
        for(RecurringLotoEvent event:recurringScheduleService.getRecurringLotoEvents(procedureDefinition.getAsset())) {
            recurringScheduleService.purgeRecurringEvent(event);
        }

        procedureDefinition.archiveEntity();
        persistenceService.update(procedureDefinition);
        assetService.decreaseProcedureCount(procedureDefinition.getAsset());
    }

    public boolean hasMainProcedureType(Asset asset){
        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);

        query.addSimpleWhere("asset", asset);
        query.addSimpleWhere("procedureType", ProcedureType.MAIN);
        query.addSimpleWhere("publishedState", PublishedState.PUBLISHED);

        Long count = persistenceService.count(query);

        if(count == 0){
            return false;
        } else {
            return true;
        }
   }

   public LotoSettings getLotoSettings() {
       return persistenceService.find(createTenantSecurityBuilder(LotoSettings.class));
   }

   public LotoSettings saveOrUpdateLotoSettings(LotoSettings lotoSettings) {
       return persistenceService.saveOrUpdate(lotoSettings);
   }

    public void deleteLotoSettings(LotoSettings lotoSettings) {
        persistenceService.reattach(lotoSettings);
        persistenceService.delete(lotoSettings);
    }
}
