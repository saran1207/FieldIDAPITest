package com.n4systems.fieldid.wicket.pages.widgets.config;

import org.apache.wicket.model.IModel;

import com.n4systems.model.dashboard.widget.AssetsStatusWidgetConfiguration;

@SuppressWarnings("serial")
public class AssetsStatusConfigPanel extends OrgDateWidgetConfigPanel<AssetsStatusWidgetConfiguration> {

    public AssetsStatusConfigPanel(String id, final IModel<AssetsStatusWidgetConfiguration> configModel) {
        super(id, configModel);
    }

}

