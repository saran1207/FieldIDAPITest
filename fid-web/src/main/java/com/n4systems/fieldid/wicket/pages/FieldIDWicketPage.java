package com.n4systems.fieldid.wicket.pages;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.permissions.UserSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.security.SecurityFilter;

public class FieldIDWicketPage extends WebPage {

    public FieldIDWicketPage(PageParameters params) {
        super(params);
    }

    public FieldIDWicketPage() {
        this(null);
    }

    protected void addComponents() {}
    
    @Override
	protected void onBeforeRender() {
		if (!hasBeenRendered()) {
			addComponents();
		}
        super.onBeforeRender();
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
        return getWebRequestCycle().getWebRequest().getHttpServletRequest();
    }
    
}
