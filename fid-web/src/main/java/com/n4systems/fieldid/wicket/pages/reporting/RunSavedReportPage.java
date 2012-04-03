package com.n4systems.fieldid.wicket.pages.reporting;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.PageFactory;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.AbstractSearchPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.model.saveditem.SavedReportItem;
import com.n4systems.services.reporting.DashboardReportingService;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

public class RunSavedReportPage extends FieldIDFrontEndPage {

    
    private @SpringBean SavedReportService savedReportService;
    private @SpringBean DashboardReportingService dashboardReportingService;

    public RunSavedReportPage(PageParameters params) {
        StringValue source = params.get(AbstractSearchPage.SOURCE_PARAMETER);
        Preconditions.checkArgument(source != null, "you must specify 'source' in order to create page");
        PageFactory<ReportPage> factory = getPageFactory(source.toString(), params);
        setResponsePage(factory.createPage());
    }

    public PageFactory<ReportPage> getPageFactory(String source, PageParameters params) {
        if (AbstractSearchPage.SAVED_ITEM_SOURCE.equals(source)) {
            return new SavedItemPageFactory(params);
        } else if (AbstractSearchPage.WIDGET_SOURCE.equals(source)) {
            return new WidgetPageFactory(params);
        } else {
            return new DefaultPageFactory(params);
        }        
    }


    // --------------------------------------------------------------------------------------------


    public class SavedItemPageFactory implements PageFactory<ReportPage> {

        private PageParameters params;

        public SavedItemPageFactory(PageParameters params) {
            this.params = params;
        }

        @Override public ReportPage createPage() {
            Long id = params.get("id").toLong();
            SavedReportItem savedReportItem = savedReportService.getConvertedReport(SavedReportItem.class, id);
            savedReportItem.getSearchCriteria().setSavedReportId(id);
            savedReportItem.getSearchCriteria().setSavedReportName(savedReportItem.getName());
            return new ReportPage(savedReportItem);
        }
    }

    public class WidgetPageFactory implements PageFactory<ReportPage> {

        private PageParameters params;

        public WidgetPageFactory(PageParameters params) {
            this.params = params;
        }

        @Override public ReportPage createPage() {
            return null;
        }
    }

    public class DefaultPageFactory implements PageFactory<ReportPage> {

        public DefaultPageFactory(PageParameters params) {

        }

        @Override public ReportPage createPage() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

}
