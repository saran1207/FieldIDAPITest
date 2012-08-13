package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.components.Agenda;
import com.n4systems.fieldid.wicket.pages.widgets.config.WidgetConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class WorkWidget extends Widget<WidgetConfiguration> {

    public WorkWidget(String id, WidgetDefinition<WidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<WidgetConfiguration>>(widgetDefinition));

        add(new Agenda("agenda"));
	}

    @Override
    public void renderHead(IHeaderResponse response) {

    }

	@Override
    public Component createConfigPanel(String id) {
		IModel<WidgetConfiguration> configModel = new Model<WidgetConfiguration>(getWidgetDefinition().getObject().getConfig());		
		return new WidgetConfigPanel<WidgetConfiguration>(id, configModel);
	}

}
