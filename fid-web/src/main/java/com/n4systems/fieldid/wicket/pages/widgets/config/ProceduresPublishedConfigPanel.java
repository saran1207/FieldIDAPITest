package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.model.dashboard.widget.ProceduresPublishedWidgetConfiguration;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2014-04-22.
 */
public class ProceduresPublishedConfigPanel extends OrgDateWidgetConfigPanel<ProceduresPublishedWidgetConfiguration> {

    public ProceduresPublishedConfigPanel(String id, final IModel<ProceduresPublishedWidgetConfiguration> configModel) {
        super(id, configModel, true);
    }

}
