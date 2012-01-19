package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class ResultsContentPanel extends Panel {

	private Mediator mediator;

	public ResultsContentPanel(String id, Mediator mediator) {
		super(id);
		this.mediator = mediator;
	}

}
