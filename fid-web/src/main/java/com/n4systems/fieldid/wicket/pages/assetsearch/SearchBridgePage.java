package com.n4systems.fieldid.wicket.pages.assetsearch;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.wicket.model.LocalizeAround;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.PageFactory;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.services.reporting.DashboardReportingService;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.concurrent.Callable;

// used as a bridge to redirect to search/report pages.
// basically reponsible for creating criteria based on the input.
public abstract class SearchBridgePage<P extends FieldIDFrontEndPage, T extends SearchCriteria, S extends SavedItem> extends FieldIDFrontEndPage {

    private @SpringBean SavedAssetSearchService savedSearchService;
    private @SpringBean DashboardReportingService dashboardReportingService;


    public SearchBridgePage(PageParameters params) {
        StringValue source = params.get(AbstractSearchPage.SOURCE_PARAMETER);
        Preconditions.checkArgument(source != null, "you must specify " + AbstractSearchPage.SOURCE_PARAMETER + " in order to create page");
        PageFactory<P> factory = getPageFactory(source.toString(), params);
        Preconditions.checkArgument(factory != null, "cannot find factory to build page with source == " + source.toString());
        setResponsePage(factory.createPage());
    }

    protected PageFactory<P> getPageFactory(String source, PageParameters params) {
        if (AbstractSearchPage.SAVED_ITEM_SOURCE.equals(source)) {
            return new SavedItemSourcePageFactory(params);
        } else if (AbstractSearchPage.WIDGET_SOURCE.equals(source)) {
            return new WidgetSourcePageFactory(params);
        }
        return null;
    }

    protected abstract S getSavedItem(Long id);

    protected abstract P createPage(T criteria, S savedItem);

    protected abstract T getCriteriaFromWidget(Long widgetDefinitionId, Long x, String y, String series);

    // --------------------------------------------------------------------------------------------


    public class SavedItemSourcePageFactory implements PageFactory<P> {

        private PageParameters params;

        public SavedItemSourcePageFactory(PageParameters params) {
            this.params = params;
        }

        @Override public P createPage() {
            final Long id = params.get("id").toLong();
            S savedItem = new LocalizeAround<S>(new Callable<S>() {
                @Override
                public S call() throws Exception {
                    return getSavedItem(id);
                }
            }).call();
            savedItem.getSearchCriteria().setSavedReportId(id);
            savedItem.getSearchCriteria().setSavedReportName(savedItem.getName());
            return SearchBridgePage.this.createPage((T) savedItem.getSearchCriteria(), savedItem);
        }
    }

    public class WidgetSourcePageFactory implements PageFactory<P> {

        private PageParameters params;

        public WidgetSourcePageFactory(PageParameters params) {
            this.params = params;
        }

        @Override public P createPage() {
            Long widgetDefinitionId = params.get(AbstractSearchPage.WIDGET_DEFINITION_PARAMETER).toLong();
            
            StringValue xParam = params.get(AbstractSearchPage.X_PARAMETER);
            // CAVEAT : make sure you pass a valid number for X!
            Long x = xParam.toString()==null?null:xParam.toLong();
            
            String series = params.get(AbstractSearchPage.SERIES_PARAMETER).toString();

            String y = params.get(AbstractSearchPage.Y_PARAMETER).toString();

            T criteria = getCriteriaFromWidget(widgetDefinitionId,x,y,series);
            return SearchBridgePage.this.createPage(criteria, null);
        }

    }


}
