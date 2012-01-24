package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;

import com.n4systems.model.search.AssetSearchCriteriaModel;



@SuppressWarnings("serial")
public abstract class AbstractSearchConfigPanel extends Panel {

	protected final String EXPAND_IMG = "images/columnlayout/arrow-over.png";
	protected final String COLLAPSE_IMG = "images/columnlayout/arrow-down.png";	
	
	// hide configuration left hand panels and their (possible) children.
	private static final String HIDE_JS = "$('.search .config').hide(); $('.locationSelection').remove(); $('.orgSelector').remove();";
	
	protected FormListener formListener;
	protected Form<AssetSearchCriteriaModel> form;

	
	public AbstractSearchConfigPanel(String id, Model<AssetSearchCriteriaModel>model, FormListener formListener) {
		super(id);
		this.formListener = formListener;
		setOutputMarkupId(true);
		
		add(form=createForm("form",model));
		form.add(createMenu("menu"));
	}

	protected abstract Form<AssetSearchCriteriaModel> createForm(String id, Model<AssetSearchCriteriaModel> model);
	
	protected abstract void updateMenu(Component... component);

	protected Component createMenu(String id) {
		WebMarkupContainer menu = new WebMarkupContainer(id);
		WebMarkupContainer search;
		WebMarkupContainer filters;
		WebMarkupContainer columns;
		
		menu.add(search = new IndicatingAjaxLink("search") {
			@Override public void onClick(AjaxRequestTarget target) {
				AbstractSearchConfigPanel.this.formListener.handleEvent(target,this);
			}
		});		
		menu.add(filters = new WebMarkupContainer("filters"));
		menu.add(columns = new WebMarkupContainer("columns"));
		
		WebMarkupContainer close = new WebMarkupContainer("x"); 
		close.add(createHideBehavior());
		menu.add(close);
		updateMenu(search, filters, columns, close);
		return menu;
	}
	

	protected Behavior createShowConfigBehavior(final String cssClass) {
		return new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			@Override public JsScope callback() {
				// hide any current config, then display desired one.  
				return JsScopeUiEvent.quickScope(HIDE_JS+"$('."+cssClass+"').show();");
			}
		});
	}	
	
	protected Behavior createHideBehavior() {
		return new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			@Override public JsScope callback() {
				return JsScopeUiEvent.quickScope(HIDE_JS);
			}
		});
	}
	
}

