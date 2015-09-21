package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.AssetsStatusWidgetConfiguration;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class AssetsStatusConfigPanel extends OrgDateWidgetConfigPanel<AssetsStatusWidgetConfiguration> {

    public AssetsStatusConfigPanel(String id, final IModel<AssetsStatusWidgetConfiguration> configModel, IModel<WidgetDefinition<AssetsStatusWidgetConfiguration>> def) {
        super(id, configModel, def);
    }

}

