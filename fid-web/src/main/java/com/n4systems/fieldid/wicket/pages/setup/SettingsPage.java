package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.pages.org.BrandingPage;
import com.n4systems.fieldid.wicket.pages.org.OrgsListPage;
import com.n4systems.fieldid.wicket.pages.setup.sso.SsoSettingsPage;
import com.n4systems.model.ExtendedFeature;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

public class SettingsPage extends SetupPage {

    public SettingsPage() {
        add(new BookmarkablePageLink<Void>("brandingLink", BrandingPage.class));
        add(new BookmarkablePageLink<Void>("systemSettingsLink", SystemSettingsPage.class));
        add(new BookmarkablePageLink<Void>("yourPlanLink", YourPlanPage.class));
        add(new BookmarkablePageLink<Void>("organizationsLink", OrgsListPage.class));
        Link ssoSettingsPageLink = new BookmarkablePageLink<Void>("ssoSettingsLink", SsoSettingsPage.class);
        add(ssoSettingsPageLink);
        ssoSettingsPageLink.setVisible(FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.SSO));
    }

}
