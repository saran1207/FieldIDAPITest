package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.utils.UrlArchive;
import com.n4systems.model.user.User;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

// Subclasses of this page can be full endpoints, but they won't have the outside
// layout for navigation. This is useful for lightbox rendered items.
public class FieldIDAuthenticatedPage extends FieldIDWicketPage {

    @SpringBean
    private PersistenceService persistenceService;

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

    protected User getCurrentUser() {
        return persistenceService.find(User.class, getSessionUser().getUniqueID());
    }

}
