package com.n4systems.fieldid.service.search;

import com.google.common.base.Preconditions;
import com.n4systems.model.Asset;
import com.n4systems.model.procedure.IsolationPoint;
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

    public void save(ProcedureDefinition procedureDefinition) {
        persistenceService.save(procedureDefinition);
    }


    public ProcedureDefinition reset(ProcedureDefinition procedureDefinition) {
        return persistenceService.reattach(procedureDefinition,true);
    }

    public IsolationPoint copyIsolationPoint(IsolationPoint from, IsolationPoint to) {
        Preconditions.checkArgument(from!=null && to!=null, "can't use null isolation points when copying.");

        to.setId(from.getId());
        to.setIdentifier(from.getIdentifier());
        to.setLocation(from.getLocation());
        to.setMethod(from.getMethod());
        to.setCheck(from.getCheck());
        to.setSource(from.getSource());
        to.setDeviceDefinition(from.getDeviceDefinition());
        to.setLockDefinition(from.getLockDefinition());

        return to;
    }

    public List<ProcedureDefinition> getProceduresForAsset(Asset asset) {
        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("asset", asset);
        return persistenceService.findAll(query);
    }
}
