package com.n4systems.fieldid.service.search;

import com.n4systems.model.Event;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.services.date.DateService;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProcedureService extends SearchService<EventReportCriteria, Event> {

    @Autowired
    private DateService dateService;

    public ProcedureService() {
        // TODO : change EVENT.class to Procedure.class
        super(Event.class);
    }

    @Override
    protected void addSearchTerms(EventReportCriteria criteriaModel, List<SearchTermDefiner> searchTerms) {
    }

    private void addAssignedUserTerm(Long assignedUserId, EventReportCriteria criteriaModel, List<SearchTermDefiner> searchTerms) {

    }

    private void addAssetStatusTerm(EventReportCriteria criteriaModel, List<SearchTermDefiner> searchTerms) {

    }

    @Override
    protected void addJoinTerms(EventReportCriteria criteriaModel, List<JoinTerm> joinTerms) {
    }

    @Override
    protected void addSortTerms(EventReportCriteria criteriaModel, QueryBuilder<?> searchBuilder, ColumnMappingView sortColumn, SortDirection sortDirection) {
    }

    private void addOrgSort(EventReportCriteria criteriaModel, QueryBuilder<?> searchBuilder, SortDirection sortDirection, String assetOrgAlias, String eventOrgAlias) {
    }

    @Override
    protected <E> QueryBuilder<E> createAppropriateQueryBuilder(EventReportCriteria criteria, Class<E> searchClass) {
        return super.createAppropriateQueryBuilder(criteria, searchClass);
    }

    public boolean hasProcedures() {
        // TODO : implement this.
        return true;
    }
}
