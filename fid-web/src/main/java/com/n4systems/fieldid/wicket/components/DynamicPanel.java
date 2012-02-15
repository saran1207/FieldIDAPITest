package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

import com.google.common.base.Preconditions;

@SuppressWarnings("serial")
public class DynamicPanel extends Panel {

	public static final String CONTENT_ID = "content";

	public DynamicPanel(String id) {
		super(id);
		setVisible(false);
		setOutputMarkupId(true);
		add(new WebMarkupContainer(CONTENT_ID));
	}

	public DynamicPanel setContent(Component component) {
		setVisible(true);
		Preconditions.checkArgument(component.getId().equals(CONTENT_ID), DynamicPanel.class.getSimpleName() + " contained component must use the id " + CONTENT_ID); 			
		component.setOutputMarkupPlaceholderTag(true);
		replace(component);
		return this;
	}	

}
