package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class SearchMenu extends Panel {

	public SearchMenu(String id, final Mediator mediator) {
		super(id);
		add(new Link("showFilters") {
			@Override public void onClick() {
				mediator.handleEvent(null, this);
			}
			
		});
		
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
