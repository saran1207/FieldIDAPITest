package com.n4systems.fieldid.wicket.components.massupdate;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public abstract class AbstractMassUpdatePanel extends Panel {

	protected AbstractMassUpdatePanel previousPanel;

	public AbstractMassUpdatePanel(String id) {
		super(id);
	}

	public AbstractMassUpdatePanel(String id, IModel<?> model) {
		super(id, model);
	}

	public AbstractMassUpdatePanel getPreviousPanel() {
		return previousPanel;
	}
	
	protected void onCancel() {}

	public boolean isDetailsPanel() {
		return false;
	}

	public boolean isConfirmPanel() {
		return false;
	}

}