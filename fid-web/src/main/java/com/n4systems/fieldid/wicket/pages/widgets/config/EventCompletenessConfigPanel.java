package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.EventCompletenessWidgetConfiguration;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class EventCompletenessConfigPanel extends OrgDateWidgetConfigPanel<EventCompletenessWidgetConfiguration> {

    public EventCompletenessConfigPanel(String id, final IModel<EventCompletenessWidgetConfiguration> configModel, IModel<WidgetDefinition<EventCompletenessWidgetConfiguration>> def) {
        super(id, configModel, def);
    }

}

