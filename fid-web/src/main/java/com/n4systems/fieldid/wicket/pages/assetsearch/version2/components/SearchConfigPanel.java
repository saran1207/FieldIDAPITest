package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;

import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.search.AssetSearchCriteriaModel;

public class SearchConfigPanel extends Panel {		
	
	private SearchFilterPanel filters;
	private SearchColumnsPanel columns;
	private Model<AssetSearchCriteriaModel> model;
	private FIDModalWindow modal;
	
	public SearchConfigPanel(String id, final Model<AssetSearchCriteriaModel> model) {
		super(id, model);
		this.model = model;
		
		SearchConfigForm form = new SearchConfigForm("form", new CompoundPropertyModel<AssetSearchCriteriaModel>(model));
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

    private Link createSaveLink(String linkId, final boolean overwrite) {
        Link link = new Link(linkId) {
            @Override public void onClick() {
                setResponsePage(getSaveResponsePage(overwrite));
            }
        };
        if (!overwrite) {
            // If this is not overwrite (ie the Save As link), it should be invisible if this isn't an existing saved report
            link.setVisible(isExistingSavedItem());
        }
        return link;
    }

    protected boolean isExistingSavedItem() {
		return true;
	}

	protected Page getSaveResponsePage(boolean overwrite) {
    	throw new UnsupportedOperationException("override this to redirect on Save actions");
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

	 public class SearchConfigForm extends Form<AssetSearchCriteriaModel> {

      	public SearchConfigForm(String id, final IModel<AssetSearchCriteriaModel> model) {
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
