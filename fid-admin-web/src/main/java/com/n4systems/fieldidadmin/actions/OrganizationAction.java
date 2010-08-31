package com.n4systems.fieldidadmin.actions;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.extendedfeatures.ToggleExendedFeatureMethod;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.services.TenantCache;
import com.n4systems.services.limiters.TenantLimitService;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DateHelper;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@Validations
public class OrganizationAction extends AbstractAdminAction implements Preparable {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(OrganizationAction.class);
	
	private Long id;
	private Tenant tenant;
	private PrimaryOrg primaryOrg;
	private User adminUser = new User();
	private String title;
	private String note;
	private Map<String, Boolean> extendedFeatures = new HashMap<String, Boolean>();	

	public void prepare() throws Exception {
		if (id != null) {
			tenant = TenantCache.getInstance().findTenant(id); 
			primaryOrg = TenantCache.getInstance().findPrimaryOrg(id);
			for (ExtendedFeature feature : primaryOrg.getExtendedFeatures()) {
				extendedFeatures.put(feature.name(), true);
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
		
		Transaction transaction = PersistenceManager.startTransaction();
		
		try {
			if (primaryOrg.getId() != null) {
				processExtendedFeatures(transaction);
			}
			
			orgSaver.update(transaction, primaryOrg);
			
			
			PersistenceManager.finishTransaction(transaction);
			
			TenantCache.getInstance().reloadPrimaryOrg(primaryOrg.getTenant().getId());
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
		user.setActive(true);
		user.setPermissions(Permissions.SYSTEM);
		user.setSystem(true);
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

	public Collection<PrimaryOrg> getPrimaryOrgs() {
		return TenantCache.getInstance().findAllPrimaryOrgs();
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
	
	public Long getUsers() {
		return primaryOrg.getLimits().getUsers();
	}
	
	public void setUsers(Long users) {
		primaryOrg.getLimits().setUsers(users);
	}
	
	public Long getSecondaryOrgs() {
		return primaryOrg.getLimits().getSecondaryOrgs();
	}
	
	public void setSecondaryOrgs(Long secondaryOrgs) {
		primaryOrg.getLimits().setSecondaryOrgs(secondaryOrgs);
	}
}
