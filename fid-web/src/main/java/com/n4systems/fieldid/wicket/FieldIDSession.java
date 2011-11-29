package com.n4systems.fieldid.wicket;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import com.n4systems.fieldid.viewhelpers.SearchContainer;
import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebSession;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.fieldid.permissions.SessionUserSecurityGuard;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.permissions.UserSecurityGuard;
import com.n4systems.fieldid.utils.FlashScopeMarshaller;
import com.n4systems.fieldidadmin.utils.Constants;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;

@SuppressWarnings({ "unchecked", "serial" })
public class FieldIDSession extends WebSession {

    private HttpSession session;
    private ConcurrentHashMap<String, String> localizationCache = new ConcurrentHashMap<String,String>();

    public FieldIDSession(Request request) {
        super(request);
        this.session = ((WebRequest)request).getHttpServletRequest().getSession();
    }
  
    @Deprecated // for testing only. 
    public FieldIDSession(Request request, User user) { 
    	this(request);
    	session.setAttribute(WebSessionMap.KEY_SESSION_USER, new SessionUser(user));
    	session.setAttribute(WebSessionMap.KEY_USER_SECURITY_GUARD, new SessionUserSecurityGuard(user));
    	session.setAttribute(WebSessionMap.KEY_SECURITY_GUARD, new SerializableSecurityGuard(TenantBuilder.n4(), PrimaryOrgBuilder.aPrimaryOrg().build()));
    }
    
    @Deprecated // for testing only.
    public void setUser(User user) { 
    	session.setAttribute(WebSessionMap.KEY_SESSION_USER, new SessionUser(user));
    }

	public static FieldIDSession get() {
        return (FieldIDSession) Session.get();
    }

    public UserSecurityGuard getUserSecurityGuard() {
        return (UserSecurityGuard) session.getAttribute(WebSessionMap.KEY_USER_SECURITY_GUARD);
    }

    public SystemSecurityGuard getSecurityGuard() {
        return (SystemSecurityGuard) session.getAttribute(WebSessionMap.KEY_SECURITY_GUARD);
    }

    public SessionUser getSessionUser() {
        return (SessionUser) session.getAttribute(WebSessionMap.KEY_SESSION_USER);
    }

    public Map<String, String> getTenantLangOverrides() {
        return (Map<String, String>) session.getAttribute(WebSessionMap.KEY_TENANT_LANG_OVERRIDES);
    }

    public void storeInfoMessageForStruts(String message) {
        session.setAttribute(FlashScopeMarshaller.FLASH_MESSAGES, Arrays.asList(message));
    }

    public void storeErrorMessageForStruts(String message) {
        session.setAttribute(FlashScopeMarshaller.FLASH_ERRORS, Arrays.asList(message));
    }

	public Collection<String> getFlashMessages() {
        return (Collection<String>) session.getAttribute(FlashScopeMarshaller.FLASH_MESSAGES);
    }

    public Collection<String> getFlashErrors() {
        return (Collection<String>) session.getAttribute(FlashScopeMarshaller.FLASH_ERRORS);
    }

    public SearchContainer getSearchContainer(String key) {
        return (SearchContainer) session.getAttribute(key);
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
