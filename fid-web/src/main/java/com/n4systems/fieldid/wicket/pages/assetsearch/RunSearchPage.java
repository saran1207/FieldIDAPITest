package com.n4systems.fieldid.wicket.pages.assetsearch;

import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.wicket.pages.PageFactory;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchBridgePage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchPage;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.services.reporting.DashboardReportingService;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RunSearchPage extends SearchBridgePage<SearchPage, AssetSearchCriteria, SavedSearchItem> {

    private @SpringBean SavedAssetSearchService savedSearchService;
    private @SpringBean DashboardReportingService dashboardReportingService;


    public RunSearchPage(PageParameters params) {
        super(params);
    }

    public PageFactory<SearchPage> getPageFactory(String source, PageParameters params) {
        PageFactory<SearchPage> factory = super.getPageFactory(source, params);
        return (factory==null) ? new DefaultSourcePageFactory(params) : factory;
    }

    @Override
    protected SavedSearchItem getSavedItem(Long id) {
        return savedSearchService.getConvertedReport(SavedSearchItem.class, id);
    }

    @Override
    protected SearchPage createPage(AssetSearchCriteria criteria, SavedSearchItem savedItem) {
        return new SearchPage(criteria, savedItem);
    }

    @Override
    protected AssetSearchCriteria getCriteriaFromWidget(Long widgetDefinitionId, Long x, String y, String series) {
        return dashboardReportingService.convertWidgetDefinitionToAssetCriteria(widgetDefinitionId,x,y,series);
    }


    // --------------------------------------------------------------------------------------------


    public class DefaultSourcePageFactory implements PageFactory<SearchPage> {
        public DefaultSourcePageFactory(PageParameters params) {
        }

        @Override public SearchPage createPage() {
            return new SearchPage(dashboardReportingService.getDefaultAssetSearchCritieria());
        }
    }

}
