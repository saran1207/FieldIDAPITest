package com.n4systems.fieldid.wicket.data;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.search.ProcedureService;
import com.n4systems.model.search.ProcedureCriteria;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.views.TableView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ProcedureDataProvider extends FieldIdAPIDataProvider {

    @SpringBean
    private ProcedureService procedureService;
    private ProcedureCriteria searchCriteria;

    public ProcedureDataProvider(ProcedureCriteria searchCriteria) {
        super(searchCriteria, "completedDate", SortDirection.DESC);
        this.searchCriteria = searchCriteria;
    }

    @Override
    protected int getResultCount() {
        return procedureService.countPages(searchCriteria, 1L);
    }

    @Override
    protected PageHolder<TableView> runSearch(int page, int pageSize) {
        return procedureService.performSearch(searchCriteria, createResultTransformer(), page, pageSize);
    }

    @Override
    public List<Long> getIdList() {
        return procedureService.idSearch(searchCriteria);
    }

}
