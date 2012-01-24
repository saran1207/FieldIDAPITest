package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.n4systems.model.AssetType;
import com.n4systems.model.search.AssetSearchCriteriaModel;

@SuppressWarnings("serial")
public class SearchConfigPanel extends Panel implements Mediator {

	private SearchFilterPanel filter;
	private SearchColumnsPanel columns;
	private Mediator mediator;
	

	public SearchConfigPanel(String id, Model<AssetSearchCriteriaModel> model, Mediator mediator) {
		super(id, model);
		setRenderBodyOnly(true);
		this.mediator = mediator;
		add(filter = new SearchFilterPanel("filter", model, this) {
			@Override
			protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {
				columns.updateDynamicAssetColumns(selectedAssetType, availableAssetTypes);
				target.add(columns);
			};
		});
		add(columns = new SearchColumnsPanel("columns",model, this));		
	}

	@Override
	public void handleEvent(AjaxRequestTarget target, Component component) {
		mediator.handleEvent(target, component);
	}

}
