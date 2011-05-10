package com.n4systems.fieldid.wicket.pages.setup;

import org.apache.wicket.markup.html.WebMarkupContainer;

public class OwnersUsersLocationsPage extends SetupPage {

    public OwnersUsersLocationsPage() {
        boolean canManageSystemUsers = getSessionUser().hasAccess("managesystemusers");
        add(new WebMarkupContainer("manageCustomersContainer").setVisible(getSessionUser().hasAccess("manageendusers")));
        add(new WebMarkupContainer("manageUsersContainer").setVisible(canManageSystemUsers));
        add(new WebMarkupContainer("manageUserRegistrationsContainer").setVisible(canManageSystemUsers && getSecurityGuard().isReadOnlyUserEnabled()));
        add(new WebMarkupContainer("managePredefinedLocationsContainer").setVisible(getSessionUser().hasAccess("managesystemconfig")));
    }

}

