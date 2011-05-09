package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.permissions.UserSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import rfid.web.helper.SessionUser;

public class FieldIDWicketPage extends WebPage {

    public FieldIDWicketPage(PageParameters params) {
        super(params);
    }

    public FieldIDWicketPage() {
        this(null);
    }

    protected UserSecurityGuard getUserSecurityGuard() {
        return FieldIDSession.get().getUserSecurityGuard();
    }

    protected SystemSecurityGuard getSecurityGuard() {
        return FieldIDSession.get().getSecurityGuard();
    }

    protected SessionUser getSessionUser() {
        return FieldIDSession.get().getSessionUser();
    }

}
