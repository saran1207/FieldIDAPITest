package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class HierarchyDisplayPanel extends Panel {

    public HierarchyDisplayPanel(String id, IModel<BaseOrg> org) {
        super(id);

        add(new FlatLabel("primaryOrgName", new PropertyModel<String>(org, "primaryOrg.name")));

        add(new FlatLabel("secondaryOrgName", new PropertyModel<String>(org, "secondaryOrg.name")));

        add(new FlatLabel("customerOrgName", new PropertyModel<String>(org, "customerOrg.name")));

        add(new FlatLabel("divisionOrgName", new PropertyModel<String>(org, "divisionOrg.name")));
    }

}
