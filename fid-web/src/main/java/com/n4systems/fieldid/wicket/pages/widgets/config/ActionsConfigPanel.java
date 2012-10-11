package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.model.dashboard.widget.ActionsWidgetConfiguration;
import org.apache.wicket.model.IModel;

public class ActionsConfigPanel extends OrgDateWidgetConfigPanel<ActionsWidgetConfiguration> {

    public ActionsConfigPanel(String id, final IModel<ActionsWidgetConfiguration> configModel) {
        super(id, configModel);
    }

}

