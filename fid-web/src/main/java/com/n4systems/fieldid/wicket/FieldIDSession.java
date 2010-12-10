package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.permissions.UserSecurityGuard;
import com.n4systems.fieldid.utils.FlashScopeMarshaller;
import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebSession;
import rfid.web.helper.SessionUser;

import java.util.Arrays;

import javax.servlet.http.HttpSession;

public class FieldIDSession extends WebSession {

    private HttpSession session;

    public FieldIDSession(Request request) {
        super(request);
        this.session = ((WebRequest)request).getHttpServletRequest().getSession();
    }

    public static FieldIDSession get() {
        return (FieldIDSession) Session.get();
    }

    public UserSecurityGuard getUserSecurityGuard() {
        return (UserSecurityGuard) session.getAttribute(com.n4systems.fieldid.actions.utils.WebSession.KEY_USER_SECURITY_GUARD);
    }

    public SystemSecurityGuard getSecurityGuard() {
        return (SystemSecurityGuard) session.getAttribute(com.n4systems.fieldid.actions.utils.WebSession.KEY_SECURITY_GUARD);
    }

    public SessionUser getSessionUser() {
        return (SessionUser) session.getAttribute(com.n4systems.fieldid.actions.utils.WebSession.KEY_SESSION_USER);
    }

    public void storeInfoMessageForStruts(String message) {
        session.setAttribute(FlashScopeMarshaller.FLASH_MESSAGES, Arrays.asList(message));
    }

}
