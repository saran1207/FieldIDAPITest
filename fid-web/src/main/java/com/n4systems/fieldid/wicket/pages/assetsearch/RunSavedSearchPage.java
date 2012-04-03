package com.n4systems.fieldid.wicket.pages.assetsearch;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.PageFactory;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.AbstractSearchPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchPage;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.services.reporting.DashboardReportingService;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

public class RunSavedSearchPage extends FieldIDFrontEndPage {

    private @SpringBean SavedAssetSearchService savedSearchService;
    private @SpringBean DashboardReportingService dashboardReportingService;


    public RunSavedSearchPage(PageParameters params) {
        StringValue source = params.get(AbstractSearchPage.SOURCE_PARAMETER);
        Preconditions.checkArgument(source != null, "you must specify " + AbstractSearchPage.SOURCE_PARAMETER + " in order to create page");
        PageFactory<SearchPage> factory = getPageFactory(source.toString(), params);
        setResponsePage(factory.createPage());
    }

    public PageFactory<SearchPage> getPageFactory(String source, PageParameters params) {
        if (AbstractSearchPage.SAVED_ITEM_SOURCE.equals(source)) {
            return new SavedItemSourcePageFactory(params);
        } else if (AbstractSearchPage.WIDGET_SOURCE.equals(source)) {
            return new WidgetSourcePageFactory(params);
        } else {
            return new DefaultSourcePageFactory(params);
        }
    }


    // --------------------------------------------------------------------------------------------


    public class SavedItemSourcePageFactory implements PageFactory<SearchPage> {

        private PageParameters params;

        public SavedItemSourcePageFactory(PageParameters params) {
            this.params = params;
        }

        @Override public SearchPage createPage() {
            Long id = params.get("id").toLong();
            SavedSearchItem savedSearchItem = savedSearchService.getConvertedReport(SavedSearchItem.class, id);
            savedSearchItem.getSearchCriteria().setSavedReportId(id);
            savedSearchItem.getSearchCriteria().setSavedReportName(savedSearchItem.getName());
            return new SearchPage(savedSearchItem);
        }
    }

    public class WidgetSourcePageFactory implements PageFactory<SearchPage> {

        private PageParameters params;

        public WidgetSourcePageFactory(PageParameters params) {
            this.params = params;
        }

        @Override public SearchPage createPage() {
            Long widgetDefinitionId = params.get(AbstractSearchPage.WIDGET_DEFINITION_PARAMETER).toLong();
            Long x = params.get(AbstractSearchPage.X_PARAMETER).toLong();
            String series = params.get(AbstractSearchPage.SERIES_PARAMETER).toString();
            String y = params.get(AbstractSearchPage.Y_PARAMETER).toString();
            return new SearchPage(dashboardReportingService.convertWidgetDefinitionToAssetCriteria(widgetDefinitionId,x,y,series));
        }
    }

    public class DefaultSourcePageFactory implements PageFactory<SearchPage> {
        public DefaultSourcePageFactory(PageParameters params) {
        }

        @Override public SearchPage createPage() {
            return new SearchPage(dashboardReportingService.getDefaultAssetSearchCritieriaModel());
        }
    }

}
