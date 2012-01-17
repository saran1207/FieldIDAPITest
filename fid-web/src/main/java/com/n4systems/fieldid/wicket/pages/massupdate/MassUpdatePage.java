package com.n4systems.fieldid.wicket.pages.massupdate;

import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.components.massupdate.MassUpdateAssetsPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.search.AssetSearchCriteriaModel;

public class MassUpdatePage extends FieldIDFrontEndPage {

	public MassUpdatePage(IModel<AssetSearchCriteriaModel> criteriaModel) {
		add(new MassUpdateAssetsPanel("contentPanel", criteriaModel));
	}
}
