package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.search.SaveSearchPage;
import com.n4systems.model.saveditem.SavedSearchItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class SaveAssetSearchPage extends SaveSearchPage<SavedSearchItem> {

    @SpringBean
    private SavedAssetSearchService savedAssetSearchService;

    public SaveAssetSearchPage(SavedSearchItem savedItem, WebPage backToPage, boolean overwrite) {
        super(savedItem, backToPage, overwrite);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.search"));
    }

    @Override
    protected IModel<String> createSavedItemDescriptionModel() {
        return new FIDLabelModel("label.saved_search_details");
    }

    @Override
    protected IModel<String> createSavedConfirmationModel() {
        return new FIDLabelModel("message.search_saved");
    }

    @Override
    protected void saveSearch(SavedSearchItem item, boolean overwrite, String name) {
        savedAssetSearchService.saveReport(item, overwrite, name);
    }

}
