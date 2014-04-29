package com.n4systems.fieldid.service.search;

import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.search.ProcedureCriteria;
import com.n4systems.model.search.ProcedureWorkflowStateCriteria;
import com.n4systems.services.date.DateService;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.search.terms.ProcedurePerformedByTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProcedureSearchService extends SearchService<ProcedureCriteria, Procedure, Procedure> {

    @Autowired
    private DateService dateService;

    public ProcedureSearchService() {
        super(Procedure.class);
    }

    @Override
    protected void addSearchTerms(ProcedureCriteria criteriaModel, List<SearchTermDefiner> searchTerms, boolean includeGps) {
        addSimpleTerm(searchTerms, "asset.type", criteriaModel.getAssetType());
        addSimpleTerm(searchTerms, "asset.type.group", criteriaModel.getAssetTypeGroup());
        addSimpleTerm(searchTerms, "assignee", criteriaModel.getAssignee());
        addSimpleTerm(searchTerms, "assignedGroup", criteriaModel.getAssignedUserGroup());
        addWildcardOrStringTerm(searchTerms, "asset.rfidNumber", criteriaModel.getRfidNumber());
        addWildcardOrStringTerm(searchTerms, "asset.identifier", criteriaModel.getIdentifier());
        addWildcardOrStringTerm(searchTerms, "asset.customerRefNumber", criteriaModel.getReferenceNumber());
        addSimpleTerm(searchTerms, "asset.assetStatus", criteriaModel.getAssetStatus());

        if (criteriaModel.getPerformedBy() != null) {
            searchTerms.add(new ProcedurePerformedByTerm(criteriaModel.getPerformedBy()));
        }

        if (criteriaModel.getWorkflowState() == ProcedureWorkflowStateCriteria.LOCKED) {
            addSimpleTerm(searchTerms, "workflowState", ProcedureWorkflowState.LOCKED);
        } else if (criteriaModel.getWorkflowState() == ProcedureWorkflowStateCriteria.UNLOCKED) {
            addSimpleTerm(searchTerms, "workflowState", ProcedureWorkflowState.UNLOCKED);
        } else if (criteriaModel.getWorkflowState() == ProcedureWorkflowStateCriteria.OPEN) {
            addSimpleTerm(searchTerms, "workflowState", ProcedureWorkflowState.OPEN);
        }

        addDateRangeTerm(searchTerms, "unlockDate", dateService.calculateFromDate(criteriaModel.getUnlockDateRange()), dateService.calculateInclusiveToDate(criteriaModel.getUnlockDateRange()));
        addDateRangeTerm(searchTerms, "lockDate", dateService.calculateFromDate(criteriaModel.getLockDateRange()), dateService.calculateInclusiveToDate(criteriaModel.getLockDateRange()));
        addDateRangeTerm(searchTerms, "dueDate", dateService.calculateFromDate(criteriaModel.getDueDateRange()), dateService.calculateInclusiveToDate(criteriaModel.getDueDateRange()));
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
