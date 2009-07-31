package com.n4systems.fieldidadmin.actions;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import rfid.ejb.entity.SerialNumberCounterBean;
import rfid.ejb.entity.UserBean;
import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.LegacyProductType;
import rfid.ejb.session.SerialNumberCounter;

import com.n4systems.ejb.SafetyNetworkManager;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.InspectorOrganization;
import com.n4systems.model.ManufacturerOrganization;
import com.n4systems.model.Organization;
import com.n4systems.model.ProductType;
import com.n4systems.model.SiteSafetyOraganization;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.Status;
import com.n4systems.model.TagOption;
import com.n4systems.model.TenantOrganization;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.tenant.SetupDataLastModDatesSaver;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureFactory;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitch;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DataUnit;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.StringListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;

public class OrganizationAction extends AbstractAdminAction implements Preparable {
	private static Logger logger = Logger.getLogger(OrganizationAction.class);

	private static final long serialVersionUID = 1L;
	private LegacyProductSerial productSerialManager;
	private LegacyProductType productTypeManager;
	private SerialNumberCounter serialNumberCounterManager;
	private Collection<TenantOrganization> organizations;
	private TenantOrganization tenant;
	private SafetyNetworkManager safetyNetworkManager;
	private String accountType;

	private File logoFile;
	private File certificateLogoFile;

	private ExtendedFeature[] availableExtendedFeatures = ExtendedFeature.values();
	private Map<String, Boolean> extendedFeatures = new HashMap<String, Boolean>();
	private Long id;
	private int page;
	private int size;
	
	private String userId;
	private String userPass;
	private String userFirstName;
	private String userLastName;

	public void prepare() throws Exception {
		if (id != null) {
			tenant = persistenceManager.find(TenantOrganization.class, id, "extendedFeatures");
			for (ExtendedFeature feature : tenant.getExtendedFeatures()) {
				extendedFeatures.put(feature.name(), true);
			}

			if (tenant.isManufacturer()) {
				accountType = "manufacturer";
			} else if (tenant.isInspector()) {
				accountType = "inspector";
			} else {
				accountType = "siteSafety";
			}
		} else {
			if ("manufacturer".equals(accountType)) {
				tenant = new ManufacturerOrganization();
			} else if ("inspector".equals(accountType)) {
				tenant = new InspectorOrganization();
			} else if ("siteSafety".equals(accountType)) {
				tenant = new SiteSafetyOraganization();
			}
		}

	}

	public String doList() {
		QueryBuilder<TenantOrganization> query = new QueryBuilder<TenantOrganization>(TenantOrganization.class);
		query.addOrder("displayName");
		try {
			organizations = persistenceManager.findAll(query);
		} catch (Exception e) {
			logger.error("could not load list", e);
		}
		return SUCCESS;
	}

	public String doFormInput() {
		return INPUT;
	}

	public String doSave() throws Exception {
		
		try {
			// deal with the logo files
			if (logoFile != null && logoFile.exists()) {
				File privateLogoPath = PathHandler.getTenantLogo(tenant);

				if (!privateLogoPath.getParentFile().exists()) {
					privateLogoPath.getParentFile().mkdirs();
				}

				FileUtils.copyFile(logoFile, privateLogoPath);
			}
			if (certificateLogoFile != null && certificateLogoFile.exists()) {
				File privateLogoPath = PathHandler.getCertificateLogo(tenant);

				if (!privateLogoPath.getParentFile().exists()) {
					privateLogoPath.getParentFile().mkdirs();
				}

				FileUtils.copyFile(certificateLogoFile, privateLogoPath);
			}
			if (id != null) {
				tenant.setId(id);
				persistenceManager.update(tenant);
			} else {
				tenant.setUsingSerialNumber(true);
				tenant.setFidAC(safetyNetworkManager.findNewFidAC());

				persistenceManager.save(tenant);
				tenant.setTenant(tenant);
				persistenceManager.update(tenant);

				//TODO move to a service for creating a tenant in the system.
				createDefaultSetupDataLastModDates();


				createDefaultTagOptionManufacture(tenant, TagOption.OptionKey.SHOPORDER);

				createDefaultProductType(tenant, "*");

				createSystemAccount();
				createAdminAccount();

				createDefaultStateSets();

				createDefaultInspectionTypeGroups();

				try {
					createDefaultSerialNumberCounter(tenant, 1L, "000000", 365L, getFirstDayOfYear());
				} catch (ParseException e) {
					logger.error("failed on default serial", e);
				}
			}
			processExtendedFeatures();
		} catch (Exception e) {
			logger.error("Failed creating tenant", e);
		}
		return Action.SUCCESS;
	}

