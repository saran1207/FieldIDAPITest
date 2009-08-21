package com.n4systems.fieldidadmin.actions;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;

import com.n4systems.handlers.creator.BaseSystemSetupDataCreateHandlerImpl;
import com.n4systems.handlers.creator.BaseSystemStructureCreateHandler;
import com.n4systems.handlers.creator.BaseSystemStructureCreateHandlerImpl;
import com.n4systems.handlers.creator.BaseSystemTenantStructureCreateHandlerImpl;
import com.n4systems.handlers.creator.PrimaryOrgCreateHandler;
import com.n4systems.handlers.creator.PrimaryOrgCreateHandlerImpl;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.inspectiontypegroup.InspectionTypeGroupSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.producttype.ProductTypeSaver;
import com.n4systems.model.serialnumbercounter.SerialNumberCounterSaver;
import com.n4systems.model.stateset.StateSetSaver;
import com.n4systems.model.tagoption.TagOptionSaver;
import com.n4systems.model.tenant.OrganizationSaver;
import com.n4systems.model.tenant.SetupDataLastModDatesSaver;
import com.n4systems.model.tenant.TenantSaver;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureFactory;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitch;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.services.TenantCache;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DataUnit;
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
	private File logoFile;
	private File certificateLogoFile;
	private ExtendedFeature[] availableExtendedFeatures = ExtendedFeature.values();
	private Map<String, Boolean> extendedFeatures = new HashMap<String, Boolean>();

	public void prepare() throws Exception {
		if (id != null) {
			tenant = TenantCache.getInstance().findTenant(id); 
			primaryOrg = TenantCache.getInstance().findPrimaryOrg(id);
			
			for (ExtendedFeature feature : primaryOrg.getExtendedFeatures()) {
				extendedFeatures.put(feature.name(), true);
			}
		} else {
			tenant = new Tenant();
			primaryOrg = new PrimaryOrg();
		}

	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	@SkipValidation
	public String doFormInput() {
		return INPUT;
	}
	
	public String doSave() throws Exception {
		TenantSaver tenantSaver = new TenantSaver();
		OrganizationSaver orgSaver = new OrganizationSaver();
		
		Transaction transaction = PersistenceManager.startTransaction();
		
		try {
			
			moveMainLogo();
			moveCertLogo();
			
			if (id != null) {	
				updateTenant(tenantSaver, orgSaver, transaction);
			} else {					
				createTenant(tenantSaver, orgSaver, transaction);
			}
			
			processExtendedFeatures(orgSaver, transaction);
			
			PersistenceManager.finishTransaction(transaction);
			
		} catch (Exception e) {
			PersistenceManager.rollbackTransaction(transaction);
			
			logger.error("Failed creating tenant", e);
			addActionError("Failed Creating Tenant: " + e.getMessage());
			return INPUT;
			
		}
		
		return SUCCESS;
	}

	private void createTenant(TenantSaver tenantSaver, OrganizationSaver orgSaver, Transaction transaction) throws ParseException {
		tenantSaver.save(transaction, tenant);
		
		BaseSystemStructureCreateHandler createHandler = new BaseSystemStructureCreateHandlerImpl(new BaseSystemTenantStructureCreateHandlerImpl(new SetupDataLastModDatesSaver(), new SerialNumberCounterSaver()), new BaseSystemSetupDataCreateHandlerImpl(new TagOptionSaver(), new ProductTypeSaver(), new InspectionTypeGroupSaver(), new StateSetSaver()));
		createHandler.forTenant(tenant).create(transaction);
		
		PrimaryOrgCreateHandler orgCreateHandler = new PrimaryOrgCreateHandlerImpl(new OrganizationSaver(), new UserSaver());
		orgCreateHandler.forTenant(tenant).forAccountInfo(null).create(transaction);
		
		
	}

	private void updateTenant(TenantSaver tenantSaver, OrganizationSaver orgSaver, Transaction transaction) {
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
	
	private void processExtendedFeatures(OrganizationSaver orgSaver, Transaction transaction) {
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




	
	private void setCommonUserAttribs(UserBean user, int permissions) {
		user.setTenant(tenant);
		user.setOrganization(primaryOrg);
		user.setTimeZoneID("United States:New York - New York");
		user.setActive(true);
		user.setPermissions(permissions);
	}

	private void createSystemAccount(Transaction transaction) {
		UserBean user = new UserBean();
		setCommonUserAttribs(user, Permissions.SYSTEM);
		user.setSystem(true);
		user.setUserID(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_USERNAME));
		user.setHashPassword(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_PASSWORD));
		user.setEmailAddress(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_ADDRESS));
		user.setFirstName("N4");
		user.setLastName("Admin");
		
		UserSaver saver = new UserSaver();
		saver.save(transaction, user);
	}
	
	private void createAdminAccount(Transaction transaction) {
		adminUser.setAdmin(true);
		setCommonUserAttribs(adminUser, Permissions.ADMIN);

		UserSaver saver = new UserSaver();
		saver.save(transaction, adminUser);
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

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant manufacture) {
		this.tenant = manufacture;
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
	
	public PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}

	public void setPrimaryOrg(PrimaryOrg primaryOrg) {
		this.primaryOrg = primaryOrg;
	}

	public Collection<PrimaryOrg> getPrimaryOrgs() {
		return TenantCache.getInstance().findAllPrimaryOrgs();
	}

	public File getLogoFile() {
		return logoFile;
	}

	public void setLogoFile(File logoFile) {
		this.logoFile = logoFile;
	}

	public File getCertificateLogoFile() {
		return certificateLogoFile;
	}

	public void setCertificateLogoFile(File certificateLogoFile) {
		this.certificateLogoFile = certificateLogoFile;
	}

	public String getOtherDateFormat() {
		return (primaryOrg.getDateFormat() != null) ? DateHelper.java2Unix(primaryOrg.getDateFormat()) : "";
	}

	public String getFormattedDate() {
		return (primaryOrg.getDateFormat() != null) ? (new SimpleDateFormat(primaryOrg.getDateFormat())).format(new Date()) : "";
	}

	public ExtendedFeature[] getAvailableExtendedFeatures() {
		return availableExtendedFeatures;
	}

	@SuppressWarnings("unchecked")
	public Map getExtendedFeatures() {
		return extendedFeatures;
	}

	@SuppressWarnings("unchecked")
	public void setExtendedFeatures(Map extendedFeatures) {
		this.extendedFeatures = extendedFeatures;
	}
	
	public void setDiskSpaceMB(Long diskSpace) {
		if (diskSpace == -1) {
			primaryOrg.getLimits().setDiskSpaceUnlimited();
		} else {
			primaryOrg.getLimits().setDiskSpace(DataUnit.BYTES.convertFrom(diskSpace, DataUnit.MEBIBYTES));
		}	
	}

	public Long getDiskSpaceMB() {
		Long diskSpace;
		if (primaryOrg.getLimits().isDiskSpaceUnlimited()) {
			diskSpace = -1L;
		} else {
			diskSpace = DataUnit.BYTES.convertTo(primaryOrg.getLimits().getDiskSpace(), DataUnit.MEBIBYTES);
		}
		
		return diskSpace;
	}
}
