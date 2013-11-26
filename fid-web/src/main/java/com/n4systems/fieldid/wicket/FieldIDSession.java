package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.fieldid.permissions.SessionUserSecurityGuard;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.permissions.UserSecurityGuard;
import com.n4systems.fieldid.utils.FlashScopeMarshaller;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.Tenant;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import rfid.web.helper.SessionUser;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({ "unchecked", "serial" })
public class FieldIDSession extends WebSession {

    private transient HttpSession session;
    private ConcurrentHashMap<String, String> localizationCache = new ConcurrentHashMap<String,String>();
    private ConcurrentHashMap<Long, List<Event>> actionsList = new ConcurrentHashMap<Long, List<Event>>();
    private String previouslyStoredTempFileId;
    private boolean concurrentSessionDetected;
    private CriteriaResult previouslyStoredCriteriaResult;
    private ThingEvent previouslyStoredEventSchedule;
    private Locale userLocale;

    public FieldIDSession(Request request) {
        super(request);
        this.session = ((ServletWebRequest)request).getContainerRequest().getSession(true);
    }
    
    @Deprecated // for testing only. 
    public FieldIDSession(Request request, User user) { 
    	this(request);
    	getHttpSession().setAttribute(WebSessionMap.KEY_SESSION_USER, new SessionUser(user));
        getHttpSession().setAttribute(WebSessionMap.KEY_USER_SECURITY_GUARD, new SessionUserSecurityGuard(user));
        getHttpSession().setAttribute(WebSessionMap.KEY_SECURITY_GUARD, new SerializableSecurityGuard(TenantBuilder.n4(), PrimaryOrgBuilder.aPrimaryOrg().build()));
    }

    @Override
    public void detach() {
        session = null;
    }

    private HttpSession getHttpSession() {
        if (session == null) {
            session = ((ServletWebRequest)RequestCycle.get().getRequest()).getContainerRequest().getSession(true);
        }
        return session;
    }

    @Deprecated // for testing only.
    public void setUser(User user) {
        getHttpSession().setAttribute(WebSessionMap.KEY_SESSION_USER, new SessionUser(user));
    }

	public static FieldIDSession get() {
        return (FieldIDSession) Session.get();
    }

    public UserSecurityGuard getUserSecurityGuard() {
        return (UserSecurityGuard) getHttpSession().getAttribute(WebSessionMap.KEY_USER_SECURITY_GUARD);
    }

    public SystemSecurityGuard getSecurityGuard() {
        return (SystemSecurityGuard) getHttpSession().getAttribute(WebSessionMap.KEY_SECURITY_GUARD);
    }

    public SessionUser getSessionUser() {
        return (SessionUser) getHttpSession().getAttribute(WebSessionMap.KEY_SESSION_USER);
    }

    public Map<String, String> getTenantLangOverrides() {
        return (Map<String, String>) getHttpSession().getAttribute(WebSessionMap.KEY_TENANT_LANG_OVERRIDES);
    }
    
    public Long getVendorContext() {
        return (Long) getHttpSession().getAttribute(WebSessionMap.VENDOR_CONTEXT);
    }

    public void storeInfoMessageForStruts(String message) {
        getHttpSession().setAttribute(FlashScopeMarshaller.FLASH_MESSAGES, Arrays.asList(message));
    }

    public void storeErrorMessageForStruts(String message) {
        getHttpSession().setAttribute(FlashScopeMarshaller.FLASH_ERRORS, Arrays.asList(message));
    }

	public Collection<String> getFlashMessages() {
        return (Collection<String>) getHttpSession().getAttribute(FlashScopeMarshaller.FLASH_MESSAGES);
    }

    public Collection<String> getFlashErrors() {
        return (Collection<String>) getHttpSession().getAttribute(FlashScopeMarshaller.FLASH_ERRORS);
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
    	return (getHttpSession().getAttribute(WebSessionMap.ADMIN_AUTHENTICATED) != null);
    }

    public String getPreviouslyStoredTempFileId() {
        return previouslyStoredTempFileId;
    }

    public void setPreviouslyStoredTempFileId(String previouslyStoredTempFileId) {
        this.previouslyStoredTempFileId = previouslyStoredTempFileId;
    }

    public void setConcurrentSessionDetectedInRequestCycle() {
        this.concurrentSessionDetected = true;
    }

    public boolean isConcurrentSessionDetectedInRequestCycle() {
        return concurrentSessionDetected;
    }

    public void setActionsForCriteria(CriteriaResult criteriaResult, List<Event> actions) {
        actionsList.put(criteriaResult.getCriteria().getId(), actions);
    }

    public List<Event> getActionsList(CriteriaResult criteriaResult) {
        return actionsList.get(criteriaResult.getCriteria().getId());
    }

    // Previously Stored Stuff:
    // The issue is that when you have a page that does some transient editing in place before a final commit,
    // and some of that editing is done inside modal windows, there is difficulty editing transient state in a way
    // that remains visible to the underlying page, due to wicket giving us multiple page maps. These are used
    // to pass back data created or modified inside a modal window to the primary transient edit page. (Identify Asset, Perform Event).
    public CriteriaResult getPreviouslyStoredCriteriaResult() {
        return previouslyStoredCriteriaResult;
    }

    public void setPreviouslyStoredCriteriaResult(CriteriaResult previouslyStoredCriteriaResult) {
        this.previouslyStoredCriteriaResult = previouslyStoredCriteriaResult;
    }

    public ThingEvent getPreviouslyStoredEventSchedule() {
        return previouslyStoredEventSchedule;
    }

    public void setPreviouslyStoredEventSchedule(ThingEvent previouslyStoredEventSchedule) {
        this.previouslyStoredEventSchedule = previouslyStoredEventSchedule;
    }

    public Locale getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(Locale userLocale) {
        this.userLocale = userLocale;
    }
}
