package com.n4systems.fieldid.ws.v2.resources.customerdata.procedure;

import com.n4systems.fieldid.service.procedure.LockoutReasonService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.ws.v2.resources.ApiKeyString;
import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;
import com.n4systems.fieldid.ws.v2.resources.ApiResource;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.procedure.*;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Component
@Path("procedure")
public class ApiProcedureResource extends ApiResource<ApiProcedure, Procedure> {

    @Autowired private ProcedureService procedureService;
    @Autowired private ProcedureDefinitionService procedureDefinitionService;
    @Autowired private TenantSettingsService tenantSettingsService;
    @Autowired private LockoutReasonService lockoutReasonService;

	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> query(@QueryParam("id") List<ApiKeyString> procedureIds) {
		if (procedureIds.isEmpty()) return new ArrayList<>();

		QueryBuilder<ApiModelHeader> query = new QueryBuilder<>(Procedure.class, securityContext.getUserSecurityFilter());
		query.setSelectArgument(new NewObjectSelect(ApiModelHeader.class, "mobileGUID", "modified"));
		query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "mobileGUID", unwrapKeys(procedureIds)));
		List<ApiModelHeader> results = persistenceService.findAll(query);
		return results;
	}

	@GET
	@Path("query/assigned")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> findAssignedProcedures(@QueryParam("startDate") Date startDate, @QueryParam("endDate") Date endDate) {
		User user = getCurrentUser();
		QueryBuilder<ApiModelHeader> query = new QueryBuilder<>(Procedure.class, securityContext.getUserSecurityFilter());
		query.setSelectArgument(new NewObjectSelect(ApiModelHeader.class, "mobileGUID", "modified"));
		query.addOrder("dueDate");
		query.addWhere(WhereParameter.Comparator.IN, "workflowState", "workflowState", Arrays.asList(ProcedureWorkflowState.ACTIVE_STATES));

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

		List<ApiModelHeader> results = persistenceService.findAll(query);
		return results;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiProcedure> findAll(@QueryParam("id") List<ApiKeyString> procedureIds) {
		if (procedureIds.isEmpty()) return new ArrayList<>();

		List<ApiProcedure> results = convertAllEntitiesToApiModels(procedureService.findByMobileId(unwrapKeys(procedureIds)));
		return results;
	}

	@GET
	@Path("assignedCounts")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<Long> findAssignedActiveProcedureCounts(
			@QueryParam("year") int year,
			@QueryParam("month") int month) {

		List<Long> counts = new ArrayList<>();
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

    @PUT
	@Path("lock")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void lock(ApiProcedureResult apiProcedure) {
        if (apiProcedure.getProcedureId() == null) {
            throw new BadRequestException("procedureId must not be null");
        }

        Procedure procedure = procedureService.findByMobileId(apiProcedure.getProcedureId(), true);

        //The Mobile app is performing an unscheduled procedure, so we need to create it first.
        if(procedure == null) {
			ProcedureDefinition procedureDefinition = procedureDefinitionService.findProcedureDefinitionByMobileId(apiProcedure.getProcedureDefinitionId());

            procedure = new Procedure();
            procedure.setMobileGUID(apiProcedure.getProcedureId());
            procedure.setType(procedureDefinition);
            procedure.setWorkflowState(ProcedureWorkflowState.OPEN);
            procedure.setAsset(procedureDefinition.getAsset());
            procedure.setTenant(procedureDefinition.getTenant());
            procedure.setState(Archivable.EntityState.ACTIVE);
            procedure.setCreated(new Date());
            procedure.setModified(new Date());
            persistenceService.save(procedure);
        }

        if (procedure.getWorkflowState() != ProcedureWorkflowState.OPEN && procedure.getWorkflowState() != ProcedureWorkflowState.CLOSED) {
            throw new BadRequestException("Attempt to lock procedure that is not in OPEN state. Actual state: " + procedure.getWorkflowState());
        }

        List<IsolationPointResult> convertedResults = convertToEntity(apiProcedure.getSortedIsolationPointResults());
        procedure.setLockResults(convertedResults);
        procedure.setLockedBy(getCurrentUser());
        procedure.setLockDate(convertedResults.get(convertedResults.size() - 1).getCheckCheckTime());
        procedure.setWorkflowState(ProcedureWorkflowState.LOCKED);
        procedure.setType(procedureDefinitionService.getPublishedProcedureDefinition(procedure.getAsset(), procedure.getType().getFamilyId()));
        convertGpsLocation(apiProcedure, procedure);

        if(getCurrentUser().isUsageBasedUser()) {
            tenantSettingsService.decrementUsageBasedEventCount();
        }

        List<LockoutReason> lockoutReasons = lockoutReasonService.getActiveLockoutReasons();
        for(LockoutReason reason:lockoutReasons){
            if(reason.getName().equals(apiProcedure.getLockoutReason()))
            {
                procedure.setLockoutReason(reason);
                break;
            }
        }
        persistenceService.update(procedure);
    }

    @PUT
	@Path("unlock")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void unlock(ApiProcedureResult apiProcedure) {
		if (apiProcedure.getProcedureId() == null) {
			throw new BadRequestException("procedureId must not be null");
		}

        Procedure procedure = procedureService.findByMobileId(apiProcedure.getProcedureId(), true);

        if (procedure.getWorkflowState() != ProcedureWorkflowState.LOCKED) {
			throw new BadRequestException("Attempt to unlock procedure that is not in LOCKED state. Actual state: " + procedure.getWorkflowState());
        }

        List<IsolationPointResult> convertedResults = convertToEntity(apiProcedure.getSortedIsolationPointResults());
        procedure.setUnlockResults(convertedResults);
        procedure.setUnlockedBy(getCurrentUser());
        procedure.setUnlockDate(convertedResults.get(convertedResults.size() - 1).getCheckCheckTime());
        procedure.setWorkflowState(ProcedureWorkflowState.UNLOCKED);

        persistenceService.update(procedure);
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
        List<IsolationPointResult> convertedResults = new ArrayList<>();
        for (ApiIsolationPointResult isolationPointResult : isolationPointResults) {
            IsolationPointResult result = new IsolationPointResult();
            result.setIsolationPoint(persistenceService.findUsingTenantOnlySecurityWithArchived(IsolationPoint.class, isolationPointResult.getIsolationPointId()));
            result.setCheckCheckTime(isolationPointResult.getCheckCheckTime());
            convertedResults.add(result);
        }
        return convertedResults;
    }

	@Override
    protected ApiProcedure convertEntityToApiModel(Procedure procedure) {
        ApiProcedure convertedProcedure = new ApiProcedure();
        convertedProcedure.setSid(procedure.getMobileGUID());
        convertedProcedure.setAssetId(procedure.getAsset().getMobileGUID());
        convertedProcedure.setActive(procedure.isActive());
        convertedProcedure.setModified(procedure.getModified());
        convertedProcedure.setAssigneeUserGroupId(procedure.getAssignedGroup() == null ? null : procedure.getAssignedGroup().getId());
        convertedProcedure.setAssigneeUserId(procedure.getAssignee() == null ? null : procedure.getAssignee().getId());
        convertedProcedure.setDueDate(procedure.getDueDate());
        convertedProcedure.setWorkflowState(procedure.getWorkflowState().name());
        convertedProcedure.setLockResults(convertAllEntitiesToApiModels(procedure.getLockResults(), this::convertPointResult));
        convertedProcedure.setAssetIdentifier(procedure.getAsset().getIdentifier());
        convertedProcedure.setProcedureDefinitionId(procedure.getType().getMobileId());
        return convertedProcedure;
    }

	private ApiIsolationPointResult convertPointResult(IsolationPointResult lockResult) {
		ApiIsolationPointResult apiResult = new ApiIsolationPointResult();
		apiResult.setCheckCheckTime(lockResult.getCheckCheckTime());
		apiResult.setDeviceAssetId(lockResult.getDevice() == null ? null : lockResult.getDevice().getMobileGUID());
		apiResult.setLockAssetId(lockResult.getLock() == null ? null : lockResult.getLock().getMobileGUID());
		apiResult.setDeviceScanOrCheckTime(lockResult.getDeviceScanOrCheckTime());
		apiResult.setLockScanOrCheckTime(lockResult.getLockScanOrCheckTime());
		apiResult.setMethodCheckTime(lockResult.getMethodCheckTime());
		apiResult.setIsolationPointId(lockResult.getIsolationPoint().getId());
		apiResult.setLocationCheckTime(lockResult.getLocationCheckTime());
		return apiResult;
	}
}
