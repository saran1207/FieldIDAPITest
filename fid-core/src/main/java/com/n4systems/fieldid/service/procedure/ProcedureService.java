package com.n4systems.fieldid.service.procedure;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

import java.util.Arrays;
import java.util.List;

public class ProcedureService extends FieldIdPersistenceService {

    public boolean hasActiveProcedure(Asset asset) {
        QueryBuilder<Procedure> query = createTenantSecurityBuilder(Procedure.class);
        query.addSimpleWhere("asset", asset);

        query.addWhere(WhereParameter.Comparator.IN, "workflowState", "workflowState", Arrays.asList(ProcedureWorkflowState.ACTIVE_STATES));
        return persistenceService.exists(query);
    }

    public Long createSchedule(Procedure openProcedure) {
        return persistenceService.save(openProcedure);
    }

    public Procedure updateSchedule(Procedure procedure) {
        return persistenceService.update(procedure);
    }

    public void deleteSchedule(Procedure procedure) {
        Preconditions.checkArgument(procedure.getWorkflowState().equals(ProcedureWorkflowState.OPEN));
        persistenceService.delete(procedure);
    }

    public Procedure findByMobileId(String mobileId, boolean withArchived) {
        QueryBuilder<Procedure> builder = createUserSecurityBuilder(Procedure.class, withArchived);
        builder.addWhere(WhereClauseFactory.create("mobileGUID", mobileId));
        return persistenceService.find(builder);
    }

    public Procedure getOpenProcedure(Asset asset) {
        QueryBuilder<Procedure> query = createTenantSecurityBuilder(Procedure.class);
        query.addSimpleWhere("asset", asset);
        query.addWhere(WhereParameter.Comparator.IN, "workflowState", "workflowState", Arrays.asList(ProcedureWorkflowState.ACTIVE_STATES));
        query.setLimit(1);
        return persistenceService.find(query);
    }

    public List<Procedure> getAllProcedures(Asset asset) {
        QueryBuilder<Procedure> query = createTenantSecurityBuilder(Procedure.class);
        query.addSimpleWhere("asset", asset);
        return persistenceService.findAll(query);
    }
}
