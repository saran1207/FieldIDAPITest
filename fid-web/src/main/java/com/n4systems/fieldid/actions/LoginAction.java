package com.n4systems.fieldid.actions;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.LoginException;
import com.n4systems.exceptions.LoginFailureInfo;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.handler.password.PasswordHelper;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.service.admin.AdminUserService;
import com.n4systems.fieldid.service.mixpanel.MixpanelService;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.servlets.ConcurrentLoginSessionListener;
import com.n4systems.fieldid.utils.UrlArchive;
import com.n4systems.model.user.User;
import com.n4systems.util.ConfigEntry;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import rfid.web.helper.SessionEulaAcceptance;

import java.util.Date;

public class LoginAction extends AbstractAction {

	private static final Logger logger = Logger.getLogger(LoginAction.class);
	private static final long serialVersionUID = 1L;

	protected UserManager userManager;

	private String previousUrl;

	private SignIn signIn = new SignIn();

	private String resetPasswordKey;
	
	@Autowired
	private TenantSettingsService tenantSettingsService;

    @Autowired
    private MixpanelService mixpanelService;

	@Autowired
	private AdminUserService adminUserService;
	
	public LoginAction(UserManager userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
	}

	@SkipValidation
	public String doTest() {
		if (isLoggedIn()) {
			return "loggedIn";
		}
		return "noSession";
	}

	@SkipValidation
	public String doAdd() {
		if (isLoggedIn()) {
			return "alreadyLoggedIn";
		}

		checkRememberMe();
		return SUCCESS;
	}

	private void checkRememberMe() {
		signIn.populateFromRememberMe(createCookieFactory());
	}

	public String doCreate() {		
		User loginUser = null;

		if (!signIn.isValid(this)) {
			return INPUT;
		}

		try {
			loginUser = findUser();
		} catch (LoginException e) {
			handleFailedLoginAttempt(e);				
			return INPUT;
		}

		WebSessionMap currentSession = getSession();
		currentSession.setUserId(loginUser.getId());

		// we never check for concurrent sessions on sudo logins or system users
		if (!currentSession.isSudoAuth() && !loginUser.isSystem()) {
			WebSessionMap concurrentSession = getConcurrentSession(loginUser, currentSession);
			if (concurrentSession != null) {
				currentSession.setConcurrentSessionId(concurrentSession.getId());
				return "confirmKick";
			}
		}
		return signIn(loginUser);
	}

	private WebSessionMap getConcurrentSession(User loginUser, WebSessionMap currentSession) {
		WebSessionMap concurrentSession = null;
		for(WebSessionMap userSession: ConcurrentLoginSessionListener.getValidSessionsForUser(loginUser.getId())) {
			// a session is not concurrent if: it is the current session or was authenticated via sudo
			if (userSession.getId().equals(currentSession.getId()) || userSession.isSudoAuth()) {
				continue;
			} else {
				// we will stop after finding the first concurrent session as there can only ever be 1
				concurrentSession = userSession;
				break;
			}
		}
		return concurrentSession;
	}

	private User findUser() throws LoginException {
		return signIn.isNormalLogin() ? findUserByPw() : findUserByRfid();
	}

	private User findUserByRfid() {
		//Being uppercased since that's what happens on the user before we hash the rfid value. 
		return userManager.findUser(getSecurityGuard().getTenantName(), signIn.getSecureRfid().toUpperCase());
	}

	private User findUserByPw() throws LoginException {
		// this is pretty ugly and should be pushed back to the user manager once moved to struts
		try {
			// findUserByPw will never return a null user.  Any authentication failure will throw a LoginException
			return userManager.findUserByPw(getSecurityGuard().getTenantName(), signIn.getUserName(), signIn.getPassword());
		} catch (LoginException e) {
			User user = adminUserService.attemptSudoAuthentication(getSecurityGuard().getTenantName(), signIn.getUserName(), signIn.getPassword());

			// if admin auth is successful, return the user, otherwise re-throw the exception
			if (user != null) {
				getSession().setSudoAuth(true);
				return user;
			} else {
				throw e;
			}
		}
	}

	private String getFailedLoginText(LoginFailureInfo info) {
		String key;
		String[] args = {};

        if (info.isUnactivated()) {
            key = "error.unactivated_account";
        } else if (info.isLocked() || info.requiresLocking()) {
			key = info.getDuration()!=0 ? "error.accountlocked_duration" : "error.accountlocked_contact";
			args = new String[]{info.getDuration()+""};
		} else {		
			key = "error.loginfailure";
			args = new String[]{info.getAttempts()+"", info.getMaxAttempts()+""};
		}		
		return getText(key, args);
	}

	private void handleFailedLoginAttempt(LoginException e) {
        LoginFailureInfo currentFailureInfo = e.getLoginFailureInfo();

        if (currentFailureInfo.isUnactivated()) {
            addActionError(getFailedLoginText(currentFailureInfo));
        } else if (currentFailureInfo.isExistingUser()) {
            addActionError(getFailedLoginText(currentFailureInfo));
            //if we find out user has tried N of N times, then lets go back to DB and lock him out.
            if (currentFailureInfo.requiresLocking()) {
                userManager.lockUser(getSecurityGuard().getTenantName(), e.getUserId(), e.getDuration(), e.getMaxAttempts());
            }
        } else {
            LoginFailureInfo failureInfo = getSession().getLoginFailureInfo();
            if (failureInfo == null) {
                getSession().setLoginFailureInfo(currentFailureInfo);
                failureInfo = currentFailureInfo;
            } else {
                failureInfo.unlockIfNecessary();
                failureInfo.incrementAndLockIfNecessary();
            }
            addActionError(getFailedLoginText(failureInfo));
        }
	}

