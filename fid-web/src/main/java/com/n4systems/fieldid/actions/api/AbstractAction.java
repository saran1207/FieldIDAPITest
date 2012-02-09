package com.n4systems.fieldid.actions.api;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import rfid.web.helper.SessionUser;

import com.google.gson.Gson;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.UIConstants;
import com.n4systems.fieldid.actions.ExtendedTextProviderAction;
import com.n4systems.fieldid.actions.downloaders.DownloadLinkAction;
import com.n4systems.fieldid.actions.helpers.AbstractActionTenantContextInitializer;
import com.n4systems.fieldid.permissions.SessionUserSecurityGuard;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.utils.CookieFactory;
import com.n4systems.fieldid.utils.SessionUserInUse;
import com.n4systems.fieldid.utils.WebContextProvider;
import com.n4systems.fieldid.viewhelpers.BaseActionHelper;
import com.n4systems.fieldid.viewhelpers.SearchContainer;
import com.n4systems.fieldid.viewhelpers.navigation.NavOptionsController;
import com.n4systems.handlers.creator.CreateHandlerFactory;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.activesession.ActiveSessionLoader;
import com.n4systems.model.activesession.ActiveSessionSaver;
import com.n4systems.model.downloadlink.DownloadCoordinator;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.notifiers.Notifier;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.persistence.loaders.NonSecureLoaderFactory;
import com.n4systems.persistence.savers.SaverFactory;
import com.n4systems.services.SecurityContext;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.util.FieldidDateFormatter;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.time.SystemClock;
import com.n4systems.util.uri.ActionURLBuilder;

import freemarker.template.utility.StringUtil;

@SuppressWarnings("serial")
abstract public class AbstractAction extends ExtendedTextProviderAction implements FlashScopeAware, UserDateFormatValidator, LoaderFactoryProvider, WebContextProvider, UIConstants {
	public static final String MISSING = "missing";
	public static final String INVALID_SECURITY = "invalid_security";
	public static final String REDIRECT_TO_URL = "redirect_to_url";
	
	protected final PersistenceManager persistenceManager;
	protected BaseActionHelper helper;
	
	private final Collection<String> flashMessages = new ArrayList<String>();
	private final Collection<String> flashErrors = new ArrayList<String>();
	private LoaderFactory loaderFactory;
	private SaverFactory saverFactory;
	private Gson json;
	private User user = null;
	private NavOptionsController navOptions;
	private String redirectUrl;
	private NonSecureLoaderFactory nonSecureLoaderFactory;
	private CreateHandlerFactory createHandlerFactory;
	private DownloadCoordinator downloadCoordinator;
	private boolean useContext = false;
    private String pageName;
	
    @Autowired
    protected UserLimitService userLimitService;
	
    @Autowired
    protected SecurityContext securityContext;
    
    @Autowired
    protected TenantSettingsService tenantSettingsService;
    
	public AbstractAction(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
		helper = new BaseActionHelper();
	}

	public String getSupportUrl() { 
		String supportUrl = getSessionUser().getTenant().getSettings().getSupportUrl();
		return supportUrl==null ? DEFAULT_SUPPORT_URL : supportUrl;
	}
	
	@Override
	public SessionUser getSessionUser() {
		return getSession().getSessionUser();
	}
	
	public Long getSessionUserId() {
		return getSessionUser().getId();
	}
	
	public SystemSecurityGuard getSecurityGuard() {
		return getSession().getSecurityGuard();
	}
	
	@Deprecated // use getCurrentUser...only here to avoid struts refactoring.
	protected User getUser() {
		return getCurrentUser();
	}
	
	protected User getCurrentUser() { 
		return persistenceManager.find(User.class, getSessionUser().getUniqueID());		
	}
	
	protected boolean isLoggedIn() {
		
		return (getSessionUser() != null && 
				getSessionUser().getTenant().equals(getTenant()) && 
				new SessionUserInUse(new ActiveSessionLoader(), getConfigContext(), new SystemClock(), new ActiveSessionSaver()).doesActiveSessionBelongTo(getSessionUserId(), getSession().getId())); 
	}

	protected void refreshSessionUser() {
		loadSessionUser(getSessionUser().getId());
	}
	
	protected void loadSessionUser(Long userId) {
		User user = persistenceManager.find(new QueryBuilder<User>(User.class, new OpenSecurityFilter()).addSimpleWhere("id", userId).addPostFetchPaths("permissions", "owner.primaryOrg.id"));
		setupSessionUser(user);
	}
	
	private void setupSessionUser(User user) {
		getSession().setSessionUser(new SessionUser(user));
		getSession().setUserSecurityGuard(new SessionUserSecurityGuard(user));
		new AbstractActionTenantContextInitializer(this).refreshSecurityGaurd();

	}
	
