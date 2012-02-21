package com.n4systems.fieldid.wicket.components.search.results;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.search.AssetSearchCriteriaModel;

@SuppressWarnings("serial")
public class SearchConfigPanel extends Panel {		
	
	private Component filters;
	private Component columns;
	private Model<AssetSearchCriteriaModel> model;
	
	public SearchConfigPanel(String id, final Model<AssetSearchCriteriaModel> model) {
		super(id, model);
		this.model = model;
		SearchConfigForm form = new SearchConfigForm("form", new CompoundPropertyModel<AssetSearchCriteriaModel>(model));
		form.add(createSaveLink("save",true));
		form.add(createSaveLink("saveAs",false));
		form.add(new Button("submit"));
		form.add(new Label("title", new Model<String>() { 
			@Override public String getObject() {
				return getTitle();
			}
		}));
		form.add(new WebMarkupContainer("close").add(createCloseBehavior()));
		form.add(filters = new SearchFilterPanel("filters",model));
		form.add(columns = new SearchColumnsPanel("columns",model));
		add(form);		
		showFilters();
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

	private String getTitle() {		
		return filters.isVisible() ? getString("label.filters") : getString("label.columns");
	}

	public void showColumns() {
		showFilters(false);
	}

	public void showFilters() {
		showFilters(true);		
	}
	
	public void showFilters(boolean v) {
		filters.setVisible(v);
		columns.setVisible(!v);		
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
