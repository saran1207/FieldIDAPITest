package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

import java.util.List;

public class ApiProcedureDefinition extends ApiReadonlyModel {

    private List<ApiIsolationPoint> isolationPoints;
    private String procedureCode;
    private String electronicIdentifier;
    private String revisionNumber;
    private String warnings;
    private boolean completeIsolationPointInOrder;
    private String equipmentNumber;
    private String equipmentDescription;

    public List<ApiIsolationPoint> getIsolationPoints() {
        return isolationPoints;
    }

    public void setIsolationPoints(List<ApiIsolationPoint> isolationPoints) {
        this.isolationPoints = isolationPoints;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getElectronicIdentifier() {
        return electronicIdentifier;
    }

    public void setElectronicIdentifier(String electronicIdentifier) {
        this.electronicIdentifier = electronicIdentifier;
    }

    public String getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(String revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public boolean isCompleteIsolationPointInOrder() {
        return completeIsolationPointInOrder;
    }

    public void setCompleteIsolationPointInOrder(boolean completeIsolationPointInOrder) {
        this.completeIsolationPointInOrder = completeIsolationPointInOrder;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
    }

    public String getEquipmentDescription() {
        return equipmentDescription;
    }

    public void setEquipmentDescription(String equipmentDescription) {
        this.equipmentDescription = equipmentDescription;
    }
}
