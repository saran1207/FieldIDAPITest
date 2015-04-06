package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.Asset;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.IsolationPointResult;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Component
@Path("procedure")
public class ApiProcedureResource extends FieldIdPersistenceService {

    private static final Logger logger = Logger.getLogger(ApiProcedureResource.class);

    @Autowired private ProcedureService procedureService;
    @Autowired private ProcedureDefinitionService procedureDefinitionService;
    @Autowired private TenantSettingsService tenantSettingsService;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("lock")
    public void lock(ApiProcedureResult apiProcedure) {
        if (apiProcedure.getProcedureId() == null) {
            throw new NullPointerException("ApiProcedureResult has null procedureId");
        }

        Procedure procedure = procedureService.findByMobileId(apiProcedure.getProcedureId(), true);

        if (procedure.getWorkflowState() != ProcedureWorkflowState.OPEN && procedure.getWorkflowState() != ProcedureWorkflowState.CLOSED) {
            throw new IllegalStateException("Attempt to lock procedure that is not in OPEN state. Actual state: " + procedure.getWorkflowState());
        }

        List<IsolationPointResult> convertedResults = convertToEntity(apiProcedure.getIsolationPointResults());
        procedure.setLockResults(convertedResults);
        procedure.setLockedBy(getCurrentUser());
        procedure.setLockDate(convertedResults.get(convertedResults.size() - 1).getCheckCheckTime());
        procedure.setWorkflowState(ProcedureWorkflowState.LOCKED);
        procedure.setType(procedureDefinitionService.getPublishedProcedureDefinition(procedure.getAsset(), procedure.getType().getFamilyId()));
        convertGpsLocation(apiProcedure, procedure);

        if(getCurrentUser().isUsageBasedUser()) {
            tenantSettingsService.decrementUsageBasedEventCount();
        }

        persistenceService.update(procedure);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("unlock")
    public void unlock(ApiProcedureResult apiProcedure) {
        if (apiProcedure.getProcedureId() == null) {
            throw new NullPointerException("ApiProcedureResult has null procedureId");
        }

        Procedure procedure = procedureService.findByMobileId(apiProcedure.getProcedureId(), true);

        if (procedure.getWorkflowState() != ProcedureWorkflowState.LOCKED) {
            logger.error("Attempt to unlock procedure that is not in LOCKED state. Actual state: " + procedure.getWorkflowState());
            return;
        }

        List<IsolationPointResult> convertedResults = convertToEntity(apiProcedure.getIsolationPointResults());
        procedure.setUnlockResults(convertedResults);
        procedure.setUnlockedBy(getCurrentUser());
        procedure.setUnlockDate(convertedResults.get(convertedResults.size() - 1).getCheckCheckTime());
        procedure.setWorkflowState(ProcedureWorkflowState.UNLOCKED);

        persistenceService.update(procedure);
    }

    @GET
    @Path("assignedList")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(readOnly = true)
    public ListResponse<ApiProcedure> findAssignedProcedures(
            @QueryParam("startDate") Date startDate,
            @QueryParam("endDate") Date endDate,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("25") @QueryParam("pageSize") int pageSize) {
        QueryBuilder<Procedure> query = createOpenAssignedProcedureBuilder(startDate, endDate);

        List<Procedure> procedures = persistenceService.findAll(query, page, pageSize);
        Long total = persistenceService.count(query);
        List<ApiProcedure> apiSchedules = convertProcedures(procedures);
        ListResponse<ApiProcedure> response = new ListResponse<ApiProcedure>(apiSchedules, page, pageSize, total);

        logger.info("findAssignedProcedures: >= startDate: " + startDate + " < endDate: " + endDate);

        return response;
    }

    public QueryBuilder<Procedure> createOpenAssignedProcedureBuilder(Date startDate, Date endDate) {
        User user = getCurrentUser();
        QueryBuilder<Procedure> query = createUserSecurityBuilder(Procedure.class)
                .addOrder("dueDate")
                .addWhere(WhereParameter.Comparator.IN, "workflowState", "workflowState", Arrays.asList(ProcedureWorkflowState.ACTIVE_STATES));

        if (startDate != null) {
            query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GE, "startDate", "dueDate", startDate));

        }

