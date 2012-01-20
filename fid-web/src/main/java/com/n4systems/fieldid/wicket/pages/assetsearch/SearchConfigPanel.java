package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;


@SuppressWarnings("serial")
public abstract class SearchConfigPanel extends Panel {
	
	private Mediator mediator;

	public SearchConfigPanel(String id, Mediator mediator) {
		super(id);
		this.mediator = mediator;
		setOutputMarkupId(true);		
		add(createMenu("topMenu"));
		add(createMenu("bottomMenu"));		
	}

	protected abstract void updateMenu(Component... component);

	protected Component createMenu(String id) {
		WebMarkupContainer menu = new WebMarkupContainer(id);
		WebMarkupContainer search;
		WebMarkupContainer filters;
		WebMarkupContainer columns;
		
		menu.add(search = new IndicatingAjaxLink("search") {
			@Override public void onClick(AjaxRequestTarget target) {
				SearchConfigPanel.this.mediator.handleEvent(target,this);
			}
		});		
		menu.add(filters = new WebMarkupContainer("filters"));
		menu.add(columns = new WebMarkupContainer("columns"));		
		updateMenu(search, filters, columns);
		return menu;
	}
	
	
	protected Behavior createShowConfigBehavior(final String cssClass) {
		return new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			@Override public JsScope callback() {
				// hide any current config, then display desired one.  
				return JsScopeUiEvent.quickScope("$('.search .config').hide();$('."+cssClass+"').show();");
			}
		});
	}	
	
	
}

