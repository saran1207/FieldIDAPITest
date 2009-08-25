package com.n4systems.fieldidadmin.actions;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.OrganizationSaver;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.services.TenantCache;
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
	private UserBean adminUser = new UserBean();

	public void prepare() throws Exception {
		if (id != null) {
			tenant = TenantCache.getInstance().findTenant(id); 
			primaryOrg = TenantCache.getInstance().findPrimaryOrg(id);
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
		OrganizationSaver orgSaver = new OrganizationSaver();
		
		Transaction transaction = PersistenceManager.startTransaction();
		
		try {
			
			
			orgSaver.update(transaction, primaryOrg);
			
			
			PersistenceManager.finishTransaction(transaction);
			
		} catch (Exception e) {
			PersistenceManager.rollbackTransaction(transaction);
			
			logger.error("Failed creating tenant", e);
			addActionError("Failed Creating Tenant: " + e.getMessage());
			return INPUT;
			
		}
		
		return SUCCESS;
	}



	
	private void createSystemAccount(Transaction transaction) {
		UserBean user = new UserBean();
		user.setTenant(tenant);
		user.setOrganization(primaryOrg);
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

	public UserBean getAdminUser() {
		return adminUser;
	}
	
	@FieldExpressionValidator(expression="(adminUser.userID != 'n4systems')", message = "Admin user cannot have a userid of n4systems.")
	public void setAdminUser(UserBean adminUser) {
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

	

}
