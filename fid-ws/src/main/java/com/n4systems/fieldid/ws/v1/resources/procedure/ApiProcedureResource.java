package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.model.Asset;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.IsolationPointResult;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("procedure")
public class ApiProcedureResource extends FieldIdPersistenceService {

    @Autowired private ProcedureService procedureService;
    @Autowired private ProcedureDefinitionService procedureDefinitionService;

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
        procedure.setType(procedureDefinitionService.getPublishedProcedureDefinition(procedure.getAsset()));
        convertGpsLocation(apiProcedure, procedure);

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
            throw new IllegalStateException("Attempt to unlock procedure that is not in LOCKED state. Actual state: " + procedure.getWorkflowState());
        }

        List<IsolationPointResult> convertedResults = convertToEntity(apiProcedure.getIsolationPointResults());
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
        List<IsolationPointResult> convertedResults = new ArrayList<IsolationPointResult>();
        for (ApiIsolationPointResult isolationPointResult : isolationPointResults) {
            IsolationPointResult result = new IsolationPointResult();
            result.setIsolationPoint(persistenceService.findUsingTenantOnlySecurityWithArchived(IsolationPoint.class, isolationPointResult.getIsolationPointId()));

            result.setCheckCheckTime(isolationPointResult.getCheckCheckTime());
            result.setDevice(isolationPointResult.getDeviceAssetId() == null ? null : findAsset(isolationPointResult.getDeviceAssetId()));
            result.setLock(isolationPointResult.getLockAssetId() == null ? null : findAsset(isolationPointResult.getLockAssetId()));
            result.setDeviceScanOrCheckTime(isolationPointResult.getDeviceScanOrCheckTime());
            result.setLockScanOrCheckTime(isolationPointResult.getLockScanOrCheckTime());
            result.setMethodCheckTime(isolationPointResult.getMethodCheckTime());
            convertedResults.add(result);
        }
        return convertedResults;
    }

    private Asset findAsset(String guid) {
        QueryBuilder<Asset> assetFinder = createTenantSecurityBuilder(Asset.class, true);
        assetFinder.addSimpleWhere("mobileGUID", guid);
        return persistenceService.find(assetFinder);
    }

    public List<ApiProcedure> getProcedures(Long assetId) {
        QueryBuilder<Procedure> query = createUserSecurityBuilder(Procedure.class);
        query.addSimpleWhere("asset.id", assetId);
        List<Procedure> procedures = persistenceService.findAll(query);
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

}
