package com.n4systems.fieldid.wicket.pages.setup;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.basic.Label;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;

public abstract class SetupPage extends FieldIDLoggedInPage {

    protected SetupPage(PageParameters params) {
        super(params);
        add(CSSPackageResource.getHeaderContribution("style/pageStyles/settings.css"));
    }

    protected SetupPage() {
        this(new PageParameters());
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.setup"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.settings").page(SettingsPage.class).cond(hasManageSystemConfig()).build(),
                aNavItem().label("nav.owners_users_loc").page(OwnersUsersLocationsPage.class).build(),
                aNavItem().label("nav.assets_and_events").page(AssetsAndEventsPage.class).cond(hasManageSystemConfig()).build(),
                aNavItem().label("nav.import").page(ImportPage.class).cond(hasManageSystemConfig()).build(),
                aNavItem().label("nav.templates").page(TemplatesPage.class).cond(hasManageSystemConfig()).build(),
                aNavItem().label("nav.widgets").page(WidgetsPage.class).cond(hasManageSystemConfig()).build(),
                aNavItem().label("nav.security").page(SecurityPage.class).cond(hasManageSystemConfig()).build(),
                aNavItem().label("nav.data_log").page(DataLogPage.class).cond(hasManageSystemConfig()).build()));
    }

    protected boolean hasManageSystemConfig() {
        return FieldIDSession.get().getSessionUser().hasAccess("managesystemconfig");
    }

}
