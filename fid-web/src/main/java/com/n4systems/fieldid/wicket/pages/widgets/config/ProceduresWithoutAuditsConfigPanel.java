package com.n4systems.fieldid.wicket.pages.widgets.config;


import com.n4systems.model.dashboard.widget.LockedoutProceduresWidgetConfiguration;
import com.n4systems.model.dashboard.widget.ProceduresWithoutAuditsWidgetConfiguration;
import org.apache.wicket.model.IModel;

public class ProceduresWithoutAuditsConfigPanel extends OrgDateWidgetConfigPanel<ProceduresWithoutAuditsWidgetConfiguration> {

    public ProceduresWithoutAuditsConfigPanel(String id, final IModel<ProceduresWithoutAuditsWidgetConfiguration> configModel) {
        super(id, configModel, false);

   }
}
