package com.n4systems.fieldid.wicket.pages.assetsearch;

import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.search.AssetSearchCriteria;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RunLastSearchPage extends FieldIDFrontEndPage {

    @SpringBean
    private SavedAssetSearchService savedAssetSearchService;

    public RunLastSearchPage() {
        final AssetSearchCriteria lastSearch = savedAssetSearchService.retrieveLastSearch();
        if (lastSearch != null) {
            setResponsePage(new AssetSearchResultsPage(lastSearch));
        } else {
            setResponsePage(DashboardPage.class);
        }
    }

}
