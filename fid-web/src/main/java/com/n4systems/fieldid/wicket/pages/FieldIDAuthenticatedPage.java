package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.utils.UrlArchive;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RedirectToUrlException;
import rfid.web.helper.SessionUser;

public class FieldIDAuthenticatedPage extends FieldIDWicketPage {

    public FieldIDAuthenticatedPage(PageParameters params) {
        super(params);
        verifyLoggedIn();
    }

    public FieldIDAuthenticatedPage() {
        verifyLoggedIn();
    }

    private void verifyLoggedIn() {
        SessionUser sessionUser = getSessionUser();

        if (sessionUser == null) {
            new UrlArchive("preLoginContext", getServletRequest(), getServletRequest().getSession()).storeUrl();
            throw new RedirectToUrlException("/login.action");
        }
    }

}
