package com.n4systems.fieldid.wicket.pages.widgets;

import static com.google.common.base.Preconditions.*;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;

@SuppressWarnings("serial")
public abstract class Widget<W extends WidgetConfiguration> extends Panel {

	protected static final String HIDECONFIGJS = "$('.config').each(function() { $(this).hide(); $(this).css('marginLeft', $(this).outerWidth()); })";

	
	// TODO DD : refactor this into common .js utility file.
	private static final String SLIDE_CONFIG_JS_TEMPLATE = 
		"$('#%1$s').parent('.config').show(); $('#%1$s').parent('.config').animate(" +
		"{marginLeft: parseInt($('#%1$s').parent('.config').css('marginLeft'),10) == 0 ? $('#%1$s').parent('.config').outerWidth() : 0 " + 
        "},'fast');";

	
	private IModel<WidgetDefinition<W>> widgetDefinition;

    protected ContextImage removeButton;
    protected ContextImage configureButton;
	private Component configPanel;
    

    public Widget(String id, IModel<WidgetDefinition<W>> widgetDefinition) {
        super(id);
        this.widgetDefinition = widgetDefinition;
        setOutputMarkupId(true);
        add(new Label("titleLabel", new PropertyModel<String>(widgetDefinition, "config.name")));
        add(new ContextImage("dragImage", "images/dashboard/drag.png"));        
        add(configPanel = createDecoratedConfigPanel("config"));
        add(new AbstractBehavior () {
			@Override public void renderHead(IHeaderResponse response) {				
				response.renderOnDomReadyJavascript("widgetToolkit.registerWidget('"+getMarkupId()+"');");;
			}
        });
        addButtons();
    }
    
//	private String getCssClassWithSuffix(String suffix) {
//		return getWidgetDefinition().getObject().getWidgetType().getCamelCase()+suffix;
//	}

	private void addButtons() {
		checkNotNull(configPanel);// config panel must exist before you add configureButton.
        add(removeButton = new ContextImage("removeButton", "images/dashboard/x.png"));
        add(configureButton = new ContextImage("configureButton", "images/dashboard/config.png"));
//        configureButton.add(new SimpleAttributeModifier("onclick", String.format(SLIDE_CONFIG_JS_TEMPLATE, configPanel.getMarkupId())));        
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

	private Component createDecoratedConfigPanel(String id) {
		Component panel = createConfigPanel(id);
		panel.setOutputMarkupId(true).setMarkupId(getWidgetDefinition().getObject().getWidgetType().getCamelCase()+"Config");
		return panel;
	}

	protected abstract Component createConfigPanel(String id);

}