	private void processExtendedFeatures() {
		for (Entry<String, Boolean> inputFeature : extendedFeatures.entrySet()) {
			processFeature(inputFeature.getKey(), inputFeature.getValue());
		}
	}

	private void processFeature(String featureName, boolean featureOn) {
		try {
			ExtendedFeature feature = ExtendedFeature.valueOf(featureName);
			ExtendedFeatureSwitch featureSwitch = ExtendedFeatureFactory.getSwitchFor(feature, tenant, persistenceManager);
			
			if (featureOn) {
				featureSwitch.enableFeature();
			} else {
				featureSwitch.disableFeature();
			}
		} catch (IllegalArgumentException e) {
			addFieldError("extendedFeatures", "incorrect type of extended feature");
		} catch (Exception e) {
			addFieldError("extendedFeatures", "could not setup");
			throw new RuntimeException("woo", e);
		}
	}

	

	private void createDefaultTagOptionManufacture(TenantOrganization tenant, TagOption.OptionKey optionKey) {
		TagOption tagOption = new TagOption();

		tagOption.setTenant(tenant);
		tagOption.setKey(optionKey);

		persistenceManager.save(tagOption);
	}

	private void createDefaultProductType(TenantOrganization manufacture, String itemNumber) {
		ProductType productType = new ProductType();

		productType.setTenant(manufacture);
		productType.setName(itemNumber);
		try {
			productTypeManager.persistProductType(productType);
		} catch (FileAttachmentException fileAttachmentException) {
			logger.error("failed to create default product type", fileAttachmentException);
		} catch (ImageAttachmentException imageAttachmentException) {
			logger.error("failed to create default product type", imageAttachmentException);
		}
	}

	private Date getFirstDayOfYear() throws ParseException {
		SimpleDateFormat fullFormat = new SimpleDateFormat("dd-MM-yyyy");
		String year = new SimpleDateFormat("yyyy").format(new Date());
		return fullFormat.parse("01-01-" + year);
	}

	private void createDefaultSerialNumberCounter(TenantOrganization tenant, Long counter, String decimalFormat, Long daysToReset, Date lastReset) {
		SerialNumberCounterBean serialNumberCounter = new SerialNumberCounterBean();

		serialNumberCounter.setTenant(tenant);
		serialNumberCounter.setCounter(counter);
		serialNumberCounter.setDecimalFormat(decimalFormat);
		serialNumberCounter.setDaysToReset(daysToReset);
		serialNumberCounter.setLastReset(lastReset);

		serialNumberCounterManager.persistSerialNumberCounter(serialNumberCounter);
	}
	
	private UserBean createAccount(boolean system, boolean admin, int permissions) {
		UserBean user = new UserBean();
		user.setTenant(tenant);
		user.setOrganization(tenant);
		user.setTimeZoneID("America/New_York");
		user.setActive(true);
		user.setSystem(system);
		user.setAdmin(admin);
		user.setPermissions(permissions);
		
		return user;
	}

