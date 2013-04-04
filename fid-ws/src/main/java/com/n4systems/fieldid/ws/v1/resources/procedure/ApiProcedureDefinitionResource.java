package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.procedure.IsolationDeviceDescription;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("procedureDefinition")
public class ApiProcedureDefinitionResource extends SetupDataResource<ApiProcedureDefinition, ProcedureDefinition> {

    public ApiProcedureDefinitionResource() {
        super(ProcedureDefinition.class, false);
    }

    @Override
    protected ApiProcedureDefinition convertEntityToApiModel(ProcedureDefinition entityModel) {
        ApiProcedureDefinition apiProcedureDef = new ApiProcedureDefinition();
        apiProcedureDef.setSid(entityModel.getId());
        apiProcedureDef.setAssetId(entityModel.getAsset().getMobileGUID());
        apiProcedureDef.setCompleteIsolationPointInOrder(entityModel.isCompleteIsolationPointInOrder());
        apiProcedureDef.setElectronicIdentifier(entityModel.getElectronicIdentifier());
        apiProcedureDef.setEquipmentDescription(entityModel.getEquipmentDescription());
        apiProcedureDef.setIsolationPoints(convertIsolationPoints(entityModel.getIsolationPoints()));
        apiProcedureDef.setEquipmentNumber(entityModel.getEquipmentNumber());
        apiProcedureDef.setRevisionNumber(entityModel.getRevisionNumber());
        apiProcedureDef.setProcedureCode(entityModel.getProcedureCode());
        apiProcedureDef.setWarnings(entityModel.getWarnings());
        apiProcedureDef.setActive(entityModel.isActive());
        return apiProcedureDef;
    }

    private List<ApiIsolationPoint> convertIsolationPoints(List<IsolationPoint> isolationPoints) {
        List<ApiIsolationPoint> apiIsolationPoints = new ArrayList<ApiIsolationPoint>();
        for (IsolationPoint isolationPoint : isolationPoints) {
            ApiIsolationPoint apiIsolationPoint = new ApiIsolationPoint();
            apiIsolationPoint.setSid(isolationPoint.getId());
            apiIsolationPoint.setCheck(isolationPoint.getCheck());
            apiIsolationPoint.setDeviceDefinition(convertDefinition(isolationPoint.getDeviceDefinition()));
            apiIsolationPoint.setActive(true);
            apiIsolationPoints.add(apiIsolationPoint);
        }
        return apiIsolationPoints;
    }

    private ApiDeviceDescription convertDefinition(IsolationDeviceDescription deviceDefinition) {
        if (deviceDefinition == null) {
            return null;
        }
        ApiDeviceDescription apiDescription = new ApiDeviceDescription();
        apiDescription.setAssetTypeSid(deviceDefinition.getAssetType() == null ? null : deviceDefinition.getAssetType().getId());
        // TODO: Attribute criteria
        return apiDescription;
    }
}
