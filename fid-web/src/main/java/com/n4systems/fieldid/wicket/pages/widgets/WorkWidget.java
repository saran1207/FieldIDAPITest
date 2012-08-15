package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.components.Agenda;
import com.n4systems.fieldid.wicket.pages.widgets.config.WorkConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WorkWidgetConfiguration;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class WorkWidget extends Widget<WorkWidgetConfiguration> {

    public WorkWidget(String id, WidgetDefinition<WorkWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<WorkWidgetConfiguration>>(widgetDefinition));
        add(new Agenda("agenda"));
	}

    @Override
    public void renderHead(IHeaderResponse response) {

    }

	@Override
    public Component createConfigPanel(String id) {
		IModel<WorkWidgetConfiguration> configModel = new Model<WorkWidgetConfiguration>(getWidgetDefinition().getObject().getConfig());
		return new WorkConfigPanel(id, configModel);
	}

}
