package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues.ApiAttributeValueResource;
import com.n4systems.model.procedure.IsolationDeviceDescription;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("procedureDefinition")
public class ApiProcedureDefinitionResource extends SetupDataResource<ApiProcedureDefinition, ProcedureDefinition> {

    @Autowired private ApiAttributeValueResource attrResource;

    public ApiProcedureDefinitionResource() {
        super(ProcedureDefinition.class, false);
    }

    @Override
    protected ApiProcedureDefinition convertEntityToApiModel(ProcedureDefinition entityModel) {
        ApiProcedureDefinition apiProcedureDef = new ApiProcedureDefinition();
        apiProcedureDef.setSid(entityModel.getId());
        apiProcedureDef.setModified(entityModel.getModified());
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
            apiIsolationPoint.setActive(true);
            apiIsolationPoint.setSid(isolationPoint.getId());
            apiIsolationPoint.setCheck(isolationPoint.getCheck());
            apiIsolationPoint.setDeviceDefinition(convertDefinition(isolationPoint.getDeviceDefinition()));
            apiIsolationPoint.setLockDefinition(convertDefinition(isolationPoint.getLockDefinition()));
            apiIsolationPoint.setSource(isolationPoint.getSourceType().name());
            apiIsolationPoint.setSourceText(isolationPoint.getSourceText());
            apiIsolationPoint.setIdentifier(isolationPoint.getIdentifier());
            apiIsolationPoint.setElectronicIdentifier(isolationPoint.getElectronicIdentifier());
            apiIsolationPoints.add(apiIsolationPoint);
        }
        return apiIsolationPoints;
    }

    private ApiDeviceDescription convertDefinition(IsolationDeviceDescription deviceDefinition) {
        if (deviceDefinition == null) {
            return null;
        }
        ApiDeviceDescription apiDescription = new ApiDeviceDescription();
        apiDescription.setActive(true);
        apiDescription.setAssetTypeSid(deviceDefinition.getAssetType() == null ? null : deviceDefinition.getAssetType().getId());
        apiDescription.setAttributes(attrResource.convertInfoOptions(deviceDefinition.getAttributeValues()));
        apiDescription.setFreeformDescription(deviceDefinition.getFreeformDescription());
        apiDescription.setSid(deviceDefinition.getId());
        return apiDescription;
    }

    @Override
    protected void addTermsToBuilder(QueryBuilder<ProcedureDefinition> builder) {
        builder.addSimpleWhere("publishedState", PublishedState.PUBLISHED);
    }
}