	public Long getTenantId() {
		return getTenant().getId();
	}
	
	public Tenant getTenant() {
		return getSecurityGuard().getTenant();
	}
	
	public PrimaryOrg getPrimaryOrg() {
		return getSecurityGuard().getPrimaryOrg();
	}
	
	public InternalOrg getInternalOrg() {
		return getSecurityFilter().getOwner().getInternalOrg();
	}
	
	public BaseOrg getSessionUserOwner() {
		return getSessionUser().getOwner();
	}
	
	@Override
	public SecurityFilter getSecurityFilter() {
		return getSessionUser().getSecurityFilter();
	}
	
	public boolean isReportActive(){
		SearchContainer searchContainer = getSession().getReportCriteria();
		return ( searchContainer != null && searchContainer.getSearchId() != null );
	}
	
	@Override
	public void addFlashMessage( String message ) {
		flashMessages.add( message );
		addActionMessage( message );
	}
	
	public void addFlashMessageText( String message ) {
		addFlashMessage( getText( message ) );
	}
	
	public void addFlashMessageText( String message, String...args ) {
		addFlashMessage( getText(message,args) );
	}
	
	public void addActionMessageText( String message ) {
		addActionMessage( getText( message ) );
	}
	
	@Override
	public void clearFlashScope() {
		clearFlashMessages();
		clearFlashErrors();
	}
	
	@Override
	public void clearFlashMessages( ) {
		flashMessages.clear();
	}
	
	@Override
	public void addFlashError( String error ) {
		flashErrors.add( error );
		addActionError( error );
	}
	
	public void addFlashErrorText( String error ) {
		addFlashError( getText(error) );
	}
	
	public void addActionErrorText( String error ) {
		addActionError( getText(error) );
	}
	
	public void addActionErrorText(String error, String...args) {
		addActionError( getText(error,args) );
	}
	
	@Override
	public void clearFlashErrors( ) {
		flashErrors.clear();
	}
	
	@Override
	public Collection<String> getFlashMessages(){
		return flashMessages;
	}
	
	@Override
	public Collection<String> getFlashErrors(){
		return flashErrors;
	}

	public Gson getJson() {
		if( json == null ) {
			json = new Gson();
			
		}
		return json;
	}
	
	public AbstractAction getAction() {
		return this;
	}
	
	
	
	public String convertDate(Date date) {
		return getSessionUser().createUserDateConverter().convertDate(date);
	}
	
	public Date convertDate(String date) {
		return getSessionUser().createUserDateConverter().convertDate(date);
	}
	
	public Date convertToEndOfDay(String date) {
		return getSessionUser().createUserDateConverter().convertToEndOfDay(date);
	}
	
	public String convertDateTime(Date date) {
		return getSessionUser().createUserDateConverter().convertDateTime(date);
	}
	
	public Date convertDateTime(String date) {
		return getSessionUser().createUserDateConverter().convertDateTime(date);
	}
	
	@Override
	public boolean isValidDate(String date, boolean usingTime) {
		return getSessionUser().createUserDateConverter().isValidDate(date, usingTime);
	}
	

	public String formatDateTime(Date date) {
		return formatAnyDate(date, true, true);
	}
	
	public String formatDate(Date date, boolean convertTimeZone) {
		return formatAnyDate(date, convertTimeZone, false);
	}
	
	public String formatBigDecimal(BigDecimal d) {		
		return d!=null ? d.toPlainString() : "";
	}
	
	protected String formatAnyDate(Date date, boolean convertTimeZone, boolean showTime) {
		return new FieldidDateFormatter(date, getSessionUser(), convertTimeZone, showTime).format();
	}
	
	public boolean isDevMode() {
		String serverName = getServletRequest().getServerName();
		String systemDomain = getConfigContext().getString(ConfigEntry.SYSTEM_DOMAIN);
		return !(serverName.toLowerCase().endsWith(systemDomain));
	}
	
	public String replaceCR(String string) {
		return string.replace("\n", "<br />");
	}
		
	protected boolean isBlank( String testString ) {
		return ( testString == null || testString.trim().length() == 0  );
	}
	
	protected String getLogLinePrefix() {
		return "[Session: " + getSession().getId() + ( (!isLoggedIn() ) ? "  public " : "" ) + " ] ";
	}
	
	public String getCurrentAction() {
		return getActionContext().getName();
	}
	
