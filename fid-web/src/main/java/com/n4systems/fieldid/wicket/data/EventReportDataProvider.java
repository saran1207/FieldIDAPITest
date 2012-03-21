package com.n4systems.fieldid.wicket.data;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.search.ReportService;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.views.TableView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class EventReportDataProvider extends FieldIdAPIDataProvider {

    @SpringBean
    private ReportService reportService;
    private EventReportCriteria reportCriteria;

    public EventReportDataProvider(EventReportCriteria reportCriteria) {
        super(reportCriteria, "date", SortDirection.DESC);
        this.reportCriteria = reportCriteria;
    }

    @Override
    protected int getResultCount() {
        return reportService.countPages(reportCriteria, 1L);
    }

    @Override
    protected PageHolder<TableView> runSearch(int page, int pageSize) {
        return reportService.performSearch(reportCriteria, createResultTransformer(), page, pageSize);
    }

    @Override
    public List<Long> getIdList() {
        return reportService.idSearch(reportCriteria);
    }

}
