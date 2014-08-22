package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadWriteModelWithOwner;

import java.util.List;

public class ApiProcedureDefinitionV2 extends ApiReadWriteModelWithOwner {

    private List<ApiIsolationPoint> isolationPoints;
    private List<ApiProcedureDefinitionImage> images;
    private String assetId;
    private String procedureCode;
    private String electronicIdentifier;
    private Long revisionNumber;
    private String warnings;
    private boolean completeIsolationPointInOrder;
    private String equipmentNumber;
    private String equipmentDescription;
    private String equipmentLocation;
    private String procedureType;
    private Long developedBy;

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

    public Long getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(Long revisionNumber) {
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

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public List<ApiProcedureDefinitionImage> getImages() {
        return images;
    }

    public void setImages(List<ApiProcedureDefinitionImage> images) {
        this.images = images;
    }

    public String getEquipmentLocation() {
        return equipmentLocation;
    }

    public void setEquipmentLocation(String equipmentLocation) {
        this.equipmentLocation = equipmentLocation;
    }

    public String getProcedureType() {
        return procedureType;
    }

    public void setProcedureType(String procedureType) {
        this.procedureType = procedureType;
    }

    public Long getDevelopedBy() {
        return developedBy;
    }

    public void setDevelopedBy(Long developedBy) {
        this.developedBy = developedBy;
    }
}