	protected Map<String, String> decodeMapKeys(Map<String, String> encodedMap) {
		Map<String, String> decodedMap = new HashMap<String, String>();
		
		try {
			for (String key : encodedMap.keySet()) {
				decodedMap.put(URLDecoder.decode(key, "UTF-8"), encodedMap.get(key));
			}
		} catch (UnsupportedEncodingException e) {
			// this should never happen since we're hardcoded to UTF-8 and that's standard.
			decodedMap = null;
		}

		return decodedMap;
	}

	protected Map<String, String> encodeMapKeys(Map<String, String> rawMap) {
		Map<String, String> encodedMap = new HashMap<String, String>();
		
		try {
			for (String key : rawMap.keySet()) {
				encodedMap.put(StringUtil.URLEnc(key, "UTF-8"), rawMap.get(key));
			}
		} catch (UnsupportedEncodingException e) {
			// this should never happen since we're hardcoded to UTF-8 and that's standard.
			encodedMap = null;
		}

		return encodedMap;
	}
	

	
	protected User fetchCurrentUser() {
		if (getSessionUserId() == null) {
			return null;
		}
		if (user == null) {
			user = persistenceManager.find(User.class, getSessionUserId());
		}
		return user;
	}

	public NavOptionsController getNavOptions() {
		if (navOptions == null) {
			navOptions = new NavOptionsController(getSessionUser(), getSecurityGuard());
		}
		return navOptions;
	}
	
	public void setPageType(String pageType, String currentAction) {
		navOptions = new NavOptionsController(getSessionUser(), getSecurityGuard(), pageType, currentAction);
	}
	
	@Override
	public LoaderFactory getLoaderFactory() {
		if (loaderFactory == null) {
			loaderFactory = new LoaderFactory(getSecurityFilter());
		}
		return loaderFactory;
	}
	
	public SaverFactory getSaverFactory() {
		if (saverFactory == null) {
			saverFactory = new SaverFactory();
		}
		return saverFactory;
	}
	
	public NonSecureLoaderFactory getNonSecureLoaderFactory() {
		if (nonSecureLoaderFactory == null) {
			nonSecureLoaderFactory = new NonSecureLoaderFactory();
		}
		return nonSecureLoaderFactory;
	}
	
	public LoaderFactory getOpenSecurityFilteredLoaderFactory() {
		if (loaderFactory == null) {
			loaderFactory = new LoaderFactory(new OpenSecurityFilter());
		}
		return loaderFactory;
	}
	
	public String getTextArg(String text, Object param) {
		return getText(text, new String[] {param.toString()});
	}
	
	public String getBaseBrandedUrl(String tenantName) {
		Tenant tenant = new Tenant();
		tenant.setName(tenantName);
		return createActionURI(tenant, "");
	}

	public String getBaseBrandedNonFieldIDUrl(String tenantName){
		Tenant tenant = new Tenant();
		tenant.setName(tenantName);
		return createNonFieldIDActionURI(tenant, "");
	}
	
	public String getLoginUrl() {
		return getLoginUrlForTenant(getTenant());
	}
	public String getLoginUrlForTenant(String tenantName) {
		Tenant tenant = new Tenant();
		tenant.setName(tenantName);
		return getLoginUrlForTenant(tenant);
	}
	public String getLoginUrlForTenant(Tenant tenant) {
		return createActionURI(tenant, "login");
	}
	
	public String getEmbeddedLoginUrl() {
		return createActionURI(getTenant(), "embedded/login");
	}
	
	public URI getBaseURI() {
		// creates a URI based on the current url, and resolved against the context path which should be /fieldid.  We add on the extra / since we currently need it.
		return URI.create(getServletRequest().getRequestURL().toString()).resolve(getServletRequest().getContextPath() + "/");
	}
	
	public URI getBaseNonFieldIdURI(){
		return URI.create(getServletRequest().getRequestURL().toString()).resolve("/");
	}
	
	public String createActionURI(String action) {
		return createActionUrlBuilder().setAction(action).build();
	}

	protected ActionURLBuilder createActionUrlBuilder() {
		return new ActionURLBuilder(getBaseURI(), getConfigContext());
	}
	
	protected ActionURLBuilder createNonFieldIDActionUrlBuilder(){
		return new ActionURLBuilder(getBaseNonFieldIdURI(), getConfigContext());
	}
	
	public String createActionURI(Tenant tenant, String action) {
		return createActionUrlBuilder().setAction(action).setCompany(tenant).build();
	}
	
	public String createActionURIWithParameters(Tenant tenant, String action, String parameters) {
		return createActionUrlBuilder().setAction(action).setParameters(parameters).setCompany(tenant).build();
	}
	
