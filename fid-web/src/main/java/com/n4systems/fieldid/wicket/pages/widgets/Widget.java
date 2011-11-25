package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.google.common.collect.Lists;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.ChartDateRange;

@SuppressWarnings("serial")
public abstract class Widget<W extends WidgetConfiguration> extends Panel {

	private IModel<WidgetDefinition<W>> widgetDefinition;

    protected ContextImage removeButton;
    protected ContextImage configureButton;
	private Component configPanel;
    

    public Widget(String id, IModel<WidgetDefinition<W>> widgetDefinition) {
        super(id, widgetDefinition);
        this.widgetDefinition = widgetDefinition;
        setOutputMarkupId(true);
        Label subTitleLabel = new Label("subTitleLabel", getSubTitleModel()); 
        add(new AttributeAppender("class", new Model<String>(getCssClassWithSuffix("Widget")), " "));
        add(new Label("titleLabel", new PropertyModel<String>(widgetDefinition, "config.name")));
        add(subTitleLabel);
        add(new ContextImage("dragImage", "images/dashboard/drag.png"));        
        add(configPanel = createDecoratedConfigPanel("configPanel"));
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
	
	protected String getRangeFilterSubTitle(ChartDateRange dateRange, BaseOrg owner) {
		return dateRange.getDisplayName() + " " + (owner==null?"nullOwner" : owner.getDisplayName());
	}
	
	protected String getPeriodFilterSubTitle(Integer period, BaseOrg owner) {
		return period + "days " + (owner==null?"nullOwner" : owner.getDisplayName());
	}	

	private void addButtons() {
        add(removeButton = new ContextImage("removeButton", "images/dashboard/x.png"));
        add(configureButton = new ContextImage("configureButton", "images/dashboard/config.png"));
    }

	public Widget<W> setRemoveBehaviour(AjaxEventBehavior behaviour) {
		removeButton.add(behaviour);
		return this;
	}

    public IModel<WidgetDefinition<W>> getWidgetDefinition() {
    	return widgetDefinition;
    }    

	private String getCssClassWithSuffix(String suffix) {
		return getWidgetDefinition().getObject().getWidgetType().getCamelCase()+suffix;
	}   

	private Component createDecoratedConfigPanel(String id) {
		Component panel = createConfigPanel(id);
		panel.add(new AttributeAppender("class", new Model<String>(getCssClassWithSuffix("Config")), " " ));
		return panel;
	}

	protected abstract Component createConfigPanel(String id);

	protected IModel<String> getRangeOrgSubTitleModel(BaseOrg org) {
		List<PropertyModel<String>> models = getOrgSubTitleModel(org);
		String key = null;
		switch (models.size()) { 
		case 0: 
			key = "dateRange.subTitle";
			break;
		case 1:
			key = "dateRange.org.subTitle";
			break;
		case 2:
			key = "dateRange.org.customer.subTitle";
			break;
		case 3:
			key = "dateRange.org.customer.division.subTitle";
			break;
		default : 
			throw new IllegalStateException("can't format subtitle for org [" + org +"]");
		}
		models.add(0, new PropertyModel<String>(getWidgetDefinition(), "config.dateRange.fromDateDisplayString")); 
		models.add(1, new PropertyModel<String>(getWidgetDefinition(), "config.dateRange.toDateDisplayString"));
		return new StringResourceModel(key, this, null, models.toArray() );		
	}

	@SuppressWarnings("unchecked")
	private final List<PropertyModel<String>> getOrgSubTitleModel(BaseOrg org) {		
		List<PropertyModel<String>> models = Lists.newArrayList( 
				new PropertyModel<String>(getWidgetDefinition(), "config.org.primaryOrg.name"),
				new PropertyModel<String>(getWidgetDefinition(), "config.org.customerOrg.name"),
				new PropertyModel<String>(getWidgetDefinition(), "config.org.divisionOrg.name"));
		
		if (org==null) {
			models = Lists.newArrayList();
		} else if (org.getCustomerOrg()==null) { 
			models = models.subList(0, 1);						
		} else if (org.getDivisionOrg()==null) {
			models = models.subList(0, 2);									
		}		
		return models;
	}	
	
	protected IModel<String> getPeriodOrgSubTitleModel(BaseOrg org) {
		List<PropertyModel<String>> models = getOrgSubTitleModel(org);
		String key = "dateRange.org.customer.division.subTitle";
		switch (models.size()) { 
		case 0: 
			key = "dateRange.subTitle";
			break;
		case 1:
			key = "dateRange.org.subTitle";
			break;
		case 2:
			key = "dateRange.org.customer.subTitle";
			break;
		case 3:
			key = "dateRange.org.customer.division.subTitle";
			break;
		default : 
			throw new IllegalStateException("can't format subtitle for org [" + org +"]");
		}
		models.add(0, new PropertyModel<String>(getWidgetDefinition(), "config.chartPeriod.fromDisplayString"));
		models.add(1, new PropertyModel<String>(getWidgetDefinition(), "config.chartPeriod.toDisplayString")); 
		return new StringResourceModel(key, this, null, models.toArray() );		
	}
	
	protected IModel<String> getRangeSubTitleModel() {
		List<PropertyModel<String>> models = Lists.newArrayList( 
				new PropertyModel<String>(getWidgetDefinition(), "config.dateRange.fromDateDisplayString"),  
				new PropertyModel<String>(getWidgetDefinition(), "config.dateRange.toDateDisplayString"));
		
		return new StringResourceModel("dateRange.subTitle", this, null, models.toArray() );
	}	
	

}