	private void createSystemAccount() {
		UserBean user = createAccount(true, false, Permissions.SYSTEM);
		user.setUserID(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_USERNAME));
		user.setHashPassword(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_PASSWORD));
		user.setEmailAddress(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_ADDRESS));
		user.setFirstName("N4");
		user.setLastName("Admin");

		try {
			ServiceLocator.getUser().createUser(user);
		} catch (Exception e) {
			logger.error("Failed creating user", e);
		}
	}
	
	private void createAdminAccount() {
		UserBean user = createAccount(false, true, Permissions.ADMIN);
		user.setUserID(userId);
		user.assignPassword(userPass);
		user.setEmailAddress(tenant.getAdminEmail());
		user.setFirstName(userFirstName);
		user.setLastName(userLastName);

		try {
			ServiceLocator.getUser().createUser(user);
		} catch (Exception e) {
			logger.error("Failed creating user", e);
		}
	}

	private void createDefaultStateSets() {
		StateSet passFail = new StateSet();
		passFail.setName("Pass, Fail");
		passFail.setTenant(tenant);

		State pass = new State();
		pass.setTenant(tenant);
		pass.setButtonName("btn0");
		pass.setDisplayText("Pass");
		pass.setStatus(Status.PASS);

		passFail.getStates().add(pass);

		State fail = new State();
		fail.setTenant(tenant);
		fail.setButtonName("btn1");
		fail.setDisplayText("Fail");
		fail.setStatus(Status.FAIL);
		passFail.getStates().add(fail);

		persistenceManager.save(passFail);

		StateSet naPassFail = new StateSet();
		naPassFail.setName("NA, Pass, Fail");
		naPassFail.setTenant(tenant);

		State na = new State();
		na.setTenant(tenant);
		na.setButtonName("btn2");
		na.setDisplayText("NA");
		na.setStatus(Status.NA);
		naPassFail.getStates().add(na);

		pass = new State();
		pass.setTenant(tenant);
		pass.setButtonName("btn0");
		pass.setDisplayText("Pass");
		pass.setStatus(Status.PASS);
		naPassFail.getStates().add(pass);

		fail = new State();
		fail.setTenant(tenant);
		fail.setButtonName("btn1");
		fail.setDisplayText("Fail");
		fail.setStatus(Status.FAIL);
		naPassFail.getStates().add(fail);

		persistenceManager.save(naPassFail);

	}

	private void createDefaultInspectionTypeGroups() {
		InspectionTypeGroup visualInspection = new InspectionTypeGroup();
		visualInspection.setName("Visual Inspection");
		visualInspection.setReportTitle("Visual Inspection");
		visualInspection.setTenant(tenant);
		persistenceManager.save(visualInspection);

		InspectionTypeGroup prooftest = new InspectionTypeGroup();
		prooftest.setName("Proof Test");
		prooftest.setReportTitle("Proof Test");
		prooftest.setTenant(tenant);
		persistenceManager.save(prooftest);

		InspectionTypeGroup repair = new InspectionTypeGroup();
		repair.setName("Repair");
		repair.setReportTitle("Repair");
		repair.setTenant(tenant);
		persistenceManager.save(repair);
	}
	
	private void createDefaultSetupDataLastModDates() {
		SetupDataLastModDates setupModDates = new SetupDataLastModDates();
		setupModDates.setTenant(tenant);
		
		SetupDataLastModDatesSaver saver = new SetupDataLastModDatesSaver();
		saver.save(setupModDates);
	}

	public String doCreateUser() {
		createSystemAccount();
		return SUCCESS;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Organization getTenant() {
		return tenant;
	}

	public void setTenant(TenantOrganization manufacture) {
		this.tenant = manufacture;
	}

	public Collection<TenantOrganization> getOrganizations() {
		return organizations;
	}

	public LegacyProductSerial getProductSerialManager() {
		return productSerialManager;
	}

	public void setProductSerialManager(LegacyProductSerial productSerialManager) {
		this.productSerialManager = productSerialManager;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}


	public LegacyProductType getProductTypeManager() {
		return productTypeManager;
	}

	public void setProductTypeManager(LegacyProductType productTypeManager) {
		this.productTypeManager = productTypeManager;
	}

	public SerialNumberCounter getSerialNumberCounterManager() {
		return serialNumberCounterManager;
	}

	public void setSerialNumberCounterManager(SerialNumberCounter serialNumberCounterManager) {
		this.serialNumberCounterManager = serialNumberCounterManager;
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

	public String getAccountType() {

		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public List<StringListingPair> getTenantTypes() {
		return Organization.tenantTypes();
	}

	public String getOtherDateFormat() {
		return (tenant.getDateFormat() != null) ? DateHelper.java2Unix(tenant.getDateFormat()) : "";
	}

	public String getFormattedDate() {
		return (tenant.getDateFormat() != null) ? (new SimpleDateFormat(tenant.getDateFormat())).format(new Date()) : "";
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


	public void setSafetyNetworkManager(SafetyNetworkManager safetyNetworkManager) {
		this.safetyNetworkManager = safetyNetworkManager;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	
	public void setDiskSpaceMB(Long diskSpace) {
		if (diskSpace == -1) {
			tenant.getLimits().setDiskSpaceUnlimited();
		} else {
			tenant.getLimits().setDiskSpace(DataUnit.BYTES.convertFrom(diskSpace, DataUnit.MEBIBYTES));
		}	
	}

	public Long getDiskSpaceMB() {
		Long diskSpace;
		if (tenant.getLimits().isDiskSpaceUnlimited()) {
			diskSpace = -1L;
		} else {
			diskSpace = DataUnit.BYTES.convertTo(tenant.getLimits().getDiskSpace(), DataUnit.MEBIBYTES);
		}
		
		return diskSpace;
	}
}
