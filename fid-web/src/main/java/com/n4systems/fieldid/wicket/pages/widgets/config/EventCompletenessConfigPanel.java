package com.n4systems.fieldid.wicket.pages.widgets.config;

import org.apache.wicket.model.IModel;

import com.n4systems.model.dashboard.widget.EventCompletenessWidgetConfiguration;

@SuppressWarnings("serial")
public class EventCompletenessConfigPanel extends OrgDateWidgetConfigPanel<EventCompletenessWidgetConfiguration> {

    public EventCompletenessConfigPanel(String id, final IModel<EventCompletenessWidgetConfiguration> configModel) {
        super(id, configModel);
    }

}

