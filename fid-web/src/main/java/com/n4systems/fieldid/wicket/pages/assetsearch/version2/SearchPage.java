package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.components.assetsearch.SearchBlankSlatePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.SearchCriteriaPanel;

@SuppressWarnings("serial")
public class SearchPage extends FieldIDFrontEndPage {

    @SpringBean
    private AssetService assetService;

    public SearchPage() {
        if (assetService.countAssets() == 0) {
        	// XXX : WEB-2706 requires new blank slate here. won't be based on asset count though.
            add(new SearchBlankSlatePanel("contentPanel"));
        } else {
            add(new SearchCriteriaPanel("contentPanel"));
        }
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.assetsearch"));
    }

}
