package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.location.LocationPicker;
import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.fieldid.wicket.components.user.GroupedUserPicker;
import com.n4systems.fieldid.wicket.model.user.GroupedUsersForTenantModel;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

public class OwnershipCriteriaPanel extends Panel {

    public OwnershipCriteriaPanel(String id, IModel<?> model) {
        super(id, model);

        add(new OrgPicker("owner", new PropertyModel<BaseOrg>(getDefaultModel(), "owner")));
        // because location picker is in a container that affects "relative" css positioning we must adjust offsets.
        //  temporary - need to customize LocationPicker and/or javascript so it can handle this situation. 
        //  (trying to minimize impact on shared components...do after consolidation of pages).
        add(new LocationPicker("location", new PropertyModel<Location>(getDefaultModel(), "location")).withOffset(-290, -275));

        WebMarkupContainer assignedUserContainer = new WebMarkupContainer("assignedToContainer");
        GroupedUserPicker groupedUserPicker = new GroupedUserPicker("assignedTo", new PropertyModel<User>(getDefaultModel(), "assignedTo"), new GroupedUsersForTenantModel());
        groupedUserPicker.setNullValid(true);
        assignedUserContainer.add(groupedUserPicker);
        assignedUserContainer.setVisible(FieldIDSession.get().getSecurityGuard().isAssignedToEnabled());
        
        add(assignedUserContainer);
    }

}
