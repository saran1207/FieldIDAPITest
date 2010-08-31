package com.n4systems.fieldid.actions;

import java.io.File;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.orgs.SecondaryOrgByNameLoader;
import com.n4systems.model.orgs.SecondaryOrgPaginatedLoader;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import com.n4systems.util.timezone.Region;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class OrganizationalCrud extends AbstractCrud implements HasDuplicateValueValidator {
	private static final Logger logger = Logger.getLogger(OrganizationalCrud.class);
	private static final long serialVersionUID = 1L;
	
	private InternalOrg organization; 
	private Pager<SecondaryOrg> page;

	private File certImage;
	private String certImageDirectory;
	private boolean removeImage = false;
	private boolean newImage = false;
	private Country country;
	private Region region;
	
	public OrganizationalCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@Override
	protected void initMemberFields() {
		// this action can only create secondary orgs
		organization = new SecondaryOrg();
		
		String primaryOrgTimeZone = getPrimaryOrg().getDefaultTimeZone();
		country = CountryList.getInstance().getCountryByFullName(primaryOrgTimeZone);
		region = CountryList.getInstance().getRegionByFullId(primaryOrgTimeZone);
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		PrimaryOrg primaryOrg = getPrimaryOrg();
		
		// Figure our if we're editing the PrimaryOrg or a SubOrg 
		if (primaryOrg.getId().equals(uniqueId)) {
			organization = primaryOrg;
		} else {
			// we arn't, load a secondary
			FilteredIdLoader<SecondaryOrg> loader = new FilteredIdLoader<SecondaryOrg>(getSecurityFilter(), SecondaryOrg.class);
			loader.setId(uniqueId);
			organization = loader.load();
		}
		
		country = CountryList.getInstance().getCountryByFullName(organization.getDefaultTimeZone());
		region = CountryList.getInstance().getRegionByFullId(organization.getDefaultTimeZone());
	}

	private void testRequiredEntities(boolean existing) {
		if (organization == null || (existing && organization.isNew())) {
			addActionErrorText("error.no_organization");
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	public String doList() {
		SecondaryOrgPaginatedLoader loader = getLoaderFactory().createSecondaryOrgPaginatedLoader();
		loader.setPage(getCurrentPage()).setPageSize(Constants.PAGE_SIZE);
		page = loader.load();
		
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
			PrimaryOrg primaryOrg = getPrimaryOrg();
			
			// in a create, the organization will always be a secondary org
			SecondaryOrg secondaryOrg = (SecondaryOrg)organization;
			secondaryOrg.setTenant(primaryOrg.getTenant());
			secondaryOrg.setPrimaryOrg(primaryOrg);
			
			OrgSaver saver = new OrgSaver();
			saver.save(secondaryOrg);

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
		
		File ouCertLogo = PathHandler.getCertificateLogo(organization, false);
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
		File ouCertLogo = PathHandler.getCertificateLogo(organization, false);
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
			OrgSaver saver = new OrgSaver();
			saver.update(organization);
			
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

	public String getCertificateName() {
		return organization.getCertificateName();
	}

	public String getDisplayName() {
		return organization.getName();
	}
	
	public void setAddressInfo(AddressInfo addressInfo) {
		organization.setAddressInfo(addressInfo);
	}

	public void setCertificateName(String certificateName) {
		organization.setCertificateName(certificateName);
	}

	@RequiredStringValidator(message="", key="error.namerequired")
	@StringLengthFieldValidator( type=ValidatorType.FIELD, message = "" , key = "errors.organization_name_too_long", maxLength="255")
	@CustomValidator(type="uniqueValue", message = "", key="errors.organization_name_used")
	public void setDisplayName(String displayName) {
		organization.setName(displayName);
	}

	public Pager<SecondaryOrg> getPage() {
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

	public String getWebSite() {
		return (organization.isPrimary()) ? ((PrimaryOrg)organization).getWebSite() : null;
	}
	
	public void setWebSite(String webSite) {
		if (organization.isPrimary()) {
			((PrimaryOrg)organization).setWebSite(webSite);
		}
	}
	
	public boolean duplicateValueExists(String formValue) {
		if (formValue != null) {
			SecondaryOrgByNameLoader loader  = getLoaderFactory().createSecondaryOrgByNameLoader();
			loader.setName(formValue.trim());
			loader.setFilterOutId(uniqueID);
			
			SecondaryOrg secOrg = loader.load();
			return (secOrg != null);
		} 
		return false;
	}
	
	public boolean isPrimary() {
		return organization.isPrimary();
	}
	
	public SortedSet<? extends Listable<String>> getCountries() {
		return CountryList.getInstance().getCountries();
	}

	public SortedSet<? extends Listable<String>> getTimeZones() {
		return (country != null) ? country.getRegions() : new TreeSet<Listable<String>>();
	}
	
	public String getCountryId() {
		return country.getId();
	}
	
	public void setCountryId(String countryId) {
		country = CountryList.getInstance().getCountryById(countryId);
	}
	
	public String getTimeZoneID() {
		return region.getId();
	}
	
	public void setTimeZoneID(String regionId) {
		if (country != null) {
			region = country.getRegionById(regionId);
			organization.setDefaultTimeZone(country.getFullName(region));
		}
	}
}
