package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public abstract class SetupPage extends FieldIDLoggedInPage {

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("Settings").page(SettingsPage.class).cond(hasManageSystemConfig()).build(),
                aNavItem().label("Owners, Users & Locations").page(OwnersUsersLocationsPage.class).build(),
                aNavItem().label("Assets & Events").page(AssetsAndEventsPage.class).cond(hasManageSystemConfig()).build(),
                aNavItem().label("Import").page("/import.action").cond(hasManageSystemConfig()).build(),
                aNavItem().label("Templates").page(TemplatesPage.class).cond(hasManageSystemConfig()).build(),
                aNavItem().label("Widgets").page("/widgets.action").cond(hasManageSystemConfig()).build(),
                aNavItem().label("Data Log").page("/dataLog.action").cond(hasManageSystemConfig()).build()));
    }

    protected boolean hasManageSystemConfig() {
        return FieldIDSession.get().getSessionUser().hasAccess("managesystemconfig");
    }

}
