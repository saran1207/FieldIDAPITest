package com.n4systems.fieldid.actions;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.web.helper.SessionEulaAcceptance;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.utils.SessionUserInUse;
import com.n4systems.fieldid.utils.UrlArchive;
import com.n4systems.model.activesession.ActiveSession;
import com.n4systems.model.activesession.ActiveSessionLoader;
import com.n4systems.model.activesession.ActiveSessionSaver;
import com.n4systems.model.user.User;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.time.SystemClock;

public class SignInAction extends AbstractAction {

	private static final Logger logger = Logger.getLogger(SignInAction.class);
	private static final long serialVersionUID = 1L;

	protected UserManager userManager;

	private String previousUrl;

	private SignIn signIn = new SignIn();

	public SignInAction(UserManager userManager, PersistenceManager persistenceManager) {
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

		if (signIn.isValid(this)) {
			loginUser = findUser();
			if (loginUser != null) {
				if (signInWillKickAnotherSessionOut(loginUser)) {
					storeUserAuthenticationForConfirmOfSessionKick(loginUser);
					return "confirmKick";
				}
				return signIn(loginUser);
			}
			addActionError(getText("error.loginfailure"));
		}
		return INPUT;
	}

	private void storeUserAuthenticationForConfirmOfSessionKick(User loginUser) {
		getSession().setUserAuthHolder(loginUser.getId());
	}

	private User findUser() {
		User loginUser;
		if (signIn.isNormalLogin()) {
			loginUser = userManager.findUser(getSecurityGuard().getTenantName(), signIn.getUserName(), signIn.getPassword());
		} else {
			//Being uppercased since that's what happens on the user before we hash the rfid value. 
			loginUser = userManager.findUser(getSecurityGuard().getTenantName(), signIn.getSecureRfid().toUpperCase());
		}
		return loginUser;
	}

	private boolean signInWillKickAnotherSessionOut(User loginUser) {
		SessionUserInUse sessionUserInUse = new SessionUserInUse(new ActiveSessionLoader(), ConfigContext.getCurrentContext(), new SystemClock(), new ActiveSessionSaver());
		return sessionUserInUse.isThereAnActiveSessionFor(loginUser.getId()) && !sessionUserInUse.doesActiveSessionBelongTo(loginUser.getId(), getSession().getId());
	}

	private String signIn(User loginUser) {
		logUserIn(loginUser);
		
		
		if (previousUrl != null) {
			return "redirect";
		}
		return SUCCESS;
	}
	
	public String doConfirmKick() {
		Long userId = getSession().getUserAuthHolder();
		if (userId == null) {
			return ERROR;
		}
		
		User loginUser = persistenceManager.find(User.class, userId);
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
		expireActiveSession();
		clearSession();
		return SUCCESS;
	}


	private void expireActiveSession() {
		if (isLoggedIn()) {
			SessionUserInUse sessionUserInUse = new SessionUserInUse(new ActiveSessionLoader(), ConfigContext.getCurrentContext(), new SystemClock(), new ActiveSessionSaver());
			sessionUserInUse.expireSession(getSessionUserId());
		}
	}

	private void clearSession() {
		// grab SecuirtyGuard
		SystemSecurityGuard securityGuard = getSecurityGuard();
		getSession().clear();
		// if the security guard gets cleared here we will no longer know the tenant context.
		
		// restore securityGuard
		getSession().setSecurityGuard(securityGuard);		
	}

	

	protected void logUserIn(User loginUser) {
		fetchPerviousUrl();
		clearSession();
		loadSessionUser(loginUser.getId());
		loadEULAInformation();
		rememberMe();
		registerActiveSession(loginUser, getSession().getId());
		logger.info(getLogLinePrefix() + "Login: " + signIn.getUserName() + " of " + getSecurityGuard().getTenantName());
	}

	private void registerActiveSession(User loginUser, String sessionId) {
		new ActiveSessionSaver().save(new ActiveSession(loginUser, sessionId));
	}

	private void loadEULAInformation() {
		getSession().setEulaAcceptance(new SessionEulaAcceptance(getLoaderFactory().createCurrentEulaLoader(), getLoaderFactory().createLatestEulaAcceptanceLoader()));
	}

	private void fetchPerviousUrl() {
		UrlArchive urlArchive =  new UrlArchive("preLoginContext", getServletRequest(), getServletRequest().getSession());
		previousUrl = urlArchive.fetchUrl();
		urlArchive.clearUrl();
	}

	private void rememberMe() {
		signIn.storeRememberMe(createCookieFactory());
	}

	public String getPreviousUrl() {
		return previousUrl;
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
