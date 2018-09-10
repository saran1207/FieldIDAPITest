package com.n4systems.fieldid.sso;

import com.n4systems.fieldid.actions.SignIn;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.SessionUserSecurityGuard;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.mixpanel.MixpanelService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.utils.CookieFactory;
import com.n4systems.fieldid.utils.UrlArchive;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.services.config.ConfigService;
import com.n4systems.sso.dao.SsoMetadataDao;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.context.SAMLContextProvider;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import rfid.web.helper.SessionEulaAcceptance;
import rfid.web.helper.SessionUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * Created by agrabovskis on 2018-08-08.
 */
public class SamlLogonResultHandler implements AuthenticationFailureHandler, AuthenticationSuccessHandler {

    private static final Logger logger = Logger.getLogger(SamlLogonResultHandler.class);

    @Autowired
    private SAMLContextProvider contextProvider;

    @Autowired
    private SsoMetadataDao ssoMetadataDao;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private MixpanelService mixpanelService;

    //@Autowired
    //private PersistenceManager persistenceManager;

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private UserService userService;

    private WebSessionMap webSessionMap;
    private LoaderFactory loaderFactory;
    private SignIn signIn = new SignIn();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("authentication success " + authentication.toString());
        SamlUserDetails details = (SamlUserDetails) authentication.getDetails();
        HttpSession session = request.getSession();
        //loginToFieldId(request, response, details.getUserIdInFieldId());
        //synchronized (request.getSession()) {
            /*session.setAttribute(ApplicationSession.KEY_SESSION_USER, new SessionUser(details.getUserId(), details.getEmailAddress(),
                    tenantService.getTenant(metadataServices.getSp(details.getSpEntityId()).getTenantId() ).getName()));*/
        //}
        if (!details.isAuthenticated()) {
            onAuthenticationFailure(request, response, new SessionAuthenticationException("User not authenticated in FieldId"));
        }
        else {
            synchronized (request.getSession()) {
                loginToFieldId(request, response, details);
            }
            //User user = entityManager.find(User.class, details.getUserIdInFieldId());
            //String newUrl = formatLoginURL(user);
            //System.out.println("Redirected url: " + newUrl);
            redirectToHomepage(request, response);
            //response.sendRedirect(newUrl);
        }
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.error("SSO authentication failure ", exception);
        redirectToHomepage(request, response);
    }

    private void redirectToHomepage(HttpServletRequest request, HttpServletResponse response) {
        try {
            SAMLMessageContext context = contextProvider.getLocalEntity(request, response);
            response.sendRedirect(ssoMetadataDao.getSp(context.getLocalEntityId()).getEntityBaseURL() + "/w/dashboard");
        } catch (Exception ex) {
            System.out.println("SAMLLogonFailureHandler failed getting context");
            throw new RuntimeException(ex);
        }
    }

    /*private String formatLoginURL(User user) {
        String systemProtocol = configService.getString(ConfigEntry.SYSTEM_PROTOCOL);
        String systemDomain = configService.getString(ConfigEntry.SYSTEM_DOMAIN);

        String url = String.format("%s://%s.%s/fieldid/login.action?userName=%s", systemProtocol, user.getTenant().getName(), systemDomain, user.getUserID());
        return url;
    }

/*
    /* Copied from LoginAction */
    private void loginToFieldId(HttpServletRequest request, HttpServletResponse response, SamlUserDetails userDetails) {
        /*try {
            User user = userService.findUserByIdValue(loginUserId);
        }
        catch(Exception ex) {
            System.out.println("fetch user threw " + ex);
        }*/
        System.out.println("finding user");
        User user = userService.findUserByUserID(userDetails.getTenantName(), userDetails.getUserId());
        System.out.println("found user " + user.getUserID());

        //WebSessionMap currentSession = getSession(request);
        //currentSession.setUserId(loginUserId);
        System.out.println("setting userid in websessionmap");
        request.getSession().setAttribute(WebSessionMap.USER_ID, userDetails.getUserIdInFieldId());
        System.out.println("userid set in websessionmap");

        System.out.println("Getting user to set into session user");
        User user2 = persistenceService.find(new QueryBuilder<User>(User.class, new OpenSecurityFilter()).addSimpleWhere("id", userDetails.getUserIdInFieldId()).addPostFetchPaths("permissions", "owner.primaryOrg.id"));
        System.out.println("Got user " + user2.getUserID());

        //User user = userService.findUserByIdValue(loginUserId);

        System.out.println("fetchpreviousurl");
        fetchPerviousUrl(request);
        System.out.println("clearSession");
        clearSession(request);
        System.out.println("loadsessionuser");
        loadSessionUser(request, userDetails.getUserIdInFieldId());
        System.out.println("loadeulainfo");
        //try {
        //    loadEULAInformation(request);
       // }
       // catch(Exception ex) {
        //    System.out.println("load eula thew " + ex);
       // }
        System.out.println("rememberme");
        rememberMe(request, response);
        System.out.println("record login on mix");
        //recordLoginOnMixPanel(user);
        System.out.println("update last login date");
        //updateLastLoginDate(user);

        logger.info("Login via SSO: " + user.getUserID() + " of " + user.getTenant().getName());

    }

    public WebSessionMap getSession(HttpServletRequest request) {
        if (webSessionMap != null)
            return webSessionMap;
        else {
            webSessionMap = new WebSessionMap(request.getSession(false));
            return webSessionMap;
        }
    }

    private SessionUser getSessionUser() {
        return FieldIDSession.get().getSessionUser();
    }

    private void fetchPerviousUrl(HttpServletRequest request) {
        UrlArchive urlArchive = new UrlArchive("preLoginContext", request, request.getSession());
        String previousUrl = urlArchive.fetchUrl();
        urlArchive.clearUrl();
    }

    private void clearSession(HttpServletRequest request) {
        WebSessionMap session = getSession(request);

        // The following items need to be preserved when clearing the session
        SystemSecurityGuard securityGuard = getSecurityGuard(request);
        boolean adminAuthenticated = session.isAdminAuthenticated();

        session.clear();

        // restore items
        session.setSecurityGuard(securityGuard);
        session.setAdminAuthenticated(adminAuthenticated);
    }

    private SystemSecurityGuard getSecurityGuard(HttpServletRequest request) {
        return getSession(request).getSecurityGuard();
    }

    protected void loadSessionUser(HttpServletRequest request, Long userId) {
        User user = persistenceService.find(new QueryBuilder<User>(User.class, new OpenSecurityFilter()).addSimpleWhere("id", userId).addPostFetchPaths("permissions", "owner.primaryOrg.id"));
        //User user = persistenceManager.find(new QueryBuilder<User>(User.class, new OpenSecurityFilter()).addSimpleWhere("id", userId).addPostFetchPaths("permissions", "owner.primaryOrg.id"));
        setupSessionUser(request, user);
    }

    private void setupSessionUser(HttpServletRequest request, User user) {
        getSession(request).setSessionUser(new SessionUser(user));
        getSession(request).setUserSecurityGuard(new SessionUserSecurityGuard(user));
        getSession(request).setVisibleUsers(userGroupService.findUsersVisibleTo(user));
        //new AbstractActionTenantContextInitializer(this).refreshSecurityGaurd();
    }

    private void loadEULAInformation(HttpServletRequest request) {
        getSession(request).setEulaAcceptance(new SessionEulaAcceptance(getLoaderFactory().createCurrentEulaLoader(), getLoaderFactory().createLatestEulaAcceptanceLoader()));
    }

    private LoaderFactory getLoaderFactory() {
        if (loaderFactory == null) {
            loaderFactory = new LoaderFactory(getSecurityFilter());
        }
        return loaderFactory;
    }

    private SecurityFilter getSecurityFilter() {
        return getSessionUser().getSecurityFilter();
    }

    private void rememberMe(HttpServletRequest request, HttpServletResponse response) {
        signIn.storeRememberMe(createCookieFactory(request, response));
    }

    private CookieFactory createCookieFactory(HttpServletRequest request, HttpServletResponse response) {
        return new CookieFactory(request, response);
    }

    private void recordLoginOnMixPanel(User user) {
        mixpanelService.sendEvent(MixpanelService.LOGGED_IN, user);
    }

    private void updateLastLoginDate(User loginUser) {
        loginUser.setLastLogin(new Date());
        //userManager.updateUser(loginUser);
        //entityManager.merge(loginUser);
    }
}
