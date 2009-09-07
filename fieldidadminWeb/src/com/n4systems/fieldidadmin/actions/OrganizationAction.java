package com.n4systems.fieldidadmin.actions;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
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
		OrgSaver orgSaver = new OrgSaver();
		
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

	private void createTenant(TenantSaver tenantSaver, OrgSaver orgSaver, Transaction transaction) throws ParseException {
		tenantSaver.save(transaction, tenant);
		
		primaryOrg.setTenant(tenant);
		primaryOrg.setUsingSerialNumber(true);
		primaryOrg.setCertificateName(primaryOrg.getName());
		primaryOrg.setDefaultTimeZone("United States:New York - New York");
		
		orgSaver.save(transaction, primaryOrg);
	}

	private void updateTenant(TenantSaver tenantSaver, OrgSaver orgSaver, Transaction transaction) {
		tenantSaver.update(transaction, tenant);
		orgSaver.update(transaction, primaryOrg);
	}

	private void moveMainLogo() throws IOException {
		if (logoFile != null && logoFile.exists()) {
			File privateLogoPath = PathHandler.getTenantLogo(tenant);

			if (!privateLogoPath.getParentFile().exists()) {
				privateLogoPath.getParentFile().mkdirs();
			}

			FileUtils.copyFile(logoFile, privateLogoPath);
		}
	}

	private void moveCertLogo() throws IOException {
		if (certificateLogoFile != null && certificateLogoFile.exists()) {
			File privateLogoPath = PathHandler.getCertificateLogo(primaryOrg);

			if (!privateLogoPath.getParentFile().exists()) {
				privateLogoPath.getParentFile().mkdirs();
			}

			FileUtils.copyFile(certificateLogoFile, privateLogoPath);
		}
	}
	
	//private void createSystemAccount(Transaction transaction) {
	//	UserBean user = new UserBean();
	
	private void processExtendedFeatures(OrgSaver orgSaver, Transaction transaction) {
		for (Entry<String, Boolean> inputFeature : extendedFeatures.entrySet()) {
			processFeature(inputFeature.getKey(), inputFeature.getValue());
		}
		orgSaver.update(transaction, primaryOrg);
	}

	private void processFeature(String featureName, boolean featureOn) {
		try {
			ExtendedFeature feature = ExtendedFeature.valueOf(featureName);
			ExtendedFeatureSwitch featureSwitch = ExtendedFeatureFactory.getSwitchFor(feature, primaryOrg);
			
			if (featureOn) {
				featureSwitch.enableFeature();
			} else {
				featureSwitch.disableFeature();
			}
		} catch (IllegalArgumentException e) {
			addFieldError("extendedFeatures", "incorrect type of extended feature");
		}
	}

	private void createDefaultTagOptionManufacture(TagOption.OptionKey optionKey, Transaction transaction) {
		TagOption tagOption = new TagOption();

		tagOption.setTenant(tenant);
		tagOption.setKey(optionKey);

		TagOptionSaver saver = new TagOptionSaver();
		
		saver.save(transaction, tagOption);
	}

	private void createDefaultProductType(String itemNumber, Transaction transaction) {
		ProductType productType = new ProductType();

		productType.setTenant(tenant);
		productType.setName(itemNumber);
		
		ProductTypeSaver saver = new ProductTypeSaver();
		saver.save(transaction, productType);
	}

	private Date getFirstDayOfYear() throws ParseException {
		SimpleDateFormat fullFormat = new SimpleDateFormat("dd-MM-yyyy");
		String year = new SimpleDateFormat("yyyy").format(new Date());
		return fullFormat.parse("01-01-" + year);
	}

	private void createDefaultSerialNumberCounter(Long counter, String decimalFormat, Long daysToReset, Date lastReset, Transaction transaction) {
		SerialNumberCounterBean serialNumberCounter = new SerialNumberCounterBean();
		serialNumberCounter.setTenant(tenant);
		serialNumberCounter.setCounter(counter);
		serialNumberCounter.setDecimalFormat(decimalFormat);
		serialNumberCounter.setDaysToReset(daysToReset);
		serialNumberCounter.setLastReset(lastReset);

		SerialNumberCounterSaver saver = new SerialNumberCounterSaver();
		saver.save(transaction, serialNumberCounter);
	}
	
	private void setCommonUserAttribs(UserBean user, int permissions) {
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
