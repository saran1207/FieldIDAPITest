package com.n4systems.fieldid.wicket.data;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.search.ProcedureSearchService;
import com.n4systems.model.search.ProcedureCriteria;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.views.TableView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ProcedureDataProvider extends FieldIdAPIDataProvider {

    @SpringBean
    private ProcedureSearchService procedureSearchService;
    private ProcedureCriteria searchCriteria;

    public ProcedureDataProvider(ProcedureCriteria searchCriteria) {
        super(searchCriteria, "completedDate", SortDirection.DESC);
        this.searchCriteria = searchCriteria;
    }

    @Override
    protected int getResultCount() {
        return procedureSearchService.countPages(searchCriteria, 1L);
    }

    @Override
    protected PageHolder<TableView> runSearch(int page, int pageSize) {
        return procedureSearchService.performSearch(searchCriteria, createResultTransformer(), page, pageSize);
    }

    @Override
    public List<Long> getIdList() {
        return procedureSearchService.idSearch(searchCriteria);
    }

}
