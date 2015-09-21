package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.UpcomingLotoWidgetConfiguration;
import com.n4systems.util.chart.RangeType;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class UpcomingLotoConfigPanel extends OrgDateWidgetConfigPanel<UpcomingLotoWidgetConfiguration> {

    public UpcomingLotoConfigPanel(String id, final IModel<UpcomingLotoWidgetConfiguration> configModel, IModel<WidgetDefinition<UpcomingLotoWidgetConfiguration>> def) {
        super(id, configModel, def);
    }

    @Override
    protected DropDownChoice<RangeType> createDateRangeSelect() {
    	return null;
    }

    @Override
    protected Component createOrgPicker(String id, IModel<UpcomingLotoWidgetConfiguration> configModel) {
        return null;
    }
}

