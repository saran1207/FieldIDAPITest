package com.n4systems.fieldid.wicket.components.massupdate;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class EditDetailsPanel extends Panel {
	
	private Panel previousPanel;

	public EditDetailsPanel(String id, IModel<?> model, Panel previousPanel) {
		super(id, model);
		this.previousPanel = previousPanel;
		add(new Link("cancelLink") {
			@Override
			public void onClick() {
				onCancel();
			}
		});
	}
	
	protected void onCancel() {};

	public Panel getPreviousPanel() {
		return previousPanel;
	}
}
