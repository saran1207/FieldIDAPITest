package com.n4systems.fieldid.actions;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.TenantOrganization;
import com.n4systems.reporting.PathHandler;
import com.opensymphony.xwork2.validator.annotations.UrlValidator;


public class SystemSettingsCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	
	private TenantOrganization myTenant;
	
	
	private File uploadedImage;

	private String imageDirectory;
	private boolean removeImage = false;
	private boolean newImage = false;
	
	public SystemSettingsCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	
	@Override
	protected void initMemberFields() {
		myTenant = persistenceManager.find(TenantOrganization.class, getTenantId());
		
	}
	
	@Override
	protected void loadMemberFields(Long uniqueId) {
		initMemberFields();
		
	}
	@SkipValidation
	public String doEdit() {
		File privateLogoPath = PathHandler.getTenantLogo(myTenant);
		if (privateLogoPath.exists()) {
			imageDirectory = privateLogoPath.getName();
		}
		return SUCCESS;
	}

	@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.Branding)
	public String doUpdate() {
		
		try {
			persistenceManager.update(myTenant, fetchCurrentUser());
			processLogo();
			refreshSessionUser();
			addFlashMessageText("message.system_settings_updated");
		} catch (Exception e) {
			addActionErrorText("error.updating_system_settings");
		}
		
		return SUCCESS;
	}


	private void processLogo() throws IOException {
		File privateLogoPath = PathHandler.getTenantLogo(myTenant);
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
		return myTenant.getWebSite();
	}

	@UrlValidator(key="error.web_site_must_be_a_url", message="")
	public void setWebSite(String webSite) {
		if (webSite == null || webSite.trim().length() == 0) {
			myTenant.setWebSite(null);
		} else {
			myTenant.setWebSite(webSite);
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

	public void setNewImage(boolean newImage) {
		this.newImage = newImage;
	}

}
