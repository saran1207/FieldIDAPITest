package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.wicket.components.timezone.TimeZoneSelectorPanel;
import com.n4systems.model.orgs.InternalOrg;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class InternalOrgFormLocalizationPanel extends Panel {
    public InternalOrgFormLocalizationPanel(String id, IModel<InternalOrg> internalOrg) {
        super(id, internalOrg);
        add(new TimeZoneSelectorPanel("timeZoneContainer", new PropertyModel<String>(internalOrg, "defaultTimeZone")));
    }
}
