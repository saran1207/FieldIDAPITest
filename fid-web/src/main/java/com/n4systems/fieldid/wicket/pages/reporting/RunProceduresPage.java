package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.service.search.SavedProcedureSearchService;
import com.n4systems.fieldid.wicket.pages.assetsearch.ProcedureSearchPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.SearchBridgePage;
import com.n4systems.model.saveditem.SavedProcedureItem;
import com.n4systems.model.search.ProcedureCriteria;
import com.n4systems.services.reporting.DashboardReportingService;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RunProceduresPage extends SearchBridgePage<ProcedureSearchPage, ProcedureCriteria, SavedProcedureItem> {

    @SpringBean
    private SavedProcedureSearchService savedProcedureSearchService;
    @SpringBean
    private DashboardReportingService dashboardReportingService;

    public RunProceduresPage(PageParameters params) {
        super(params);
    }

    @Override
    protected SavedProcedureItem getSavedItem(Long id) {
        return savedProcedureSearchService.getConvertedReport(SavedProcedureItem.class, id);
    }

    @Override
    protected ProcedureSearchPage createPage(ProcedureCriteria criteria, SavedProcedureItem savedItem) {
        return new ProcedureSearchPage(criteria, savedItem);
    }

    @Override
    protected ProcedureCriteria getCriteriaFromWidget(Long widgetDefinitionId, Long x, String y, String series) {
        return (ProcedureCriteria) dashboardReportingService.convertWidgetDefinitionToReportCriteria(widgetDefinitionId, x, y, series);
    }
}
