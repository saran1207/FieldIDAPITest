package com.n4systems.fieldid.actions;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.NonTenantOrganization;
import com.n4systems.model.Organization;
import com.n4systems.model.OrganizationalUnitType;
import com.n4systems.model.TenantOrganization;
import com.n4systems.reporting.PathHandler;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class OrganizationalCrud extends AbstractCrud implements HasDuplicateValueValidator {
	private static final Logger logger = Logger.getLogger(OrganizationalCrud.class);
	private static final long serialVersionUID = 1L;

	private Organization organization;
	private Pager<Organization> page;

	private File certImage;
	private String certImageDirectory;
	private boolean removeImage = false;
	private boolean newImage = false;
	
	
	public OrganizationalCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@Override
	protected void initMemberFields() {
		organization = new NonTenantOrganization();

	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		QueryBuilder<Organization> query = new QueryBuilder<Organization>(Organization.class, getSecurityFilter().setTargets("tenant.id"));
		query.addSimpleWhere("id", uniqueId);
		organization = persistenceManager.find(query);
	}

	private void testRequiredEntities(boolean existing) {
		if (organization == null || (existing && organization.isNew())) {
			addActionErrorText("error.no_organization");
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	public String doList() {
		try {
			QueryBuilder<Organization> qBuilder = new QueryBuilder<Organization>(Organization.class, getSecurityFilter().setTargets("tenant.id"));
			qBuilder.setSimpleSelect().addOrder("displayName");
			page = persistenceManager.findAllPaged(qBuilder, getCurrentPage(), Constants.PAGE_SIZE);
		} catch (InvalidQueryException iqe) {
			logger.error(getLogLinePrefix() + "bad search query", iqe);
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		try {
			
			TenantOrganization tenant = persistenceManager.find(TenantOrganization.class, getTenantId(), "children");
			organization.setType(OrganizationalUnitType.DIVISION);
			organization.setTenant(tenant);
			
			// only one level deep right now so we always attach directly to the tenant
			tenant.attachChild(organization);
			persistenceManager.save(organization);
			persistenceManager.update(tenant);
			processImage();
			refreshSessionUser();
			addFlashMessageText("message.organizationsaved");
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + "could not create organization", e);
			addActionErrorText("error.savingorganization");
		}
		return SUCCESS;
	}

	private void processImage() {
		
		File ouCertLogo = PathHandler.getCertificateLogo(organization);
		if (removeImage) {
			if (ouCertLogo.exists()) {
				ouCertLogo.delete();
			}
		}
		if (newImage == true && certImageDirectory != null && certImageDirectory.length() != 0) {
			try {
				if (ouCertLogo.exists()) {
					ouCertLogo.delete();
				}
				
				File tmpDirectory = PathHandler.getTempRoot();
				File uploadedImage = new File(tmpDirectory.getAbsolutePath() + '/' + certImageDirectory);
				
				FileUtils.copyFile( uploadedImage, ouCertLogo );
				uploadedImage.delete();
				
			} catch (IOException e) {
				logger.error("Could not save logo file",e);
			}
		}
	
		
	}
	
	private String certImageExists() {
		File ouCertLogo = PathHandler.getCertificateLogo(organization);
		return (ouCertLogo.exists()) ? ouCertLogo.getName() : null; 
	}
	
	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		certImageDirectory = certImageExists();
		
		return SUCCESS;
	}

	public String doUpdate() {
		testRequiredEntities(true);
		try {
			persistenceManager.update(organization);
			processImage();
			refreshSessionUser();
			addFlashMessageText("message.organizationsaved");
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + "could not update organization", e);
			addActionErrorText("error.savingorganization");
		}
		return SUCCESS;
	}

	public AddressInfo getAddressInfo() {
		return organization.getAddressInfo();
	}

	public String getAdminEmail() {
		return organization.getAdminEmail();
	}

	public String getCertificateName() {
		return organization.getCertificateName();
	}

	public String getDisplayName() {
		return organization.getDisplayName();
	}

	
	public void setAddressInfo(AddressInfo addressInfo) {
		organization.setAddressInfo(addressInfo);
	}

	@RequiredStringValidator( type=ValidatorType.FIELD, message="", key="error.emailrequired" )
	@EmailValidator( type=ValidatorType.FIELD, message="", key="error.emailformat")
	public void setAdminEmail(String adminEmail) {
		organization.setAdminEmail(adminEmail);
	}

	public void setCertificateName(String certificateName) {
		organization.setCertificateName(certificateName);
	}

	
	@RequiredStringValidator(message="", key="error.namerequired")
	@StringLengthFieldValidator( type=ValidatorType.FIELD, message = "" , key = "errors.organization_name_too_long", maxLength="255")
	@CustomValidator(type="uniqueValue", message = "", key="errors.organization_name_used")
	public void setDisplayName(String displayName) {
		organization.setDisplayName(displayName);
	}


	public Pager<Organization> getPage() {
		return page;
	}

	public File getCertImage() {
		return certImage;
	}

	@CustomValidator(type = "fileSizeValidator", message = "", key = "errors.file_too_large", parameters = { @ValidationParameter(name = "fileSize", value = "524288") })
	public void setCertImage(File certImage) {
		this.certImage = certImage;
	}

	public String getCertImageDirectory() {
		return certImageDirectory;
	}

	public void setCertImageDirectory(String certImageDirectory) {
		this.certImageDirectory = certImageDirectory;
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

	public boolean duplicateValueExists(String formValue) {
		if (formValue != null) {
			QueryBuilder<Organization> query = new QueryBuilder<Organization>(Organization.class, getSecurityFilter().prepareFor(Organization.class));
			query.setCountSelect().addWhere(Comparator.EQ, "displayName", "displayName", formValue.trim(), WhereParameter.IGNORE_CASE);
			if (uniqueID != null) {
				query.addWhere(Comparator.NE, "id", "id", uniqueID);
			}
			return 0 != persistenceManager.findCount(query);
		} 
		return false;
	}
}