        if (endDate != null) {
            query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LT, "endDate", "dueDate", endDate));	//excludes end date.
        }

        if (user.getGroups().isEmpty()) {
            query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "assignee.id", user.getId()));
        } else {
            // WE need to do AND ( assignee.id = user.GetId() OR assignedGroup.id = user.getGroup().getId() )
            WhereParameterGroup group = new WhereParameterGroup();
            group.setChainOperator(WhereClause.ChainOp.AND);
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "assignee.id", user.getId(), WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.IN, "assignedGroup", user.getGroups(), WhereClause.ChainOp.OR));
            query.addWhere(group);
        }
        return query;
    }


    private void convertGpsLocation(ApiProcedureResult procedureResult, Procedure procedure) {
        if (procedureResult.getGpsLatitude() != null && procedureResult.getGpsLongitude() != null) {
            GpsLocation gpsLocation = new GpsLocation(procedureResult.getGpsLatitude(), procedureResult.getGpsLongitude());

            if (gpsLocation.isValid()) {
                procedure.setGpsLocation(gpsLocation);
            }
        }
    }

    private List<IsolationPointResult> convertToEntity(List<ApiIsolationPointResult> isolationPointResults) {
        List<IsolationPointResult> convertedResults = new ArrayList<IsolationPointResult>();
        for (ApiIsolationPointResult isolationPointResult : isolationPointResults) {
            IsolationPointResult result = new IsolationPointResult();
            result.setIsolationPoint(persistenceService.findUsingTenantOnlySecurityWithArchived(IsolationPoint.class, isolationPointResult.getIsolationPointId()));

            // TODO: We're not actually doing device/lock picking at this point. Patents?
//            result.setDevice(isolationPointResult.getDeviceAssetId() == null ? null : findAsset(isolationPointResult.getDeviceAssetId()));
//            result.setLock(isolationPointResult.getLockAssetId() == null ? null : findAsset(isolationPointResult.getLockAssetId()));

            // XXX: The patent lawyers won't let us store this stuff!!
//            result.setDeviceScanOrCheckTime(isolationPointResult.getDeviceScanOrCheckTime());
//            result.setLockScanOrCheckTime(isolationPointResult.getLockScanOrCheckTime());
//            result.setMethodCheckTime(isolationPointResult.getMethodCheckTime());

            // The final time check point.
            result.setCheckCheckTime(isolationPointResult.getCheckCheckTime());

            convertedResults.add(result);
        }
        return convertedResults;
    }

    private Asset findAsset(String guid) {
        QueryBuilder<Asset> assetFinder = createTenantSecurityBuilder(Asset.class, true);
        assetFinder.addSimpleWhere("mobileGUID", guid);
        return persistenceService.find(assetFinder);
    }

    public List<ApiProcedure> getOpenAndLockedProcedures(Long assetId) {
        QueryBuilder<Procedure> query = createOpenAssignedProcedureBuilder(null, null);
        query.addSimpleWhere("asset.id", assetId);

        List<Procedure> procedures = persistenceService.findAll(query);
        return convertProcedures(procedures);
    }

    private List<ApiProcedure> convertProcedures(List<Procedure> procedures) {
        List<ApiProcedure> convertedProcedures = new ArrayList<ApiProcedure>();
        for (Procedure procedure : procedures) {
            convertedProcedures.add(convert(procedure));
        }
        return convertedProcedures;
    }

    private ApiProcedure convert(Procedure procedure) {
        ApiProcedure convertedProcedure = new ApiProcedure();
        convertedProcedure.setSid(procedure.getMobileGUID());
        convertedProcedure.setAssetId(procedure.getAsset().getMobileGUID());
        convertedProcedure.setActive(procedure.isActive());
        convertedProcedure.setModified(procedure.getModified());
        convertedProcedure.setAssigneeUserGroupId(procedure.getAssignedGroup() == null ? null : procedure.getAssignedGroup().getId());
        convertedProcedure.setAssigneeUserId(procedure.getAssignee() == null ? null : procedure.getAssignee().getId());
        convertedProcedure.setDueDate(procedure.getDueDate());
        convertedProcedure.setWorkflowState(procedure.getWorkflowState().name());
        convertedProcedure.setLockResults(convert(procedure.getLockResults()));
        convertedProcedure.setAssetIdentifier(procedure.getAsset().getIdentifier());
        convertedProcedure.setProcedureDefinitionId(procedure.getType().getMobileId());
        return convertedProcedure;
    }

    private List<ApiIsolationPointResult> convert(List<IsolationPointResult> lockResults) {
        List<ApiIsolationPointResult> apiResults = new ArrayList<ApiIsolationPointResult>();
        for (IsolationPointResult lockResult : lockResults) {
            ApiIsolationPointResult apiResult = new ApiIsolationPointResult();
            apiResult.setCheckCheckTime(lockResult.getCheckCheckTime());
            apiResult.setDeviceAssetId(lockResult.getDevice() == null ? null : lockResult.getDevice().getMobileGUID());
            apiResult.setLockAssetId(lockResult.getLock() == null ? null : lockResult.getLock().getMobileGUID());
            apiResult.setDeviceScanOrCheckTime(lockResult.getDeviceScanOrCheckTime());
            apiResult.setLockScanOrCheckTime(lockResult.getLockScanOrCheckTime());
            apiResult.setMethodCheckTime(lockResult.getMethodCheckTime());
            apiResult.setIsolationPointId(lockResult.getIsolationPoint().getId());
            apiResult.setLocationCheckTime(lockResult.getLocationCheckTime());
            apiResults.add(apiResult);
        }
        return apiResults;
    }

    @GET
    @Path("assignedListCounts")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(readOnly = true)
    public List<Long> findAssignedActiveProcedureCounts(
            @QueryParam("year") int year,
            @QueryParam("month") int month) {
        List<Long> counts = new ArrayList<Long>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i = 1; i <= days; i++) {
            Date startDate = new DateTime(year, month + 1, i, 0, 0).toDate();
            Date endDate = new DateTime(year, month + 1, i, 0, 0).plusDays(1).toDate();
            User user = getCurrentUser();

            QueryBuilder<Procedure> query = createUserSecurityBuilder(Procedure.class)
                    .addOrder("dueDate")
                    .addWhere(WhereParameter.Comparator.IN, "workflowState", "workflowState", Arrays.asList(ProcedureWorkflowState.ACTIVE_STATES))
                    .addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GE, "startDate", "dueDate", startDate))
                    .addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LT, "endDate", "dueDate", endDate));

            if (user.getGroups().isEmpty()) {
                query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "assignee.id", user.getId()));
            } else {
                // WE need to do AND ( assignee.id = user.GetId() OR assignedGroup.id = user.getGroup().getId() )
                WhereParameterGroup group = new WhereParameterGroup();
                group.setChainOperator(WhereClause.ChainOp.AND);
                group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "assignee.id", user.getId(), WhereClause.ChainOp.OR));
                group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.IN, "assignedGroup", user.getGroups(), WhereClause.ChainOp.OR));
                query.addWhere(group);
            }

            Long count = persistenceService.count(query);
            counts.add(count);
        }
        return counts;
    }

}
