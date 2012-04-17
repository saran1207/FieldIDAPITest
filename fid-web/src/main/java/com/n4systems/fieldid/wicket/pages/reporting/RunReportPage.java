package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.pages.PageFactory;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchBridgePage;
import com.n4systems.model.saveditem.SavedReportItem;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.services.reporting.DashboardReportingService;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RunReportPage extends SearchBridgePage<ReportPage, EventReportCriteria, SavedReportItem> {

    private @SpringBean SavedReportService savedReportService;
    private @SpringBean DashboardReportingService dashboardReportingService;

    public RunReportPage(PageParameters params) {
        super(params);
    }

    public PageFactory<ReportPage> getPageFactory(String source, PageParameters params) {
        PageFactory<ReportPage> factory = super.getPageFactory(source, params);
        return (factory==null) ? new DefaultSourcePageFactory(params) : factory;

    }

    @Override
    protected SavedReportItem getSavedItem(Long id) {
        return savedReportService.getConvertedReport(SavedReportItem.class, id);
    }

    @Override
    protected ReportPage createPage(EventReportCriteria criteria, SavedReportItem savedItem) {
        return new ReportPage(criteria, savedItem);
    }

    @Override
    protected EventReportCriteria getCriteriaFromWidget(Long widgetDefinitionId, Long x, String y, String series) {
        return dashboardReportingService.convertWidgetDefinitionToReportCriteria(widgetDefinitionId, x, series);
    }


    // --------------------------------------------------------------------------------------------


    public class DefaultSourcePageFactory implements PageFactory<ReportPage> {
        public DefaultSourcePageFactory(PageParameters params) {
        }

        @Override public ReportPage createPage() {
            return new ReportPage(dashboardReportingService.getDefaultReportCriteria());
        }
    }

}
