package com.n4systems.fieldid.actions.api;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import rfid.ejb.entity.FindProductOptionManufactureBean;
import rfid.ejb.entity.UserBean;
import rfid.web.helper.Constants;
import rfid.web.helper.SessionUser;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.helpers.AbstractActionTenantContextInitializer;
import com.n4systems.fieldid.actions.search.InspectionReportAction;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.security.TenantLimitProxy;
import com.n4systems.fieldid.viewhelpers.SearchContainer;
import com.n4systems.fieldid.viewhelpers.navigation.NavOptionsController;
import com.n4systems.handlers.remover.RemovalHandlerFactory;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.DateHelper;
import com.n4systems.util.FieldidDateFormatter;
import com.n4systems.util.HostNameParser;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import freemarker.template.utility.StringUtil;

@SuppressWarnings("serial")
abstract public class AbstractAction extends ActionSupport implements ServletResponseAware, ServletRequestAware {
	public static final String MISSING = "missing";
	public static final String INVALID_SECURITY = "invalid_security";
	public static final String REDIRECT_TO_URL = "redirect_to_url";
	
	@SuppressWarnings("unchecked")
	private Map session;
	private HttpServletResponse response;
	private HttpServletRequest request;
	private Collection<String> flashMessages = new ArrayList<String>();
	private Collection<String> flashErrors = new ArrayList<String>();
	private Collection<FindProductOptionManufactureBean> searchOptions;
	private LoaderFactory loaderFactory;
	private JSONSerializer json;
	private TenantLimitProxy limitProxy;
	
	protected final PersistenceManager persistenceManager;
	
	private UserBean user = null;
	
	private NavOptionsController navOptions;
	
	private String redirectUrl;
	private RemovalHandlerFactory rhFactory;
	
