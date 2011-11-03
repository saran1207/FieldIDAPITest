package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.n4systems.fieldid.wicket.components.dashboard.subcomponents.EventKpiTable;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.widgets.config.EventKPIConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.EventKPIWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;

@SuppressWarnings("serial")
public class EventKpiWidget extends Widget<EventKPIWidgetConfiguration> {
	
	public EventKpiWidget(String id, WidgetDefinition<EventKPIWidgetConfiguration> widgetDefinition) {
        super(id, new Model<WidgetDefinition<EventKPIWidgetConfiguration>>(widgetDefinition));
		setOutputMarkupId(true);
		add(CSSPackageResource.getHeaderContribution("style/dashboard/widgets/eventkpi.css"));

		List<BaseOrg> orgList = getOrgList();
		ContextImage arrow;
		add(arrow = new ContextImage("arrow", "images/dashboard/arrow.png"));
		
		if(orgList.isEmpty()) {
			Label message= new Label("eventKpiContent", new FIDLabelModel("message.configure_widget"));
			message.add(new SimpleAttributeModifier("class", "configureMessage"));
			add(message);
			arrow.setVisible(true);
		} else {
			add(new EventKpiTable("eventKpiContent", orgList));
			arrow.setVisible(false);
		}
	}
	
	private List<BaseOrg> getOrgList() {
		EventKPIWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();

		if(config == null) {
			return new ArrayList<BaseOrg>();
		} else {
			return config.getOrgs();
		}
	}

	@Override
	protected Component createConfigPanel(String id) {
		EventKPIWidgetConfiguration configCopy = getWidgetDefinition().getObject().getConfig().copy();
		IModel<EventKPIWidgetConfiguration> configModel = new Model<EventKPIWidgetConfiguration>(configCopy);
		return new EventKPIConfigPanel(id, configModel);        
	}
	
}
