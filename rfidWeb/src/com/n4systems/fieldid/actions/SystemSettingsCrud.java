package com.n4systems.fieldid.actions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.subscriptions.AccountHelper;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.UpgradePackageFilter;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureFactory;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitch;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.services.TenantCache;
import com.n4systems.util.ListingPair;
import com.n4systems.util.StringListingPair;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.UrlValidator;

@UserPermissionFilter(userRequiresOneOf = { Permissions.ManageSystemConfig })
public class SystemSettingsCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;

	private AccountHelper accountHelper;

	private PrimaryOrg primaryOrg;

	private String dateFormat;
	private Long defaultVendorContext;

	private String webSite;
	private File uploadedImage;
	private String imageDirectory;
	private boolean removeImage = false;
	private boolean newImage = false;
	private boolean assignedTo = false;

	private TransactionManager transactionManager;

	public SystemSettingsCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		primaryOrg = getPrimaryOrg();

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
		defaultVendorContext = primaryOrg.getDefaultVendorContext();
		assignedTo = primaryOrg.hasExtendedFeature(ExtendedFeature.AssignedTo);
		webSite = primaryOrg.getWebSite();

		File privateLogoPath = PathHandler.getTenantLogo(primaryOrg.getTenant());
		if (privateLogoPath.exists()) {
			imageDirectory = privateLogoPath.getName();
		}
		return SUCCESS;
	}

	public String doUpdate() {

		Transaction transaction = transactionManager().startTransaction();

		try {
			updateAssignedToFeature(transaction);
			updateSystemSettings(transaction);

			transactionManager().finishTransaction(transaction);
			clearCachedValues();
		} catch (Exception e) {
			addActionErrorText("error.updating_system_settings");
			return ERROR;
		}

		return SUCCESS;
	}

	private void updateAssignedToFeature(Transaction transaction) throws Exception {
		PrimaryOrg updatedPrimaryOrg = processAssignedToSetting(transaction);
		new OrgSaver().update(transaction, updatedPrimaryOrg);
	}

	private void updateSystemSettings(Transaction transaction) throws Exception {
		PrimaryOrg updatedPrimaryOrg = processSystemSettings(transaction);
		new OrgSaver().update(transaction, updatedPrimaryOrg);
	}

	private PrimaryOrg processSystemSettings(Transaction transaction) throws IOException {
		PrimaryOrg primaryOrg = getPrimaryOrg();

		if (getSecurityGuard().isBrandingEnabled()) {
			processLogo();
			primaryOrg.setWebSite(webSite);
		}

		primaryOrg.setDateFormat(dateFormat);
		primaryOrg.setDefaultVendorContext(defaultVendorContext);

		addFlashMessageText("message.system_settings_updated");

		return primaryOrg;
	}

	private PrimaryOrg processAssignedToSetting(Transaction transaction) throws Exception {
		PrimaryOrg primaryOrg = getPrimaryOrg();
		ExtendedFeatureSwitch featureSwitchAssignedTo = ExtendedFeatureFactory.getSwitchFor(ExtendedFeature.AssignedTo, primaryOrg);

		if (assignedTo) {
			featureSwitchAssignedTo.enableFeature(transaction);
		} else {
			featureSwitchAssignedTo.disableFeature(transaction);
		}

		return primaryOrg;
	}

	private TransactionManager transactionManager() {
		if (transactionManager == null) {
			transactionManager = new FieldIdTransactionManager();
		}
		return transactionManager;
	}

	private void clearCachedValues() {
		TenantCache.getInstance().reloadPrimaryOrg(getPrimaryOrg().getTenant().getId());
		refreshSessionUser();
	}

	private void processLogo() throws IOException {
		File privateLogoPath = PathHandler.getTenantLogo(primaryOrg.getTenant());
		if (newImage == true && imageDirectory != null && imageDirectory.length() != 0) {
			if (!privateLogoPath.getParentFile().exists()) {
				privateLogoPath.getParentFile().mkdirs();
			}
			File tmpDirectory = PathHandler.getTempRoot();
			uploadedImage = new File(tmpDirectory.getAbsolutePath() + '/' + imageDirectory);

			FileUtils.copyFile(uploadedImage, privateLogoPath);
		} else if (removeImage && privateLogoPath.exists()) {
			privateLogoPath.delete();
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

	public boolean isRemoveImage() {
		return removeImage;
	}

	public void setRemoveImage(boolean removeImage) {
		this.removeImage = removeImage;
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

	public Long getDefaultVendorContext() {
		return defaultVendorContext;
	}

	@FieldExpressionValidator(message = "", key = "error.selected_company_is_not_one_of_your_vendors", expression = "(validVendor == true)")
	public void setDefaultVendorContext(Long defaultVendorContext) {
		if (defaultVendorContext == null || defaultVendorContext.equals(getPrimaryOrg().getId())) {
			this.defaultVendorContext = null;
		} else {
			this.defaultVendorContext = defaultVendorContext;
		}
	}

	public boolean isValidVendor() {
		if (defaultVendorContext == null) {
			return true;
		}

		for (ListingPair vendor : getVendorContextList()) {
			if (vendor.getId().equals(defaultVendorContext)) {
				return true;
			}
		}
		return false;
	}
}