	public AbstractAction(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	
	public ActionContext getActionContext() {
		return ActionContext.getContext();
	}
	
	@SuppressWarnings("unchecked")
	protected Map getSession() {
		if( session == null ) {
			session = getActionContext().getSession();
		}
		return session;
	}
	
	public SessionUser getSessionUser() {
		getSession();
		SessionUser sessionUser = (SessionUser)session.get(Constants.SESSION_USER);
		return sessionUser;
	}
	
	public Long getSessionUserId() {
		return getSessionUser().getId();
	}
	
	public SystemSecurityGuard getSecurityGuard() {
		return (SystemSecurityGuard)getSession().get(Constants.SECURITY_GUARD);
	}
	
	protected UserBean getUser() {
		return persistenceManager.findLegacy(UserBean.class, getSessionUser().getUniqueID());
	}
	
	@SuppressWarnings("unchecked")
	protected void setSessionVar(String key, Object value) {
		getSession().put(key, value);
	}
	
	protected Object getSessionVar(String key) {
		return (sessionContains(key)) ? getSession().get(key) : null;
	}
	
	protected boolean sessionContains(String key) {
		return (key != null) ? getSession().containsKey(key) : false;
	}
	
	protected Object clearSessionVar(String key) {
		Object oldValue = null;
		if (sessionContains(key)) {
			oldValue = getSession().remove(key);
		}
		return oldValue;
	}
	
	protected boolean isLoggedIn() {
		return (getSessionUser() != null && getSessionUser().getTenant().equals(getTenant())); 
	}

	protected void refreshSessionUser() {
		loadSessionUser(getSessionUser().getId());
	}
	protected void loadSessionUser(Long userId) {
		UserBean user = persistenceManager.find(new QueryBuilder<UserBean>(UserBean.class).addSimpleWhere("uniqueID", userId).addPostFetchPaths("permissions", "organization.primaryOrg.id"));
		setupSessionUser(user);
	}
	
	@SuppressWarnings("unchecked")
	private void setupSessionUser(UserBean user) {
		getSession().remove(Constants.SESSION_USER);
		SessionUser sessionUser = new SessionUser(user);
		getSession().put(Constants.SESSION_USER, sessionUser);
		new AbstractActionTenantContextInitializer(this, persistenceManager).refreshSecurityGaurd();
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
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public HttpServletRequest getServletRequest() {
		return request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;	
	}
	
	public HttpServletResponse getServletResponse() {
		return response;
	}
	
	public Long getEndUserId() {
		return getSessionUser().getR_EndUser(); 
	}
	
	public Long getDivisionId() {	
		return getSessionUser().getR_Division(); 
	}
	
	public SecurityFilter getSecurityFilter() {
		return getSessionUser().getSecurityFilter();
	}
	
	public boolean isReportActive(){
		SearchContainer searchContainer = (SearchContainer)getSession().get( InspectionReportAction.REPORT_CRITERIA );
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

	public JSONSerializer getJson() {
		if( json == null ) {
			json = new JSONSerializer();
		}
		return json;
	}
	
	public AbstractAction getAction() {
		return this;
	}
	
	public String convertDate(Date date) {
		return DateHelper.date2String(getSessionUser().getDateFormat(), date);
	}
	
	public Date convertDate(String date) {
		return DateHelper.string2Date(getSessionUser().getDateFormat(), date);
	}
	
	public Date convertToEndOfDay(String date) {
		Date day = convertDate(date);
		return (day != null) ? DateHelper.getEndOfDay(day) : null;
	}
	
	public String convertDateTime(Date date) {
		return DateHelper.date2String(getSessionUser().getDateTimeFormat(), date, getSessionUser().getTimeZone());
	}
	
	public Date convertDateTime(String date) {
		return DateHelper.string2DateTime(getSessionUser().getDateTimeFormat(), date, getSessionUser().getTimeZone());
	}
	
	public boolean isValidDate(String date, boolean usingTime) {
		// blank dates are ok
		if(date == null || date.trim().length() == 0) {
			return true;
		}
		String format = ( usingTime ) ? getSessionUser().getDateTimeFormat() : getSessionUser().getDateFormat();
		return  DateHelper.isDateValid(format, date);
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
	
	public boolean getDevMode() {
		String serverName = request.getServerName();
		return !( serverName.equalsIgnoreCase( "www.fieldid.com" ) || serverName.equalsIgnoreCase( "fieldid.com" ));
	}
	
	public String replaceCR(String string) {
		return string.replace("\n", "<br />");
	}
	
		
	protected boolean isBlank( String testString ) {
		return ( testString == null || testString.trim().length() == 0  );
	}
	
	protected String getLogLinePrefix() {
		return "[Session: " + request.getSession().getId() + ( (!isLoggedIn() ) ? "  public " : "" ) + " ] ";
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
	
	public String formatDateTime(Date date) {
		return formatAnyDate(date, true, true);
	}
	
	public String formatDate(Date date, boolean convertTimeZone) {
		return formatAnyDate(date, convertTimeZone, false);
	}
	
	protected String formatAnyDate(Date date, boolean convertTimeZone, boolean showTime) {
		return new FieldidDateFormatter(date, getSessionUser(), convertTimeZone, showTime).format();
	}
	
	
	protected UserBean fetchCurrentUser() {
		if (getSessionUserId() == null) {
			return null;
		}
		if (user == null) {
			user = persistenceManager.findLegacy(UserBean.class, getSessionUserId());
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
	
	protected LoaderFactory getLoaderFactory() {
		if (loaderFactory == null) {
			loaderFactory = new LoaderFactory(getSecurityFilter());
		}
		return loaderFactory;
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
		return baseBrandedUrlFor(tenantName) + "fieldid/" ;
	}

	public String baseBrandedUrlFor(String tenantName) {
		HostNameParser hostParser = HostNameParser.create(getBaseURI());
		String newHostname = hostParser.replaceFirstSubDomain(tenantName);
		
		return "https://" + newHostname + "/";
	}
	
	public String getLoginUrl() {
		return getLoginUrlForTenant(getTenant().getName());
	}
	
	public String getLoginUrlForTenant(String tenantName) {
		return getBaseBrandedUrl(tenantName) + "login.action" ;
	}
	
	public String getEmbeddedLoginUrl() {
		return getBaseBrandedUrl(getTenant().getName()) + "embedded/login.action";
	}
	
	public URI getBaseURI() {
		// creates a URI based on the current url, and resolved against the context path which should be /fieldid.  We add on the extra / since we currently need it.
		return URI.create(getServletRequest().getRequestURL().toString()).resolve(getServletRequest().getContextPath() + "/");
	}
	
	public URI createActionURI(String action) {
		return getBaseURI().resolve(action);
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
}
