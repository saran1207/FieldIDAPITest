package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;


public class SearchBlankSlate extends Panel {

	public SearchBlankSlate(String id) {
		super(id);
		add(new WebMarkupContainer("blankSlate"));
	}


}
