package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.UpcomingProcedureAuditsWidgetConfiguration;
import com.n4systems.util.chart.RangeType;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class UpcomingProcedureAuditsConfigPanel extends OrgDateWidgetConfigPanel<UpcomingProcedureAuditsWidgetConfiguration>{
    public UpcomingProcedureAuditsConfigPanel(String id, final IModel<UpcomingProcedureAuditsWidgetConfiguration> configModel, IModel<WidgetDefinition<UpcomingProcedureAuditsWidgetConfiguration>> def) {
        super(id, configModel, def);
    }

    @Override
    protected DropDownChoice<RangeType> createDateRangeSelect() {
        return null;
    }
}
