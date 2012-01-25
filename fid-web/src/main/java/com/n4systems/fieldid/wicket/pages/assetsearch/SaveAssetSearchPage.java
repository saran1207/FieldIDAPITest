package com.n4systems.fieldid.wicket.pages.assetsearch;

import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.wicket.pages.search.SaveSearchPage;
import com.n4systems.model.saveditem.SavedSearchItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class SaveAssetSearchPage extends SaveSearchPage<SavedSearchItem> {

    @SpringBean
    private SavedAssetSearchService savedAssetSearchService;

    public SaveAssetSearchPage(SavedSearchItem savedItem, WebPage backToPage, boolean overwrite) {
        super(savedItem, backToPage, overwrite);
    }

    @Override
    protected void saveSearch(SavedSearchItem item, boolean overwrite, String name) {
        savedAssetSearchService.saveReport(item, overwrite, name);
    }

}
