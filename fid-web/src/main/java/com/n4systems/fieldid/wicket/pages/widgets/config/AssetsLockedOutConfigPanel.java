package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.LockedoutProceduresWidgetConfiguration;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2014-05-19.
 */
public class AssetsLockedOutConfigPanel extends OrgDateWidgetConfigPanel<LockedoutProceduresWidgetConfiguration> {

    public AssetsLockedOutConfigPanel(String id, final IModel<LockedoutProceduresWidgetConfiguration> configModel, IModel<WidgetDefinition<LockedoutProceduresWidgetConfiguration>> def) {
        super(id, configModel, false, def);
    }

}
