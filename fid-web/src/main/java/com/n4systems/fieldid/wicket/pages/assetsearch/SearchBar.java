package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class SearchBar extends Panel {

	public SearchBar(String id) {
		super(id);
		add(new AjaxLink("results") {
			@Override public void onClick(AjaxRequestTarget target) {
				System.out.println("RESULTS");
			}			
		});
	}

}
