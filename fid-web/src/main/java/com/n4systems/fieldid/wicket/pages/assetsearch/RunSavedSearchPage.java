package com.n4systems.fieldid.wicket.pages.assetsearch;

import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.saveditem.SavedSearchItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RunSavedSearchPage extends FieldIDFrontEndPage {

    @SpringBean
    private SavedAssetSearchService savedSearchService;

    public RunSavedSearchPage(PageParameters params) {
        Long id = params.get("id").toLong();

        SavedSearchItem savedSearchItem = savedSearchService.getConvertedReport(SavedSearchItem.class, id);

        savedSearchItem.getSearchCriteria().setSavedReportId(id);
        savedSearchItem.getSearchCriteria().setSavedReportName(savedSearchItem.getName());

        setResponsePage(new AssetSearchResultsPage(savedSearchItem));
    }

}
