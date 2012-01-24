package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.n4systems.model.AssetType;
import com.n4systems.model.search.AssetSearchCriteriaModel;

@SuppressWarnings("serial")
public class SearchConfigPanel extends Panel implements FormListener {

	private SearchFilterPanel filter;
	private SearchColumnsPanel columns;
	private FormListener formListener;
	

	public SearchConfigPanel(String id, Model<AssetSearchCriteriaModel> model, FormListener formListener) {
		super(id, model);
		setRenderBodyOnly(true);
		this.formListener = formListener;
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
		formListener.handleEvent(target, component);
	}

}
