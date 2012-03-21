package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.search.AssetSearchCriteria;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;

import java.util.List;

public class SearchConfigPanel extends Panel {		
	
	private SearchFilterPanel filters;
	private SearchColumnsPanel columns;
	private FIDModalWindow modal;
    private Model<AssetSearchCriteria> model;
	
	public SearchConfigPanel(String id, final Model<AssetSearchCriteria> model) {
		super(id, new CompoundPropertyModel<AssetSearchCriteria>(model));
        this.model = model;

		SearchConfigForm form = new SearchConfigForm("form",model);
		form.add(new Button("submit"));
		form.add(new WebMarkupContainer("close").add(createCloseBehavior()));
		form.add(columns = new SearchColumnsPanel("columns",model));
		form.add(filters = new SearchFilterPanel("filters",model) {
			@Override
			protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {
				columns.updateAssetTypeOrGroup(target, selectedAssetType, availableAssetTypes);
				// since we know that this can only occur when Filters panel is visible (.: we are not), we hide it.
				columns.add(new AttributeModifier("style", "display:none;"));  
			}			
		});
		
		add(form);		
	}

	protected void onNoDisplayColumnsSelected() {
	}
	
	protected void onSearchSubmit() {
	}
	
	protected Behavior createCloseBehavior() {
		return new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			@Override public JsScope callback() {
				return JsScopeUiEvent.quickScope(SearchSubMenu.HIDE_JS);
			}
		});
	}	

	 public class SearchConfigForm extends Form<AssetSearchCriteria> {

      	public SearchConfigForm(String id, final IModel<AssetSearchCriteria> model) {
        	super(id, model);
          	setOutputMarkupId(true); 
      	}
      	
      	@Override
      	protected void onSubmit() {
			 if (model.getObject().getSortedStaticAndDynamicColumns().isEmpty()) {
                error(new FIDLabelModel("error.nocolumnsselected").getObject());
                onNoDisplayColumnsSelected();
                return;
	         }
			 model.getObject().setReportAlreadyRun(true);
			 model.getObject().getSelection().clear();
			 onSearchSubmit();
      	}
      	
	 }	
	      	
	
}
