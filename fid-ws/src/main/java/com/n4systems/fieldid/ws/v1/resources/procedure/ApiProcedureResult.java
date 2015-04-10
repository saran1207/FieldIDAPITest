package com.n4systems.fieldid.ws.v1.resources.procedure;

import java.math.BigDecimal;
import java.util.List;

public class ApiProcedureResult {

    private String procedureId;
    private String procedureDefinitionId;

    private List<ApiIsolationPointResult> isolationPointResults;
    private BigDecimal gpsLatitude;
    private BigDecimal gpsLongitude;
    private String lockoutReason;

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public String getProcedureDefinitionId() {
        return procedureDefinitionId;
    }

    public void setProcedureDefinitionId(String procedureDefinitionId) {
        this.procedureDefinitionId = procedureDefinitionId;
    }

    public List<ApiIsolationPointResult> getIsolationPointResults() {
        return isolationPointResults;
    }

    public void setIsolationPointResults(List<ApiIsolationPointResult> isolationPointResults) {
        this.isolationPointResults = isolationPointResults;
    }

    public BigDecimal getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(BigDecimal gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public BigDecimal getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(BigDecimal gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    public String getLockoutReason() {
        return lockoutReason;
    }

    public void setLockoutReason(String lockoutReason) {
        this.lockoutReason = lockoutReason;
    }
}
