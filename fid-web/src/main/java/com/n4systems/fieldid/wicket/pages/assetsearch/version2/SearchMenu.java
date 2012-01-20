package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;


@SuppressWarnings("serial")
public class SearchMenu extends Panel {

	private Component showFilters;
	private Component showColumns;
	
	public SearchMenu(String id, final Mediator mediator) {
		super(id);
		add(showFilters = new WebMarkupContainer("showFilters").add(new AttributeAppender("class", new Model<String>("showFilters"), " ")));
		add(showColumns = new WebMarkupContainer("showColumns").add(new AttributeAppender("class", new Model<String>("showFilters"), " ")));

		showFilters.add(createToggleBehavior("filter",""));
		
		showColumns.add(createToggleBehavior("columns",""));
		
		add(new AjaxLink("printCertificates"){
			@Override public void onClick(AjaxRequestTarget target) {
				 mediator.handleEvent(target, this);				
			}			
		});
		add(new AjaxLink("exportToExcel"){
			@Override public void onClick(AjaxRequestTarget target) {
				 mediator.handleEvent(target, this);				
			}			
		});
		add(new AjaxLink("massActions"){
			@Override public void onClick(AjaxRequestTarget target) {
				 mediator.handleEvent(target, this);				
			}			
		});		
	
	}

	private Behavior createToggleBehavior(final String cssClass, final String showHide) {
		return new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			@Override public JsScope callback() {
				return JsScopeUiEvent.quickScope("$('."+cssClass+"').toggle("+showHide+");");
			}
		});
	}	

}
