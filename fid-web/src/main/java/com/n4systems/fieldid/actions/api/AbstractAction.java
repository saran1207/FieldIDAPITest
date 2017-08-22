package com.n4systems.fieldid.actions.api;

import com.google.gson.Gson;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.UIConstants;
import com.n4systems.fieldid.actions.ExtendedTextProviderAction;
import com.n4systems.fieldid.actions.downloaders.DownloadLinkAction;
import com.n4systems.fieldid.actions.helpers.AbstractActionTenantContextInitializer;
import com.n4systems.fieldid.permissions.SessionUserSecurityGuard;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.offlineprofile.OfflineProfileService;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.utils.CookieFactory;
import com.n4systems.fieldid.version.FieldIdVersion;
import com.n4systems.fieldid.viewhelpers.BaseActionHelper;
import com.n4systems.fieldid.viewhelpers.navigation.NavOptionsController;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.admin.AdminUserType;
import com.n4systems.model.downloadlink.DownloadCoordinator;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.notifiers.Notifier;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.persistence.loaders.NonSecureLoaderFactory;
import com.n4systems.persistence.savers.SaverFactory;
import com.n4systems.services.SecurityContext;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.util.FieldIdDateFormatter;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.uri.ActionURLBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.wicket.util.crypt.Base64;
import org.apache.wicket.util.string.interpolator.MapVariableInterpolator;
import org.springframework.beans.factory.annotation.Autowired;
import rfid.web.helper.SessionUser;

import java.math.BigDecimal;
import java.net.URI;
import java.util.*;

@SuppressWarnings("serial")
abstract public class AbstractAction extends ExtendedTextProviderAction implements FlashScopeAware, UserDateFormatValidator, LoaderFactoryProvider, UIConstants {
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
	private DownloadCoordinator downloadCoordinator;
	private boolean useContext = false;
    private String pageName;

    @Autowired
    protected UserLimitService userLimitService;

    @Autowired
    protected UserGroupService userGroupService;

    @Autowired
    protected SecurityContext securityContext;

    @Autowired
    protected TenantSettingsService tenantSettingsService;

    @Autowired
    protected S3Service s3Service;

    @Autowired
    protected ConfigService configService;

    @Autowired
    private OfflineProfileService offlineProfileService;

	public AbstractAction(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
		helper = new BaseActionHelper();
	}

	public String getSupportUrl() {
		String supportUrl = getSessionUser().getTenant().getSettings().getSupportUrl();
		return supportUrl==null ? DEFAULT_SUPPORT_URL : supportUrl;
	}

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
		return (getSessionUser() != null && getSessionUser().getTenant().equals(getTenant()) && !getSession().isBooted());
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
        getSession().setVisibleUsers(userGroupService.findUsersVisibleTo(user));
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

	public SecurityFilter getSecurityFilter() {
		return getSessionUser().getSecurityFilter();
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

    public Date convertDateWithOptionalTime(String date) {
        return getSessionUser().createUserDateConverter().convertDateWithOptionalTime(date);
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

    public String formatDateWithTime(Date date, boolean convertTimeZone) {
        // only show time if non-midnight.
        boolean isMidnight = new PlainDate(date).equals(date);
        return formatAnyDate(date, convertTimeZone, !isMidnight);
    }

    public String formatDate(Date date, boolean convertTimeZone) {
		return formatAnyDate(date, convertTimeZone, false);
	}

	public String formatBigDecimal(BigDecimal d) {
		return d!=null ? d.toPlainString() : "";
	}

	protected String formatAnyDate(Date date, boolean convertTimeZone, boolean showTime) {
		return new FieldIdDateFormatter(date, getSessionUser(), convertTimeZone, showTime).format();
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

        for (String key : encodedMap.keySet()) {
            decodedMap.put(new String(Base64.decodeBase64(key)), encodedMap.get(key));
        }

		return decodedMap;
	}

	protected Map<String, String> encodeMapKeys(Map<String, String> rawMap) {
		Map<String, String> encodedMap = new HashMap<String, String>();

        for (String key : rawMap.keySet()) {
            encodedMap.put(getBase64EncodedString(key), rawMap.get(key));
        }

		return encodedMap;
	}

    public String getBase64EncodedString(String key) {
        return new String(Base64.encodeBase64(key.getBytes())).replace("=", "");
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
		return ConfigService.getInstance();
	}


	protected Notifier getDefaultNotifier() {
		return ServiceLocator.getDefaultNotifier();
	}


	public Integer getCurrentSessionTimeout() {
		return ConfigService.getInstance().getInteger(ConfigEntry.ACTIVE_SESSION_TIME_OUT);
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
        return ConfigService.getInstance().getBoolean(ConfigEntry.GOOGLE_ANALYTICS_ENABLED);
    }

    public boolean isApptegicEnabled() {
        return getConfigContext().getBoolean(ConfigEntry.APPTEGIC_ENABLED);
    }

    public String getApptegicDataset() {
        return getConfigContext().getString(ConfigEntry.APPTEGIC_DATASET);
    }

    public String getHeaderScript() {
        return getConfigContext().getString(ConfigEntry.STRUTS_HEADER_SCRIPT, getTenantId());
    }

    public String geFooterScript() {
        return getConfigContext().getString(ConfigEntry.STRUTS_FOOTER_SCRIPT, getTenantId());
    }


    public String getMainLogoUrl(Long tenantId) {
        return s3Service.getBrandingLogoURL(tenantId).toString();
    }

    public String getIEHeader() {
        return "Edge";
    }

    public String getProtoypeVersion() {
        return "prototype";
    }

    public String getVersion() {
        return FieldIdVersion.getVersion();
    }

    public String getCustomJs() {
        String js = configService.getString(ConfigEntry.CUSTOM_JS);

        Map<String,String> variableMap = new HashMap<String, String>();
        variableMap.put("tenant", getTenant().getName());
        variableMap.put("user", getSessionUser().getName());
        variableMap.put("userType", getSessionUser().getUserTypeLabel());
        variableMap.put("accountType", getSessionUser().getAccountType());

        MapVariableInterpolator interpolator = new MapVariableInterpolator(js, variableMap);
        return interpolator.toString();
    }

	public String getWalkmeJs() {
		return BASE_WALKME_SCRIPT.replace("${walkmeURL}", configService.getConfig().getWeb().getWalkmeUrl());
	}

	public String getSlaaskJsURL() {
		return SLAASK_JS_URL;
	}

	public String getSlaaskScript() {
		return SLAASK_JS_SCRIPT.replace("${UserName}",
				getSessionUser().getUserID()).replace("${User_Email}", getSessionUser().getEmailAddress());
	}

    public boolean isMultiLanguage() {
        return getTenant().getSettings().getTranslatedLanguages().size() > 0;
    }

    public boolean isOverrideLanguage(String methodName) {
        return true;
    }

    public boolean isOfflineProfileAvailable() {
        securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(getTenantId()));
        return offlineProfileService.hasOfflineProfile(getCurrentUser());
    }

	public boolean isSuperUser() {
		return getSession().getAdminUser().getType().equals(AdminUserType.SUPER);
	}
}
