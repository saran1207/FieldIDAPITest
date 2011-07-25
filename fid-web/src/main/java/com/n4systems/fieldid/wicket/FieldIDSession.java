package com.n4systems.fieldid.wicket;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebSession;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.permissions.UserSecurityGuard;
import com.n4systems.fieldid.utils.FlashScopeMarshaller;
import com.n4systems.fieldidadmin.utils.Constants;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;

public class FieldIDSession extends WebSession {

    private HttpSession session;
    private ConcurrentHashMap<String, String> localizationCache = new ConcurrentHashMap<String,String>();

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

    public Map<String, String> getTenantLangOverrides() {
        return (Map<String, String>) session.getAttribute(com.n4systems.fieldid.actions.utils.WebSession.KEY_TENANT_LANG_OVERRIDES);
    }

    public void storeInfoMessageForStruts(String message) {
        session.setAttribute(FlashScopeMarshaller.FLASH_MESSAGES, Arrays.asList(message));
    }

    public Collection<String> getFlashMessages() {
        return (Collection<String>) session.getAttribute(FlashScopeMarshaller.FLASH_MESSAGES);
    }

    public Collection<String> getFlashErrors() {
        return (Collection<String>) session.getAttribute(FlashScopeMarshaller.FLASH_ERRORS);
    }

    public Tenant getTenant() {
        return getSecurityGuard().getTenant();
    }

    public PrimaryOrg getPrimaryOrg() {
        return getSecurityGuard().getPrimaryOrg();
    }

    public void clearCache() {
        localizationCache.clear();
    }

    public Map<String, String> getCache() {
        return localizationCache;
    }
    
    public boolean isAdminConsoleAuthenticated() {
    	return (session.getAttribute(Constants.SESSION_USER) != null);
    }
}
