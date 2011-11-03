package com.n4systems.fieldid.wicket.pages.widgets.config;

import org.apache.wicket.model.IModel;

import com.n4systems.model.dashboard.widget.AssetsIdentifiedWidgetConfiguration;

public class AssetsIdentifiedConfigPanel extends OrgDateWidgetConfigPanel<AssetsIdentifiedWidgetConfiguration> {

    public AssetsIdentifiedConfigPanel(String id, final IModel<AssetsIdentifiedWidgetConfiguration> configModel) {
        super(id, configModel);
    }

}

