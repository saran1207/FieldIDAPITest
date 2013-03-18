package com.n4systems.fieldid.service.search;

import com.n4systems.model.Event;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.ProcedureCriteria;
import com.n4systems.services.date.DateService;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.terms.DateRangeTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProcedureService extends SearchService<ProcedureCriteria, Procedure> {

    @Autowired
    private DateService dateService;

    public ProcedureService() {
        // TODO : change EVENT.class to Procedure.class
        super(Procedure.class);
    }

    @Override
    protected void addSearchTerms(ProcedureCriteria criteriaModel, List<SearchTermDefiner> searchTerms) {
        addSimpleTerm(searchTerms, "type.asset.type", criteriaModel.getAssetType());
        addSimpleTerm(searchTerms, "type.asset.type.group", criteriaModel.getAssetTypeGroup());
        addSimpleTerm(searchTerms, "assignee", criteriaModel.getAssignee());
        addSimpleTerm(searchTerms, "performedBy", criteriaModel.getPerformedBy());

        DateRangeTerm completedRangeTerm = new DateRangeTerm("completedDate", dateService.calculateFromDate(criteriaModel.getDateRange()), dateService.calculateToDate(criteriaModel.getDateRange()));
        DateRangeTerm dueRangeTerm = new DateRangeTerm("dueDate", dateService.calculateFromDate(criteriaModel.getDueDateRange()), dateService.calculateToDate(criteriaModel.getDueDateRange()));

        searchTerms.add(completedRangeTerm);
        searchTerms.add(dueRangeTerm);
    }

    private void addAssignedUserTerm(Long assignedUserId, ProcedureCriteria criteriaModel, List<SearchTermDefiner> searchTerms) {
    }

    private void addAssetStatusTerm(ProcedureCriteria criteriaModel, List<SearchTermDefiner> searchTerms) {
    }

    public boolean hasProcedures() {
        QueryBuilder<Procedure> countProcedures = createTenantSecurityBuilder(Procedure.class);
        return persistenceService.count(countProcedures) > 0;
    }

}
