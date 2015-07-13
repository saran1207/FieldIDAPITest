package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.CompletedEventsWidgetConfiguration;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class CompletedEventsConfigPanel extends OrgDateWidgetConfigPanel<CompletedEventsWidgetConfiguration> {

    public CompletedEventsConfigPanel(String id, final IModel<CompletedEventsWidgetConfiguration> configModel, IModel<WidgetDefinition<CompletedEventsWidgetConfiguration>> def) {
        super(id, configModel, def);
    }

}

