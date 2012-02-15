package com.n4systems.fieldid.wicket.components.search.results;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class SearchSubMenu extends Panel {
	
	public SearchSubMenu(String id) {
		super(id);
		add(new SubMenuLink("columns"));
		add(new SubMenuLink("filters"));
	}	
	
	class SubMenuLink extends AjaxLink  {

		public SubMenuLink(String id) {
			super(id);
		}

		@Override
		public void onClick(AjaxRequestTarget target) {
			SearchSubMenu.this.onClick(target, getId());
		}

	}

	protected void onClick(AjaxRequestTarget target, String id) {
	} 
	
	
}
