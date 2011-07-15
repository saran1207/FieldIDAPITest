package com.n4systems.fieldid.wicket.pages.setup;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import com.n4systems.fieldid.wicket.pages.setup.columnlayout.ColumnsLayoutPage;

public class SecurityPage extends SetupPage {

    public SecurityPage() {
        add(new BookmarkablePageLink<ColumnsLayoutPage>("passwordPolicyLink", PasswordPolicyPage.class));
        add(new BookmarkablePageLink<ColumnsLayoutPage>("accountPolicyLink", AccountPolicyPage.class));
    }

}
