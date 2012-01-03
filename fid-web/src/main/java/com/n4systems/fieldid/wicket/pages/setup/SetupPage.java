package com.n4systems.fieldid.wicket.pages.setup;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.*;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.request.resource.CssPackageResource;
import org.apache.wicket.markup.html.basic.Label;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;

public abstract class SetupPage extends FieldIDFrontEndPage {

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

    protected boolean hasManageSystemConfig() {
        return FieldIDSession.get().getSessionUser().hasAccess("managesystemconfig");
    }

    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/pageStyles/settings.css");
    }

}
