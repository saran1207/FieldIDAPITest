package com.n4systems.fieldidadmin.actions;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.activesession.ActiveSession;
import com.n4systems.model.activesession.LastActiveSessionLoader;
import com.n4systems.model.asset.AssetCountLoader;
import com.n4systems.model.event.EventCountLoader;
import com.n4systems.model.orgs.AllPrimaryOrgsPaginatedLoader;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.TenantSaver;
import com.n4systems.model.tenant.extendedfeatures.ToggleExendedFeatureMethod;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.services.TenantFinder;
import com.n4systems.services.limiters.TenantLimitService;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DateHelper;
import com.n4systems.util.UserType;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@Validations
public class OrganizationAction extends AbstractCrud implements Preparable {

	public OrganizationAction(com.n4systems.ejb.PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(OrganizationAction.class);
	private static final int RESULTS_PER_PAGE = 20;

	private Long id;
	private Tenant tenant;
	private PrimaryOrg primaryOrg;

	private User adminUser = new User();
	private String title;
	private String note;
	private Map<String, Boolean> extendedFeatures = new HashMap<String, Boolean>();	

	private AssetCountLoader assetCountLoader = new AssetCountLoader();
	private EventCountLoader eventCountLoader = new EventCountLoader();
	private LastActiveSessionLoader lastActiveSessionLoader = new LastActiveSessionLoader();

	Collection<PrimaryOrg> primaryOrgs = null;
	private Map<Long, Long> totalAssets = new HashMap<Long, Long>();
	private Map<Long, Long> total30DayAssets = new HashMap<Long, Long>();
	private Map<Long, Long> totalEvents = new HashMap<Long, Long>();
	private Map<Long, Long> total30DayEvents = new HashMap<Long, Long>();
	private Map<Long, ActiveSession> lastActiveSessions = new HashMap<Long, ActiveSession>();
	
	private String sortColumn;
	private String sortDirection;
	
	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
	}	
	
