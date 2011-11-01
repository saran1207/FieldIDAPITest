package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.n4systems.fieldid.wicket.components.dashboard.subcomponents.EventKpiTable;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.widgets.config.EventKPIConfigPanel;
import com.n4systems.fieldid.wicket.pages.widgets.config.WidgetConfigPanel;
import com.n4systems.fieldid.wicket.util.AjaxCallback;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.EventKPIWidgetConfiguration;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;

public class EventKpiWidget extends Widget {
	
	public EventKpiWidget(String id, WidgetDefinition widgetDefinition) {
        super(id, new Model<WidgetDefinition>(widgetDefinition));
		setOutputMarkupId(true);
		
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
		EventKPIWidgetConfiguration config = (EventKPIWidgetConfiguration) getWidgetDefinition().getObject().getConfig();

		if(config == null) {
			return new ArrayList<BaseOrg>();
		}else {
			return config.getOrgs();
		}
	}
	
    @Override
    public <T extends WidgetConfiguration> WidgetConfigPanel createConfigurationPanel(String id, IModel<T> config, AjaxCallback<Boolean> saveCallback) {
        return new EventKPIConfigPanel(id, (IModel<EventKPIWidgetConfiguration>)config, saveCallback);
    }

}
