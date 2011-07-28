package com.n4systems.fieldid.wicket.pages.setup;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class SettingsPage extends SetupPage {

    public SettingsPage() {
        add(new BookmarkablePageLink<Void>("systemSettingsLink", SystemSettingsPage.class));
    }

}
