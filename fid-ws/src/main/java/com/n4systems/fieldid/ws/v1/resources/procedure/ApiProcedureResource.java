package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
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

    @Autowired
    private ProcedureService procedureService;

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

        List<IsolationPointResult> convertedResults = convert(apiProcedure.getIsolationPointResults());
        procedure.setLockResults(convertedResults);
        procedure.setLockDate(convertedResults.get(convertedResults.size()-1).getCheckCheckTime());
        procedure.setWorkflowState(ProcedureWorkflowState.LOCKED);
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

        List<IsolationPointResult> convertedResults = convert(apiProcedure.getIsolationPointResults());
        procedure.setUnlockResults(convertedResults);
        procedure.setUnlockDate(convertedResults.get(convertedResults.size()-1).getCheckCheckTime());
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

    private List<IsolationPointResult> convert(List<ApiIsolationPointResult> isolationPointResults) {
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
        assetFinder.addSimpleWhere("mobileGuid", guid);
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
        convertedProcedure.setCompletedDate(procedure.getLockDate());
        convertedProcedure.setDueDate(procedure.getDueDate());
        return convertedProcedure;
    }

}
