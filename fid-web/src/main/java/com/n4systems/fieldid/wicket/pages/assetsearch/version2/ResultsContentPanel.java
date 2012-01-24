package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchResultsPanel;
import com.n4systems.model.search.AssetSearchCriteriaModel;


@SuppressWarnings("serial")
public class ResultsContentPanel extends Panel {

	private Mediator mediator;

	public ResultsContentPanel(String id, Model<AssetSearchCriteriaModel> model, Mediator mediator) {
		super(id);
		this.mediator = mediator;
		setOutputMarkupId(true);
		add(new AssetSearchResultsPanel("results", model));
	}

}
