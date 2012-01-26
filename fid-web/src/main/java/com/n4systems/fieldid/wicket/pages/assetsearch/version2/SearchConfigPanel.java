package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.n4systems.model.AssetType;
import com.n4systems.model.search.AssetSearchCriteriaModel;

@SuppressWarnings("serial")
public class SearchConfigPanel extends Panel implements FormListener {

	// hide configuration left hand panels and their (possible) children.
	public static final String HIDE_JS = "$('.search .config').hide(); $('.locationSelection').remove(); $('.orgSelector').remove();";
	
	
	private SearchFilterPanel filter;
	private SearchColumnsPanel columns;
	private FormListener formListener;
	

	public SearchConfigPanel(String id, IModel<AssetSearchCriteriaModel> model, FormListener formListener, boolean showConfig) {
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
		
		if (!showConfig) { 
			AttributeAppender hideStyle = new AttributeAppender("style", new Model<String>("display:none"), ";");
			filter.add(hideStyle);
			columns.add(hideStyle);
		}
	}

	@Override
	public void handleEvent(AjaxRequestTarget target, Component component, Form<?> form) {
		formListener.handleEvent(target, component, form);
	}

}
