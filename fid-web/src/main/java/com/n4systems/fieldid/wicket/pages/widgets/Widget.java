package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;

@SuppressWarnings("serial")
public abstract class Widget<W extends WidgetConfiguration> extends Panel {

	private IModel<WidgetDefinition<W>> widgetDefinition;

    protected ContextImage removeButton;
    protected ContextImage configureButton;
    

    public Widget(String id, IModel<WidgetDefinition<W>> widgetDefinition) {
        super(id, widgetDefinition);
        this.widgetDefinition = widgetDefinition;
        setOutputMarkupId(true);
        Label subTitleLabel = new Label("subTitleLabel", getSubTitleModel()); 
        add(new Label("titleLabel", new PropertyModel<String>(widgetDefinition, "config.name")));
        add(subTitleLabel);
        add(new ContextImage("dragImage", "images/dashboard/drag.png"));        
        add(new AbstractBehavior () {
			@Override public void renderHead(IHeaderResponse response) {				
				response.renderOnDomReadyJavascript("widgetToolkit.registerWidget('"+getMarkupId()+"');");
			}
        });
        if (StringUtils.isBlank(subTitleLabel.getDefaultModelObjectAsString())) {
        	subTitleLabel.setVisible(false);
        }
        addButtons();
    }
    
	protected IModel<String> getSubTitleModel() {
		return new Model<String>();
	}
	
	private void addButtons() {
        add(removeButton = new ContextImage("removeButton", "images/dashboard/x.png"));
        add(configureButton = new ContextImage("configureButton", "images/dashboard/config.png"));
    }

	public Widget<W> setRemoveBehaviour(AjaxEventBehavior behaviour) {
		removeButton.add(behaviour);
		return this;
	}

	public Widget<W> setConfigureBehavior(AjaxEventBehavior behaviour) {
		configureButton.add(behaviour);
		return this;
	}

    public IModel<WidgetDefinition<W>> getWidgetDefinition() {
    	return widgetDefinition;
    }    

	public abstract Component createConfigPanel(String id);

}
