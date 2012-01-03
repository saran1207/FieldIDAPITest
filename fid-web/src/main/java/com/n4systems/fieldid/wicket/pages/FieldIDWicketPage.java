package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.permissions.UserSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.security.SecurityFilter;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import rfid.web.helper.SessionUser;

import javax.servlet.http.HttpServletRequest;

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

    protected SecurityFilter getSecurityFilter() {
        return getSessionUser().getSecurityFilter();
    }

    protected HttpServletRequest getServletRequest() {
        return ((ServletWebRequest)getRequest()).getContainerRequest();
    }
    
}
