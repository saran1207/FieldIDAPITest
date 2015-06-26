package com.n4systems.fieldid.api.mobile.resources.procedure;

import com.n4systems.fieldid.api.mobile.resources.model.ApiReadWriteModelWithOwner;

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
    private String equipmentBuilding;
    private String applicationProcess;
    private String removalProcess;
    private String testingAndVerification;
    private String status;
    private String rejectedReason;

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

    public String getEquipmentBuilding() {
        return equipmentBuilding;
    }

    public void setEquipmentBuilding(String equipmentBuilding) {
        this.equipmentBuilding = equipmentBuilding;
    }

    public String getApplicationProcess() {
        return applicationProcess;
    }

    public void setApplicationProcess(String applicationProcess) {
        this.applicationProcess = applicationProcess;
    }

    public String getRemovalProcess() {
        return removalProcess;
    }

    public void setRemovalProcess(String removalProcess) {
        this.removalProcess = removalProcess;
    }

    public String getTestingAndVerification() {
        return testingAndVerification;
    }

    public void setTestingAndVerification(String testingAndVerification) {
        this.testingAndVerification = testingAndVerification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }
}

