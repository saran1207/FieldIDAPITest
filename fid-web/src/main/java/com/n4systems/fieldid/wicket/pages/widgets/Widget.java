package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.pages.widgets.config.WidgetConfigPanel;
import com.n4systems.fieldid.wicket.util.AjaxCallback;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;

@SuppressWarnings("serial")
public abstract class Widget<W extends WidgetConfiguration> extends Panel {

	private IModel<WidgetDefinition<W>> widgetDefinition;

    protected ContextImage removeButton;
    protected ContextImage configureButton;

    public Widget(String id, IModel<WidgetDefinition<W>> widgetDefinition) {
        super(id);
        this.widgetDefinition = widgetDefinition;
        setOutputMarkupId(true);
        add(new Label("titleLabel", new PropertyModel<String>(widgetDefinition, "config.name")));
        addButtons();
        add(new ContextImage("dragImage", "images/dashboard/drag.png"));
    }

	private void addButtons() {
        add(removeButton = new ContextImage("removeButton", "images/dashboard/x.png"));
        add(configureButton = new ContextImage("configureButton", "images/dashboard/config.png"));
    }

	public Widget<W> setRemoveBehaviour(AjaxEventBehavior behaviour) {
		removeButton.add(behaviour);
		return this;
	}

    public Widget<W> setConfigureBehaviour(AjaxEventBehavior behaviour) {
        configureButton.add(behaviour);
        return this;
    }

    public WidgetConfigPanel<W> createConfigurationPanel(String id, IModel<W> config, final AjaxCallback<Boolean> saveCallback) {
        return new WidgetConfigPanel<W>(id, config, saveCallback);
    }
    
    public IModel<WidgetDefinition<W>> getWidgetDefinition() {
    	return widgetDefinition;
    }

}
