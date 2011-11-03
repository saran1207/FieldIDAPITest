package com.n4systems.fieldid.wicket.pages.widgets.config;

import org.apache.wicket.model.IModel;

import com.n4systems.model.dashboard.widget.UpcomingEventsWidgetConfiguration;

@SuppressWarnings("serial")
public class UpcomingEventsConfigPanel extends OrgDateWidgetConfigPanel<UpcomingEventsWidgetConfiguration> {

    public UpcomingEventsConfigPanel(String id, final IModel<UpcomingEventsWidgetConfiguration> configModel) {
        super(id, configModel);
        dateRangeSelect.setVisible(false);
    }

}

