package com.n4systems.fieldid.actions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.service.amazon.S3Service;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.subscriptions.AccountHelper;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.UpgradePackageFilter;
import com.n4systems.model.tenant.extendedfeatures.ExendedFeatureToggler;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.util.StringListingPair;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.UrlValidator;

@UserPermissionFilter(userRequiresOneOf = { Permissions.ManageSystemConfig })
public class SystemSettingsCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;

	private AccountHelper accountHelper;

	private PrimaryOrg primaryOrg;

	private String dateFormat;
    private String identifierFormat;

	private String webSite;
	private File uploadedImage;
	private String imageDirectory;
	private boolean newImage = false;
	private boolean assignedTo = false;
	private boolean proofTestIntegration=false;
	private boolean manufacturerCertificate=false;
	private boolean gpsCapture = false;
	private TransactionManager transactionManager;
	
	@Autowired
	private TenantSettingsService tenantSettingsService;

    @Autowired
    private S3Service s3Service;

	public SystemSettingsCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		primaryOrg = getPrimaryOrg();
		assignedTo = primaryOrg.hasExtendedFeature(ExtendedFeature.AssignedTo);
		proofTestIntegration = primaryOrg.hasExtendedFeature(ExtendedFeature.ProofTestIntegration);
		manufacturerCertificate = primaryOrg.hasExtendedFeature(ExtendedFeature.ManufacturerCertificate);
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		initMemberFields();
	}

	@Override
	protected void postInit() {
		super.postInit();
		accountHelper = new AccountHelper(getCreateHandlerFactory().getSubscriptionAgent(), getPrimaryOrg(), getNonSecureLoaderFactory().createSignUpPackageListLoader());
	}

	@SkipValidation
	public String doEdit() {
		dateFormat = primaryOrg.getDateFormat();
		assignedTo = primaryOrg.hasExtendedFeature(ExtendedFeature.AssignedTo);
        identifierFormat = primaryOrg.getIdentifierFormat();
		proofTestIntegration = primaryOrg.hasExtendedFeature(ExtendedFeature.ProofTestIntegration);
		manufacturerCertificate = primaryOrg.hasExtendedFeature(ExtendedFeature.ManufacturerCertificate);
		gpsCapture = getTenant().getSettings().isGpsCapture();
		webSite = primaryOrg.getWebSite();
		return SUCCESS;
	}
	
	@SkipValidation
	public String doEditQuickSetupWizard() {
		dateFormat = primaryOrg.getDateFormat();
		webSite = primaryOrg.getWebSite();
		return SUCCESS;
	}

	public String doUpdate() {

		Transaction transaction = transactionManager().startTransaction();

		try {
			updateExtendedFeatures(transaction);
			updateDateFormat();
            updateSerialNumberFormat();
            updateTenantSettings();
			save(transaction);
			transactionManager().finishTransaction(transaction);
			addFlashMessageText("message.system_settings_updated");
		} catch (Exception e) {
			addActionErrorText("error.updating_system_settings");
			return ERROR;
		} finally {
			clearCachedValues();
		}
		return SUCCESS;
	}

    private void updateTenantSettings() {
    	tenantSettingsService.updateGpsCapture(isGpsCapture());    	
	}

	@SkipValidation
	public String doUpdateBranding() {

		Transaction transaction = transactionManager().startTransaction();

		try {
			updateBranding();
			save(transaction);
			transactionManager().finishTransaction(transaction);
			addFlashMessageText("message.system_settings_updated");
		} catch (Exception e) {
			addActionErrorText("error.updating_system_settings");
			return ERROR;
		} finally {
			clearCachedValues();
		}
		return SUCCESS;
	}
	
	
	public String doUpdateDateAndBranding() {

		Transaction transaction = transactionManager().startTransaction();

		try {
			updateDateFormat();
			updateBranding();
			save(transaction);
			transactionManager().finishTransaction(transaction);
			addFlashMessageText("message.system_settings_updated");
		} catch (Exception e) {
			addActionErrorText("error.updating_system_settings");
			return ERROR;
		} finally {
			clearCachedValues();
		}
		return SUCCESS;
	}

	private void save(Transaction transaction){
		new OrgSaver().update(transaction, getPrimaryOrg());
	}
	
	private void updateExtendedFeatures(Transaction transaction) throws Exception {
		PrimaryOrg primaryOrg = getPrimaryOrg();
		new ExendedFeatureToggler(ExtendedFeature.ProofTestIntegration, proofTestIntegration).applyTo(primaryOrg, transaction);
		new ExendedFeatureToggler(ExtendedFeature.AssignedTo, assignedTo).applyTo(primaryOrg, transaction);
		new ExendedFeatureToggler(ExtendedFeature.ManufacturerCertificate, manufacturerCertificate).applyTo(primaryOrg, transaction);
	}

	private void updateDateFormat() throws Exception {
		getPrimaryOrg().setDateFormat(dateFormat);
	}

    private void updateSerialNumberFormat() {
        getPrimaryOrg().setIdentifierFormat(identifierFormat);
    }

	private void updateBranding() throws Exception {
		if (getSecurityGuard().isBrandingEnabled()) {
			processLogo();
			getPrimaryOrg().setWebSite(webSite);
		}
	}
	
	private TransactionManager transactionManager() {
		if (transactionManager == null) {
			transactionManager = new FieldIdTransactionManager();
		}
		return transactionManager;
	}

	private void clearCachedValues() {
		refreshSessionUser();
	}

	private void processLogo() throws IOException {
		if (newImage && imageDirectory != null && imageDirectory.length() != 0) {
            File tmpDirectory = PathHandler.getTempRoot();

            uploadedImage = new File(tmpDirectory.getAbsolutePath() + '/' + imageDirectory);

            s3Service.uploadBrandingLogo(uploadedImage);
		}
	}

	public String getWebSite() {
		return webSite;
	}

	@UrlValidator(key = "error.web_site_must_be_a_url", message = "")
	public void setWebSite(String webSite) {
		if (webSite == null || webSite.trim().length() == 0) {
			this.webSite = null;
		} else {
			this.webSite = webSite;
		}
	}

	public File getUploadedImage() {
		return uploadedImage;
	}

	public void setUploadedImage(File uploadedImage) {
		this.uploadedImage = uploadedImage;
	}

	public String getImageDirectory() {
		return imageDirectory;
	}

	public void setImageDirectory(String imageDirectory) {
		this.imageDirectory = imageDirectory;
	}

	public boolean isNewImage() {
		return newImage;
	}

	public boolean isAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(boolean assignedTo) {
		this.assignedTo = assignedTo;
	}

	public void setNewImage(boolean newImage) {
		this.newImage = newImage;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public List<StringListingPair> getDateFormats() {
		List<StringListingPair> dateFormats = new ArrayList<StringListingPair>();
		dateFormats.add(new StringListingPair("MM/dd/yy", "MM/dd/yy (12/31/99)"));
		dateFormats.add(new StringListingPair("dd/MM/yy", "dd/MM/yy (31/12/99)"));
		dateFormats.add(new StringListingPair("MM/dd/yyyy", "MM/dd/yyyy (12/31/1999)"));
		dateFormats.add(new StringListingPair("dd/MM/yyyy", "dd/MM/yyyy (31/12/1999)"));
		dateFormats.add(new StringListingPair("yyyy-MM-dd", "yyyy-MM-dd (1999-12-31)"));

		return dateFormats;
	}

	@FieldExpressionValidator(message = "", key = "error.date_format_not_valid", expression = "(validDateFormat == true)", fieldName = "dateFormat")
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public boolean isValidDateFormat() {
		try {
			@SuppressWarnings("unused")
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getDateFormat());
		} catch (IllegalArgumentException e) {
			return false;
		}

		return true;
	}

	public UpgradePackageFilter currentPackageFilter() {
		return accountHelper.currentPackageFilter();
	}

	public boolean isProofTestIntegration() {
		return proofTestIntegration;
	}

	public void setProofTestIntegration(boolean proofTestIntegration) {
		this.proofTestIntegration = proofTestIntegration;
	}

    public String getIdentifierFormat() {
        return identifierFormat;
    }

    public void setIdentifierFormat(String identifierFormat) {
        this.identifierFormat = identifierFormat;
    }

	public boolean isManufacturerCertificate() {
		return manufacturerCertificate;
	}

	public void setManufacturerCertificate(boolean manufacturerCertificate) {
		this.manufacturerCertificate = manufacturerCertificate;
	}
	
	public boolean isGpsCapture() {
		return gpsCapture;
	}

	public void setGpsCapture(boolean gpsCapture) {
		this.gpsCapture = gpsCapture;
	}
}
