package com.n4systems.fieldid.wicket.pages.widgets.config;

import org.apache.wicket.model.IModel;

import com.n4systems.model.dashboard.widget.CompletedEventsWidgetConfiguration;

@SuppressWarnings("serial")
public class CompletedEventsConfigPanel extends OrgDateWidgetConfigPanel<CompletedEventsWidgetConfiguration> {

    public CompletedEventsConfigPanel(String id, final IModel<CompletedEventsWidgetConfiguration> configModel) {
        super(id, configModel);
    }

}

