package com.n4systems.fieldid.wicket.pages.widgets.config;


import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.ProceduresWithoutAuditsWidgetConfiguration;
import org.apache.wicket.model.IModel;

public class ProceduresWithoutAuditsConfigPanel extends OrgDateWidgetConfigPanel<ProceduresWithoutAuditsWidgetConfiguration> {

    public ProceduresWithoutAuditsConfigPanel(String id, final IModel<ProceduresWithoutAuditsWidgetConfiguration> configModel, IModel<WidgetDefinition<ProceduresWithoutAuditsWidgetConfiguration>> def) {
        super(id, configModel, false, def);
   }
}
