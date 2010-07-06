package com.n4systems.fieldid.actions.api;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import rfid.ejb.entity.FindProductOptionManufactureBean;
import rfid.web.helper.SessionUser;

import com.google.gson.Gson;
import com.n4systems.caching.Cache;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.ExtendedTextProviderAction;
import com.n4systems.fieldid.actions.downloaders.DownloadLinkAction;
import com.n4systems.fieldid.actions.helpers.AbstractActionTenantContextInitializer;
import com.n4systems.fieldid.permissions.SessionUserSecurityGuard;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.security.TenantLimitProxy;
import com.n4systems.fieldid.utils.CookieFactory;
import com.n4systems.fieldid.utils.SessionUserInUse;
import com.n4systems.fieldid.viewhelpers.BaseActionHelper;
import com.n4systems.fieldid.viewhelpers.SearchContainer;
import com.n4systems.fieldid.viewhelpers.navigation.NavOptionsController;
import com.n4systems.handlers.creator.CreateHandlerFactory;
import com.n4systems.handlers.remover.RemovalHandlerFactory;
import com.n4systems.model.Tenant;
import com.n4systems.model.activesession.ActiveSessionLoader;
import com.n4systems.model.activesession.ActiveSessionSaver;
import com.n4systems.model.downloadlink.DownloadCoordinator;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.user.User;
import com.n4systems.notifiers.Notifier;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.persistence.loaders.NonSecureLoaderFactory;
import com.n4systems.persistence.savers.SaverFactory;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.FieldidDateFormatter;
import com.n4systems.util.ListingPair;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.time.SystemClock;
import com.n4systems.util.uri.ActionURLBuilder;

import freemarker.template.utility.StringUtil;

@SuppressWarnings("serial")
abstract public class AbstractAction extends ExtendedTextProviderAction implements FlashScopeAware, UserDateFormatValidator  {
	public static final String MISSING = "missing";
	public static final String INVALID_SECURITY = "invalid_security";
	public static final String REDIRECT_TO_URL = "redirect_to_url";
	
	protected final PersistenceManager persistenceManager;
	protected BaseActionHelper helper;
	
	private Collection<String> flashMessages = new ArrayList<String>();
	private Collection<String> flashErrors = new ArrayList<String>();
	private Collection<FindProductOptionManufactureBean> searchOptions;
	private LoaderFactory loaderFactory;
	private SaverFactory saverFactory;
	private Gson json;
	private TenantLimitProxy limitProxy;
	private User user = null;
	private NavOptionsController navOptions;
	private String redirectUrl;
	private RemovalHandlerFactory rhFactory;
	private NonSecureLoaderFactory nonSecureLoaderFactory;
	private CreateHandlerFactory createHandlerFactory;
	private DownloadCoordinator downloadCoordinator;
	private boolean useContext = false;
	
	public AbstractAction(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
		helper = new BaseActionHelper();
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
	
	protected User getUser() {
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
	
	public SecurityFilter getSecurityFilter() {
		return getSessionUser().getSecurityFilter();
	}
	
	public boolean isReportActive(){
		SearchContainer searchContainer = getSession().getReportCriteria();
		return ( searchContainer != null && searchContainer.getSearchId() != null );
	}
	
	public void addFlashMessage( String message ) {
		flashMessages.add( message );
		addActionMessage( message );
	}
	
	public void addFlashMessageText( String message ) {
		addFlashMessage( getText( message ) );
	}
	
	public void addActionMessageText( String message ) {
		addActionMessage( getText( message ) );
	}
	
	public void clearFlashScope() {
		clearFlashMessages();
		clearFlashErrors();
	}
	
	public void clearFlashMessages( ) {
		flashMessages.clear();
	}
	
	public void addFlashError( String error ) {
		flashErrors.add( error );
		addActionError( error );
	}
	
	public void addFlashErrorText( String error ) {
		addFlashError( getText( error  ) );
	}
	
	public void addActionErrorText( String error ) {
		addActionError( getText( error  ) );
	}
	
	public void clearFlashErrors( ) {
		flashErrors.clear();
	}
	
	public Collection<String> getFlashMessages(){
		return flashMessages;
	}
	
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
	
	public boolean isValidDate(String date, boolean usingTime) {
		return getSessionUser().createUserDateConverter().isValidDate(date, usingTime);
	}
	

	public String formatDateTime(Date date) {
		return formatAnyDate(date, true, true);
	}
	
	public String formatDate(Date date, boolean convertTimeZone) {
		return formatAnyDate(date, convertTimeZone, false);
	}
	
	protected String formatAnyDate(Date date, boolean convertTimeZone, boolean showTime) {
		return new FieldidDateFormatter(date, getSessionUser(), convertTimeZone, showTime).format();
	}
	
	public Collection<FindProductOptionManufactureBean> getSearchOptions() {
		if( searchOptions == null ) {
			try {
				searchOptions = ServiceLocator.getOption().getFindProductOptionsForTenant( getTenantId() );
			} catch (Exception e) {
				// eat the exception.
			}
		}
		return searchOptions;
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
	
	
	
	protected RemovalHandlerFactory getRemovalHandlerFactory() {
		if (rhFactory == null) {
			rhFactory = new RemovalHandlerFactory(getLoaderFactory());
		}
		return rhFactory;
	}
	
	public String getTextArg(String text, Object param) {
		return getText(text, new String[] {param.toString()});
	}
	
	public String getBaseBrandedUrl(String tenantName) {
		Tenant tenant = new Tenant();
		tenant.setName(tenantName);
		return createActionURI(tenant, "");
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
	
	public String createActionURI(String action) {
		return createActionUrlBuilder().setAction(action).build();
	}

	protected ActionURLBuilder createActionUrlBuilder() {
		return new ActionURLBuilder(getBaseURI(), getConfigContext());
	}
	
	public String createActionURI(Tenant tenant, String action) {
		return createActionUrlBuilder().setAction(action).setCompany(tenant).build();
	}
	
	public TenantLimitProxy getLimits() {
		if (limitProxy == null) { 
			limitProxy = new TenantLimitProxy(getTenantId());
		}
		return limitProxy;
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
	
	public String labelForTenantLimit(Long limit) {
		if (limit.equals(TenantLimit.UNLIMITED)) {
			return "label.unlimited";
		}
		return limit.toString();
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
			downloadCoordinator = new DownloadCoordinator(getUser(), getSaverFactory().createDownloadLinkSaver());
		}
		return downloadCoordinator;
	}
	
	public String getDownloadLinkUrl() {
		return DownloadLinkAction.buildDownloadUrl(this);
	}
	
	public List<ListingPair> getVendorContextList() {
		return Cache.getVendorListStore().getAsListingPair(getSessionUserOwner().getInternalOrg());
	}
	
	public void setVendorContext(Long vendorContext) {
		getSession().setVendorContext(vendorContext);
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
	
	public String getCurrentVendorContextName() {
		if (getVendorContext() == null) {
			return "label.my_company";
		}
		
		Long contextId = getVendorContext();
		String name = null;
		for (ListingPair vendor: getVendorContextList()) {
			if (contextId.equals(vendor.getId())) {
				name = vendor.getName();
				break;
			}
		}
		return name;
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

	protected ConfigContext getConfigContext() {
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
}