	public String createNonFieldIDActionURI(Tenant tenant, String action) {
		return createNonFieldIDActionUrlBuilder().setAction(action).setCompany(tenant).build();
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	public String getHumanReadableFileSize(Long fileSize) {
		String byteCountToDisplaySize = FileUtils.byteCountToDisplaySize(fileSize);
		return byteCountToDisplaySize;
	}

	protected CreateHandlerFactory getCreateHandlerFactory() {
		if (createHandlerFactory == null) {
			createHandlerFactory = new CreateHandlerFactory();
		}
		
		return createHandlerFactory;
	}
	
	public String getHouseAccountName() {
		return getConfigContext().getString(ConfigEntry.HOUSE_ACCOUNT_NAME);
	}
	
	
	public BaseActionHelper getHelper() {
		return helper;
	}
	
	public void overrideHelper(BaseActionHelper helper) {
		this.helper = helper;
	}
	
	public DownloadCoordinator getDownloadCoordinator() {
		if (downloadCoordinator == null) {
			downloadCoordinator = new DownloadCoordinator(getCurrentUser(), getSaverFactory().createDownloadLinkSaver());
		}
		return downloadCoordinator;
	}
	
	public String getDownloadLinkUrl() {
		return DownloadLinkAction.buildDownloadUrl(this);
	}
	
	public void setVendorContext(Long vendorContext) {
		getSession().setVendorContext(vendorContext);
	}
	
	public boolean isLocationHeirarchyFeatureEnabled(){
		return getPrimaryOrg().hasExtendedFeature(ExtendedFeature.AdvancedLocation);
	}
	
	public Long getVendorContext() {
		return getSession().getVendorContext();
	}
	
	public boolean isInVendorContext() {
		return (getVendorContext() != null && useContext );
	}
	
	public boolean isUseContext() {
		return useContext;
	}

	public void setUseContext(boolean useContext) {
		this.useContext = useContext;
	}
	
	public String getUseContextString() {
		return Boolean.toString(useContext);
	}
	

	/**
	 * used for the on radio button list
	 * 
	 * @return  a map with just a true key
	 */
	@SuppressWarnings("unchecked")
	public Map getOn() {
		return getOn("");
	}

	@SuppressWarnings("unchecked")
	public Map getOn(String label) {
		Map<Boolean, String> onOff = new HashMap<Boolean, String>();
		onOff.put(true, label);
		return onOff;
	}

	/**
	 * used for the off radio button list
	 * 
	 * @return  a map with just a false key
	 */
	@SuppressWarnings("unchecked")
	public Map getOff() {
		return getOff("");
	}

	@SuppressWarnings("unchecked")
	public Map getOff(String label) {
		Map<Boolean, String> onOff = new HashMap<Boolean, String>();
		onOff.put(false, label);
		return onOff;
	}

	/**
	 * used for the off radio button list
	 * 
	 * @return  a map with just a false key
	 */
	@SuppressWarnings("unchecked")
	public Map getSingleMapElement(Long id) {
		Map<Long, String> singleElementMap = new HashMap<Long, String>();
		singleElementMap.put(id, "");
		return singleElementMap;
	}

	protected ConfigurationProvider getConfigContext() {
		return ConfigContext.getCurrentContext();
	}

	
	protected Notifier getDefaultNotifier() {
		return ServiceLocator.getDefaultNotifier();
	}
	
	
	public Integer getCurrentSessionTimeout() {
		return ConfigContext.getCurrentContext().getInteger(ConfigEntry.ACTIVE_SESSION_TIME_OUT);
	}

	protected CookieFactory createCookieFactory() {
		return new CookieFactory(getServletRequest(), getServletResponse());
	}

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public boolean isPageName(String pageName) {
        return pageName.equals(this.pageName);
    }

	public String getHelpUrl() {
		return getConfigContext().getString(ConfigEntry.HELP_SYSTEM_URL);
	}
	
	public String getClickTaleStart() {
		return getConfigContext().getString(ConfigEntry.CLICKTALE_START);
	}

	public String getClickTaleEnd() {
		return getConfigContext().getString(ConfigEntry.CLICKTALE_END);
	}

	public UserLimitService getUserLimitService() {
		return userLimitService;
	}

	public SecurityContext getSecurityContext() {
		return securityContext;
	}

    public String getIdentifierLabel() {
        return getPrimaryOrg().getIdentifierLabel();
    }

    public boolean isUseLegacyCss() {
        return true;
    }

	public TenantSettingsService getTenantSettingsService() {
		return tenantSettingsService;
	}

    public boolean isGoogleAnalyticsEnabled() {
        return ConfigContext.getCurrentContext().getBoolean(ConfigEntry.GOOGLE_ANALYTICS_ENABLED);
    }
    
}
