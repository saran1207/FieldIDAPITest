package com.n4systems.fieldid.service.search;

import com.google.common.base.Preconditions;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.search.ProcedureCriteria;
import com.n4systems.model.search.ProcedureWorkflowStateCriteria;
import com.n4systems.services.date.DateService;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.search.terms.DateRangeTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProcedureSearchService extends SearchService<ProcedureCriteria, Procedure> {

    @Autowired
    private DateService dateService;

    public ProcedureSearchService() {
        super(Procedure.class);
    }

    @Override
    protected void addSearchTerms(ProcedureCriteria criteriaModel, List<SearchTermDefiner> searchTerms) {
        addSimpleTerm(searchTerms, "type.asset.type", criteriaModel.getAssetType());
        addSimpleTerm(searchTerms, "type.asset.type.group", criteriaModel.getAssetTypeGroup());
        addSimpleTerm(searchTerms, "assignee", criteriaModel.getAssignee());
        addSimpleTerm(searchTerms, "assignedGroup", criteriaModel.getAssignedUserGroup());
        addSimpleTerm(searchTerms, "performedBy", criteriaModel.getPerformedBy());

        if (criteriaModel.getWorkflowState() == ProcedureWorkflowStateCriteria.LOCKED) {
            addSimpleTerm(searchTerms, "workflowState", ProcedureWorkflowState.LOCKED);
        } else if (criteriaModel.getWorkflowState() == ProcedureWorkflowStateCriteria.UNLOCKED) {
            addSimpleTerm(searchTerms, "workflowState", ProcedureWorkflowState.UNLOCKED);
        } else if (criteriaModel.getWorkflowState() == ProcedureWorkflowStateCriteria.OPEN) {
            addSimpleTerm(searchTerms, "workflowState", ProcedureWorkflowState.OPEN);
        }

        DateRangeTerm lockRangeTerm = new DateRangeTerm("unlockDate", dateService.calculateFromDate(criteriaModel.getUnlockDateRange()), dateService.calculateToDate(criteriaModel.getUnlockDateRange()));
        DateRangeTerm unlockRangeTerm = new DateRangeTerm("lockDate", dateService.calculateFromDate(criteriaModel.getLockDateRange()), dateService.calculateToDate(criteriaModel.getLockDateRange()));
        DateRangeTerm dueRangeTerm = new DateRangeTerm("dueDate", dateService.calculateFromDate(criteriaModel.getDueDateRange()), dateService.calculateToDate(criteriaModel.getDueDateRange()));

        searchTerms.add(lockRangeTerm);
        searchTerms.add(unlockRangeTerm);
        searchTerms.add(dueRangeTerm);
    }

    public boolean hasProcedures() {
        QueryBuilder<Procedure> countProcedures = createTenantSecurityBuilder(Procedure.class);
        return persistenceService.count(countProcedures) > 0;
    }

    public void save(ProcedureDefinition procedureDefinition) {
        persistenceService.update(procedureDefinition);
    }

    public ProcedureDefinition reset(ProcedureDefinition procedureDefinition) {
        if (procedureDefinition!=null && !procedureDefinition.isNew()) {
            return persistenceService.reattach(procedureDefinition,true);
        }
        return procedureDefinition;
    }


}