	private String signIn(User loginUser) {		
		// if password expired, jump to reset password page (which requires username & resetkey)
		PasswordHelper passwordHelper = new PasswordHelper(tenantSettingsService.getTenantSettings().getPasswordPolicy());
		if (passwordHelper.isPasswordExpired(loginUser)) {
			resetPasswordKey = loginUser.createResetPasswordKey();	// need this in order to get to "reset password" screen.
			userManager.updateUser(loginUser);
			return REDIRECT_TO_URL;					
		}
		
		logUserIn(loginUser);
		
		if (previousUrl != null) {
			return "redirect";
		}
		return SUCCESS;
	}

	public String doConfirmKick() {
		WebSessionMap concurrentSession = ConcurrentLoginSessionListener.getSessionById(getSession().getConcurrentSessionId());

		// it's possible (but unlikely) that the concurrent user logged out between the login attempt and now
		if (concurrentSession != null) {
			// boot the concurrent session
			concurrentSession.setBooted(true);
		}

		User loginUser = persistenceManager.find(User.class, getSession().getUserId());
		if (loginUser != null) {
			return signIn(loginUser);
		}
		return ERROR;
	}

	@SkipValidation
	public String doBooted() {
		String result = doDelete();
		addFlashErrorText("label.why_you_have_been_signed_out");
		return result;
	}

	@SkipValidation
	public String doDelete() {
		clearSession();
        if(getLogoutUrl() != null)
            return "redirect";
        else
		    return SUCCESS;
	}

	private void clearSession() {
		WebSessionMap session = getSession();

		// The following items need to be preserved when clearing the session
		SystemSecurityGuard securityGuard = getSecurityGuard();
		boolean adminAuthenticated = session.isAdminAuthenticated();

		session.clear();

		// restore items
		session.setSecurityGuard(securityGuard);
		session.setAdminAuthenticated(adminAuthenticated);
	}

	protected void logUserIn(User loginUser) {
		fetchPerviousUrl();
		clearSession();
		loadSessionUser(loginUser.getId());
		loadEULAInformation();
		rememberMe();
        recordLoginOnMixPanel(loginUser);
		updateLastLoginDate(loginUser);

		logger.info(getLogLinePrefix() + "Login: " + signIn.getUserName() + " of " + getSecurityGuard().getTenantName());
	}

	private void updateLastLoginDate(User loginUser) {
		loginUser.setLastLogin(new Date());
		userManager.updateUser(loginUser);
	}

	private void recordLoginOnMixPanel(User user) {
        mixpanelService.sendEvent(MixpanelService.LOGGED_IN, user);
    }

	private void loadEULAInformation() {
		getSession().setEulaAcceptance(new SessionEulaAcceptance(getLoaderFactory().createCurrentEulaLoader(), getLoaderFactory().createLatestEulaAcceptanceLoader()));
	}

	private void fetchPerviousUrl() {
		UrlArchive urlArchive = new UrlArchive("preLoginContext", getServletRequest(), getServletRequest().getSession());
		previousUrl = urlArchive.fetchUrl();
		urlArchive.clearUrl();
	}

	public String getExternalPlansAndPricingUrl() {
		return getConfigContext().getString(ConfigEntry.EXTERNAL_PLANS_AND_PRICING_URL);
	}

	private void rememberMe() {
		signIn.storeRememberMe(createCookieFactory());
	}

	public String getPreviousUrl() {
		return previousUrl;
	}
	
    public String getLogoutUrl() {
        return getTenantSettingsService().getTenantSettings().getLogoutUrl();
    }

	public String getKey() { 
		return resetPasswordKey;
	}

	@Deprecated()
	public boolean isRememberMe() {
		return signIn.isRememberMe();
	}
	@Deprecated()
	public void setRememberMe(boolean rememberMe) {
		this.signIn.setRememberMe(rememberMe);
	}
	@Deprecated()
	public boolean isNormalLogin() {
		return signIn.isNormalLogin();
	}
	@Deprecated()
	public String getSecureRfid() {
		return signIn.getSecureRfid();
	}
	@Deprecated()
	public void setSecureRfid(String secureRfid) {
		this.signIn.setSecureRfid(secureRfid);
	}
	@Deprecated()
	public void setNormalLogin(boolean normalLogin) {
		this.signIn.setNormalLogin(normalLogin);
	}

	@Deprecated()
	public String getUserName() {
		return signIn.getUserName();
	}
	@Deprecated()
	public void setUserName(String userName) {
		this.signIn.setUserName(userName);
	}
	@Deprecated()
	public String getPassword() {
		return signIn.getPassword();
	}

	@Deprecated()
	public void setPassword(String password) {
		this.signIn.setPassword(password);
	}

	public SignIn getSignIn() {
		return signIn;
	}

	
	
}
