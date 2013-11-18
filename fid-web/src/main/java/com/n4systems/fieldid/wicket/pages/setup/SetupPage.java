package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public abstract class SetupPage extends FieldIDTemplatePage {

    protected SetupPage(PageParameters params) {
        super(params);
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
                aNavItem().label("nav.security").page(SecurityPage.class).cond(hasManageSystemConfig()).build()));
    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        add(new BreadCrumbBar(breadCrumbBarId,
                aNavItem().label("label.dashboard").page(DashboardPage.class).build(),
                aNavItem().label("label.setup").page(this.getClass()).build()
        ));
    }

    protected boolean hasManageSystemConfig() {
        return FieldIDSession.get().getSessionUser().hasAccess("managesystemconfig");
    }

    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/pageStyles/settings.css");
    }


    @Override
    protected boolean forceDefaultLanguage() {
        return true;
    }
}
