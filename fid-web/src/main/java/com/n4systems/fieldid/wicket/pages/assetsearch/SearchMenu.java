package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
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

	public SearchMenu(String id, final Mediator mediator) {
		super(id);
		Component show;
		add(show = new WebMarkupContainer("showFilters").add(new AttributeAppender("class", new Model<String>("showFilters"), " ")));

		show.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {
			@Override public JsScope callback() {
				return JsScopeUiEvent.quickScope("$('.filter').toggle();");
			}
		}));
		
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

}
