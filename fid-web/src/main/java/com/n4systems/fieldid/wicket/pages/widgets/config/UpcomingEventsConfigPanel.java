package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.UpcomingEventsWidgetConfiguration;
import com.n4systems.util.chart.RangeType;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class UpcomingEventsConfigPanel extends OrgDateWidgetConfigPanel<UpcomingEventsWidgetConfiguration> {

    public UpcomingEventsConfigPanel(String id, final IModel<UpcomingEventsWidgetConfiguration> configModel, IModel<WidgetDefinition<UpcomingEventsWidgetConfiguration>> def) {
        super(id, configModel, def);
    }

    @Override
    protected DropDownChoice<RangeType> createDateRangeSelect() {
    	return null;
    }    
}

