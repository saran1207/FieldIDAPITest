package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchResultsPanel;
import com.n4systems.model.search.AssetSearchCriteriaModel;


public class SearchResultsPanel extends Panel {

	private AssetSearchResultsPanel results;
	private WebMarkupContainer noResults;

	public SearchResultsPanel(String id, IModel<AssetSearchCriteriaModel> model, boolean showNoResults) {
		super(id, model);		
		setOutputMarkupId(true);
		setMarkupId(id);

		add(noResults = new WebMarkupContainer("noResults"));
		add(results = new AssetSearchResultsPanel("results", model) {
        	@Override protected void updateSelectionStatus(AjaxRequestTarget target) {
        		super.updateSelectionStatus(target);
        		SearchResultsPanel.this.updateSelectionStatus(target);
        	};       				
		});
		noResults.setVisible(showNoResults);
		results.setVisible(!showNoResults);
	}

	protected void updateSelectionStatus(AjaxRequestTarget target) {
	}
    
}
