package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.utils.SessionUserInUse;
import com.n4systems.fieldid.utils.UrlArchive;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.activesession.ActiveSessionLoader;
import com.n4systems.model.activesession.ActiveSessionSaver;
import com.n4systems.model.user.User;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.time.SystemClock;
import org.apache.wicket.RestartResponseException;
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
        verifyNonConcurrentSession();
    }

    public FieldIDAuthenticatedPage() {
        verifyLoggedIn();
        verifyNonConcurrentSession();
    }

    private void verifyLoggedIn() {
        SessionUser sessionUser = getSessionUser();

        if (sessionUser == null) {
            new UrlArchive("preLoginContext", getServletRequest(), getServletRequest().getSession()).storeUrl();
            throw new RedirectToUrlException("/login.action");
        }
    }

    private void verifyNonConcurrentSession() {
		FieldIDSession fieldidSession = FieldIDSession.get();

		SessionUser sessionUser = fieldidSession.getSessionUser();
		String sessionId = fieldidSession.getId();

		SessionUserInUse sessionUserInUse = new SessionUserInUse(new ActiveSessionLoader(), ConfigContext.getCurrentContext(), new SystemClock(), new ActiveSessionSaver());

		if (sessionUser != null && !sessionUserInUse.doesActiveSessionBelongTo(sessionUser.getUniqueID(), sessionId)) {
			throw new RestartResponseException(SessionBootedPage.class);
		}
    }

    protected User getCurrentUser() {
        return persistenceService.find(User.class, getSessionUser().getUniqueID());
    }

}
