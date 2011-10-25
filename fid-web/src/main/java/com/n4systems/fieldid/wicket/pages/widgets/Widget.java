package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.model.dashboard.WidgetDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

@SuppressWarnings("serial")
public class Widget extends Panel {

    private static final String CONTENT_ID = "content";

    private IModel<WidgetDefinition> widgetDefinition;

    private ContextImage removeButton;
    private ContextImage configureButton;

    public Widget(String id, IModel<WidgetDefinition> widgetDefinition) {
        super(id);
        this.widgetDefinition = widgetDefinition;
        setOutputMarkupId(true);
        add(new Label("titleLabel", new PropertyModel<String>(widgetDefinition, "name")));
        addButtons();
        add(new ContextImage("dragImage", "images/dashboard/drag.png"));
    }

    private void addButtons() {
        add(removeButton = new ContextImage("removeButton", "images/dashboard/x.png"));
        add(configureButton = new ContextImage("configureButton", "images/dashboard/config.png"));
        configureButton.setVisible(widgetDefinition.getObject().getWidgetType().isConfigurable());
    }

    public void addContent(Component content) {
        add(content);        
    }

    public String getContentId() {
        return CONTENT_ID;
    }

	public Widget withRemoveBehaviour(AjaxEventBehavior behaviour) {
		removeButton.add(behaviour);
		return this;
	}

}