	public void prepare() throws Exception {
		if (id != null) {
			tenant = TenantFinder.getInstance().findTenant(id);
			primaryOrg = TenantFinder.getInstance().findPrimaryOrg(id);
			for (ExtendedFeature feature : primaryOrg.getExtendedFeatures()) {
				extendedFeatures.put(feature.name(), true);
			}			
		} else {
			for (PrimaryOrg org: getPage().getList()) {
				totalAssets.put(org.getId(), loadTotalAssets(org.getTenant().getId(), false));
				total30DayAssets.put(org.getId(), loadTotalAssets(org.getTenant().getId(), true));
				totalEvents.put(org.getId(), loadTotalEvents(org.getTenant().getId(), false));
				total30DayEvents.put(org.getId(), loadTotalEvents(org.getTenant().getId(), true));
				lastActiveSessions.put(org.getId(), loadLastActiveSession(org.getTenant().getId()));
			}
		}
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doShow() {
		if (tenant == null) {
			addActionError("You can only edit tenants");
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doEdit() {
		if (tenant == null) {
			addActionError("You can only edit tenants");
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doUpdate() throws Exception {
		OrgSaver orgSaver = new OrgSaver();
		TenantSaver tenantSaver = new TenantSaver();
		
		Transaction transaction = PersistenceManager.startTransaction();
		
		try {
			if (primaryOrg.getId() != null) {
				processExtendedFeatures(transaction);
			}
			
			orgSaver.update(transaction, primaryOrg);
			tenantSaver.update(transaction, tenant);
			
			PersistenceManager.finishTransaction(transaction);
			
			TenantLimitService.getInstance().updateAll();
			
			
		} catch (Exception e) {
			PersistenceManager.rollbackTransaction(transaction);
			
			logger.error("Failed creating tenant", e);
			addActionError("Failed Creating Tenant: " + e.getMessage());
			return INPUT;
			
		}
		
		return SUCCESS;
	}
	
	private void processExtendedFeatures(Transaction transaction) {
		for (Entry<String, Boolean> inputFeature : extendedFeatures.entrySet()) {
			processFeature(inputFeature.getKey(), inputFeature.getValue(), transaction);
		}
	}

	private void processFeature(String featureName, boolean featureOn, Transaction transaction) {
		
		try {
			ExtendedFeature feature = ExtendedFeature.valueOf(featureName);
			
			new ToggleExendedFeatureMethod(feature, featureOn).applyTo(primaryOrg, transaction);
						
		} catch (IllegalArgumentException e) {
			addFieldError("extendedFeatures", "incorrect type of extended feature");
		} catch (Exception e) {
			addFieldError("extendedFeatures", "could not setup");
			throw new RuntimeException("problem while switching on or off an extended feature", e);
		}
	}	

	public String doNote() throws Exception {
		SubscriptionAgent subscriptionAgent = getCreateHandlerFactory().getSubscriptionAgent();
		
		if (primaryOrg != null) {	
			if (primaryOrg.getExternalId() != null) {
				subscriptionAgent.attachNote(primaryOrg.getExternalId(), title, note);
				title = null;
				note = null;
				addActionMessage("The note has been successfully attached");
			} else {
				addActionError("This organization is not attached to a netsuite account.");
				return INPUT;
			} 
		} else {
			addActionError("You can only add a note to an organization that exists");
			return INPUT;
		}
		
		return SUCCESS;
	}

	private void createSystemAccount(Transaction transaction) {
		User user = new User();
		user.setTenant(tenant);
		user.setOwner(primaryOrg);
		user.setTimeZoneID("United States:New York - New York");
		user.setRegistered(true);
		user.setPermissions(Permissions.SYSTEM);
		user.setUserType(UserType.SYSTEM);
		user.setUserID(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_USERNAME));
		user.setHashPassword(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_PASSWORD));
		user.setEmailAddress(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_ADDRESS));
		user.setFirstName("N4");
		user.setLastName("Admin");
		
		UserSaver saver = new UserSaver();
		saver.save(transaction, user);
	}

	public String doCreateUser() {
		Transaction transaction = null;
		try {
			transaction = PersistenceManager.startTransaction();
			
			createSystemAccount(transaction);
		} finally {
			PersistenceManager.finishTransaction(transaction);
		}
		
		return SUCCESS;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getAdminUser() {
		return adminUser;
	}
	
	@FieldExpressionValidator(expression="(adminUser.userID != 'n4systems')", message = "Admin user cannot have a userid of n4systems.")
	public void setAdminUser(User adminUser) {
		this.adminUser = adminUser;
	}
	
	public void setAdminUserPass(String pass) {
		adminUser.assignPassword(pass);
	}
	
	public Tenant getTenant() {
		return tenant;
	}
	public PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}
	
	public void setPrimaryOrg(PrimaryOrg primaryOrg) {
		this.primaryOrg = primaryOrg;
	}

	public Pager<PrimaryOrg> getPage() {
		return new AllPrimaryOrgsPaginatedLoader().setPage(getCurrentPage())
		                                          .setPageSize(RESULTS_PER_PAGE)
		                                          .setOrder(sortColumn, sortDirection!=null? sortDirection.equalsIgnoreCase("asc") : true)
		                                          .load();
	}

	public String getOtherDateFormat() {
		return (primaryOrg.getDateFormat() != null) ? DateHelper.java2Unix(primaryOrg.getDateFormat()) : "";
	}

	public String getFormattedDate() {
		return (primaryOrg.getDateFormat() != null) ? (new SimpleDateFormat(primaryOrg.getDateFormat())).format(new Date()) : "";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@SuppressWarnings("unchecked")
	public Map getExtendedFeatures() {
		return extendedFeatures;
	}

	@SuppressWarnings("unchecked")
	public void setExtendedFeatures(Map extendedFeatures) {
		this.extendedFeatures = extendedFeatures;
	}

	public ExtendedFeature[] getAvailableExtendedFeatures() {
		return ExtendedFeature.values();
	}
	
	public Long getDiskSpace() {
		return primaryOrg.getLimits().getDiskSpaceInBytes();
	}
	
	public void setDiskSpace(Long diskSpace) {
		primaryOrg.getLimits().setDiskSpaceInBytes(diskSpace);
	}
	
	public Long getAssets() {
		return primaryOrg.getLimits().getAssets();
	}
	
	public void setAssets(Long assets) {
		primaryOrg.getLimits().setAssets(assets);
	}
	
	public Long getLiteUsers(){
		return primaryOrg.getLimits().getLiteUsers();
	}
	
	public Long getUsers() {
		return primaryOrg.getLimits().getUsers();
	}
	
	public void setUsers(Long users) {
		primaryOrg.getLimits().setUsers(users);
	}
	
	public void setLiteUsers(Long users){
		primaryOrg.getLimits().setLiteUsers(users);
	}
	
	public Long getSecondaryOrgs() {
		return primaryOrg.getLimits().getSecondaryOrgs();
	}
	
	public void setSecondaryOrgs(Long secondaryOrgs) {
		primaryOrg.getLimits().setSecondaryOrgs(secondaryOrgs);
	}
	
	public void setDisabled(boolean disabled){
		tenant.setDisabled(disabled);
	}
	
	public boolean isDisabled(){
		return tenant.isDisabled();
	}
	
	private Long loadTotalAssets(Long tenantId, boolean limit30Days) {
		return assetCountLoader.setTenantId(tenantId).setLimit30Days(limit30Days).load();
	}

	private Long loadTotalEvents(Long tenantId, boolean limit30Days) {
		return eventCountLoader.setTenantId(tenantId).setLimit30Days(limit30Days).load();
	}
	
	private ActiveSession loadLastActiveSession(Long tenantId) {
		return lastActiveSessionLoader.setTenant(tenantId).load();
	}
	
	public Long getTotalAssets(Long orgId) {
			return totalAssets.get(orgId);
	}

	public Long getTotal30DayAssets(Long orgId) {
		return total30DayAssets.get(orgId);
	}

	public Long getTotalEvents(Long orgId) {
			return totalEvents.get(orgId);
	}
	
	public Long getTotal30DayEvents(Long orgId) {
		return total30DayEvents.get(orgId);
	}

	public ActiveSession getLastActiveSession(Long orgId) {
		return lastActiveSessions.get(orgId);
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public String getSortDirection() {
		return sortDirection;
	}
}
