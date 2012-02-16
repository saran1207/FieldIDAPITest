package com.n4systems.fieldid.wicket.components.search.results;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchMassActionPanel;
import com.n4systems.model.search.AssetSearchCriteriaModel;

@SuppressWarnings("serial")
public class SearchSubMenu extends Panel {
	
	public static final String HIDE_JS = "fieldIdWidePage.hideLeftMenu()";
	public static final String SHOW_JS = "fieldIdWidePage.showLeftMenu()";
	
	
	public SearchSubMenu(String id, Model<AssetSearchCriteriaModel> model) {
		super(id);
		add(new SubMenuLink("columns"));
		add(new SubMenuLink("filters"));
		add(new AssetSearchMassActionPanel("actions", model));		
	}	
	
	class SubMenuLink extends AjaxLink  {

		public SubMenuLink(final String id) {
			super(id);
		}

		@Override
		public void onClick(AjaxRequestTarget target) {
			SearchSubMenu.this.onClick(target, getId());
			target.appendJavaScript(SHOW_JS);
		}

	}

	protected void onClick(AjaxRequestTarget target, String id) {
	}
	
}
