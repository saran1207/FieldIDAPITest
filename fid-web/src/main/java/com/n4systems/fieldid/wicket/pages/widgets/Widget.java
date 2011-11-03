package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

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
        setOutputMarkupId(true).setMarkupId(getCssClassWithSuffix("Widget"));
        add(new Label("titleLabel", new PropertyModel<String>(widgetDefinition, "config.name")));
        add(new ContextImage("dragImage", "images/dashboard/drag.png"));        
        addButtons();
        add(createConfigPanel("config"));
    }
    
	private String getCssClassWithSuffix(String suffix) {
		return getWidgetDefinition().getObject().getWidgetType().getCamelCase()+suffix;
	}

	private void addButtons() {
        add(removeButton = new ContextImage("removeButton", "images/dashboard/x.png"));
        add(configureButton = new ContextImage("configureButton", "images/dashboard/config.png"));
        configureButton.setOutputMarkupId(true).setMarkupId(configureButton.getId());

        String js = "$('.widget-content').slideToggle();$('.widget-config').slideToggle();";
        configureButton.add(new SimpleAttributeModifier("onclick", js));        
    }

	public Widget<W> setRemoveBehaviour(AjaxEventBehavior behaviour) {
		removeButton.add(behaviour);
		return this;
	}

	@Deprecated //not needed anymore??
    public Widget<W> setConfigureBehaviour(AjaxEventBehavior behaviour) {
        configureButton.add(behaviour);
        return this;
    }

    public IModel<WidgetDefinition<W>> getWidgetDefinition() {
    	return widgetDefinition;
    }

	protected abstract Component createConfigPanel(String id);

}
