package com.n4systems.fieldid.service.search;

import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.search.ProcedureCriteria;
import com.n4systems.model.search.WorkflowState;
import com.n4systems.services.date.DateService;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.search.terms.DateRangeTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProcedureService extends SearchService<ProcedureCriteria, Procedure> {

    @Autowired
    private DateService dateService;

    public ProcedureService() {
        super(Procedure.class);
    }

    @Override
    protected void addSearchTerms(ProcedureCriteria criteriaModel, List<SearchTermDefiner> searchTerms) {
        addSimpleTerm(searchTerms, "type.asset.type", criteriaModel.getAssetType());
        addSimpleTerm(searchTerms, "type.asset.type.group", criteriaModel.getAssetTypeGroup());
        addSimpleTerm(searchTerms, "assignee", criteriaModel.getAssignee());
        addSimpleTerm(searchTerms, "assignedGroup", criteriaModel.getAssignedUserGroup());
        addSimpleTerm(searchTerms, "performedBy", criteriaModel.getPerformedBy());

        if (criteriaModel.getWorkflowState() == WorkflowState.COMPLETE) {
            addSimpleTerm(searchTerms, "workflowState", com.n4systems.model.WorkflowState.COMPLETED);
        } else if (criteriaModel.getWorkflowState() == WorkflowState.OPEN) {
            addSimpleTerm(searchTerms, "workflowState", com.n4systems.model.WorkflowState.OPEN);
        } else if (criteriaModel.getWorkflowState() == WorkflowState.CLOSED) {
            addSimpleTerm(searchTerms, "workflowState", com.n4systems.model.WorkflowState.CLOSED);
        }


        DateRangeTerm completedRangeTerm = new DateRangeTerm("completedDate", dateService.calculateFromDate(criteriaModel.getDateRange()), dateService.calculateToDate(criteriaModel.getDateRange()));
        DateRangeTerm dueRangeTerm = new DateRangeTerm("dueDate", dateService.calculateFromDate(criteriaModel.getDueDateRange()), dateService.calculateToDate(criteriaModel.getDueDateRange()));

        searchTerms.add(completedRangeTerm);
        searchTerms.add(dueRangeTerm);
    }

    public boolean hasProcedures() {
        QueryBuilder<Procedure> countProcedures = createTenantSecurityBuilder(Procedure.class);
        return persistenceService.count(countProcedures) > 0;
    }

    public void saveProcedureDefinition(ProcedureDefinition procedureDefinition) {
        persistenceService.save(procedureDefinition);
    }


    public ProcedureDefinition resetProcedureDefinition(ProcedureDefinition procedureDefinition) {
        return persistenceService.reattach(procedureDefinition,true);
    }
}
