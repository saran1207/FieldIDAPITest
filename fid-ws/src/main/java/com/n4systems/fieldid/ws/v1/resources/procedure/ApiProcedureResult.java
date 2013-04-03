package com.n4systems.fieldid.ws.v1.resources.procedure;

import java.util.Date;
import java.util.List;

public class ApiProcedureResult {

    private Date completedDate;
    private Long procedureId;
    private List<ApiIsolationPointResult> isolationPointResults;

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Long getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Long procedureId) {
        this.procedureId = procedureId;
    }

    public List<ApiIsolationPointResult> getIsolationPointResults() {
        return isolationPointResults;
    }

    public void setIsolationPointResults(List<ApiIsolationPointResult> isolationPointResults) {
        this.isolationPointResults = isolationPointResults;
    }
}
