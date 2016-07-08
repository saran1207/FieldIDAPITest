package com.n4systems.fieldid.wicket.pages.setup.security;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.*;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * Created by dlai on 23/06/16.
 */
public class AbstractSecurityPage extends FieldIDTemplatePage {

    protected AbstractSecurityPage(PageParameters params) {
        super(params);
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
                aNavItem().label("nav.security").page(this.getClass()).cond(hasManageSystemConfig()).build()));
    }

    protected boolean hasManageSystemConfig() {
        return FieldIDSession.get().getSessionUser().hasAccess("managesystemconfig");
    }

    @Override
    protected boolean forceDefaultLanguage() {
        return true;
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, SecurityPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }
}
