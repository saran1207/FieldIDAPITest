package com.n4systems.fieldid.wicket.pages.massupdate;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.components.massupdate.asset.MassUpdateAssetsPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.search.AssetSearchCriteria;

public class MassUpdateAssetsPage extends FieldIDFrontEndPage {

	public MassUpdateAssetsPage(IModel<AssetSearchCriteria> criteriaModel) {
		add(new MassUpdateAssetsPanel("contentPanel", criteriaModel));
	}
	
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/massupdate/mass_update_assets.css");
    }
}
