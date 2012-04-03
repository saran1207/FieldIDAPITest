package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchCriteriaPanel;
import com.n4systems.fieldid.wicket.components.assetsearch.SearchBlankSlatePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchPage;

public class AssetSearchPage extends FieldIDFrontEndPage {

    @SpringBean
    private AssetService assetService;

    public AssetSearchPage() {
        if (assetService.countAssets() == 0) {
            add(new SearchBlankSlatePanel("contentPanel"));
        } else {
            add(new AssetSearchCriteriaPanel("contentPanel"));
        }
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.assetsearch"));
    }
    
    @Override
    protected Component createHeaderLink(String id, String label) {
    	BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(id, SearchPage.class);
    	pageLink.add(new FlatLabel(label, new FIDLabelModel("label.search2")));
    	return pageLink;
    }
    
}
