package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.AssetsIdentifiedWidgetConfiguration;
import org.apache.wicket.model.IModel;

public class AssetsIdentifiedConfigPanel extends OrgDateWidgetConfigPanel<AssetsIdentifiedWidgetConfiguration> {

    public AssetsIdentifiedConfigPanel(String id, final IModel<AssetsIdentifiedWidgetConfiguration> configModel, IModel<WidgetDefinition<AssetsIdentifiedWidgetConfiguration>> def) {
        super(id, configModel, def);
    }

}

