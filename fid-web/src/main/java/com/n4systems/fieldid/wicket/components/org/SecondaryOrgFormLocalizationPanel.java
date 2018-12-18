package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.wicket.components.timezone.TimeZoneSelectorPanel;
import com.n4systems.model.orgs.SecondaryOrg;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class SecondaryOrgFormLocalizationPanel extends Panel {
    public SecondaryOrgFormLocalizationPanel(String id, IModel<SecondaryOrg> secondaryOrg) {
        super(id, secondaryOrg);
        add(new TimeZoneSelectorPanel("timeZoneContainer", new PropertyModel<String>(secondaryOrg, "defaultTimeZone")));
    }
}
