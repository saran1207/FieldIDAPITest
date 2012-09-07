package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.utils.UrlArchive;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.user.User;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.IHeaderResponse;
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

    @Override
    protected void onInitialize() {
        super.onInitialize();
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
        // We detect concurrent sessions in the request cycle, but boot them out here.
        // Reason is that there's a bug in wicket that prevents you from setting a response page in the RequestCycleListener
        // We need to do it in the request cycle listener because ALL requests (including ajax requests that don't fire page constructors)
        // need to refresh the session timeout counter.
		if (FieldIDSession.get().isConcurrentSessionDetectedInRequestCycle()) {
			throw new RestartResponseException(SessionBootedPage.class);
		}
    }

    protected User getCurrentUser() {
        return persistenceService.find(User.class, getSessionUser().getUniqueID());
    }


    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

    }
}
