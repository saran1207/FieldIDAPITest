package com.n4systems.fieldid.service.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;

import java.util.Arrays;
import java.util.List;

public class ProcedureService extends FieldIdPersistenceService {

    public boolean hasActiveProcedure(Asset asset) {
        QueryBuilder<Procedure> query = createTenantSecurityBuilder(Procedure.class);
        query.addSimpleWhere("asset", asset);

        WhereParameterGroup workflowStates = new WhereParameterGroup("workflowStates");
        List<ProcedureWorkflowState> invalidStates = Arrays.asList(ProcedureWorkflowState.OPEN, ProcedureWorkflowState.LOCKED, ProcedureWorkflowState.UNLOCKED);
        query.addWhere(WhereParameter.Comparator.IN, "workflowState", "workflowState", invalidStates);
        return persistenceService.exists(query);
    }

    public Long createSchedule(Procedure openProcedure) {
        return persistenceService.save(openProcedure);
    }

}
