package com.n4systems.fieldid.actions.asset;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.helpers.ConnectedEntityLoader;
import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.UploadFileSupport;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasAssetDescriptionTemplateValidator;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.UnitOfMeasure;
import com.n4systems.model.utils.CleanAssetTypeFactory;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.util.AssetTypeRemovalSummary;
import com.n4systems.util.ContentTypeUtil;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.UrlValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Validation
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class AssetTypeCrud extends UploadFileSupport implements HasDuplicateValueValidator,
        HasAssetDescriptionTemplateValidator {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AssetTypeCrud.class);

	private LegacyAssetType assetTypeManager;
	private AssetManager assetManager;
	private ConnectedEntityLoader entityLoader;

	private AssetType assetType;
	private AssetTypeRemovalSummary removalSummary;
	private List<AssetType> assetTypes;

	private Collection<InfoFieldBean.InfoFieldType> infoFieldTypes;
	private List<InfoOptionInput> editInfoOptions;
	private List<InfoFieldInput> infoFields;
	private Collection<Long> undeletableInfoFields;
	private List<Long> retiredInfoFields;
	private List<UnitOfMeasure> unitOfMeasures;
	private List<AssetTypeGroup> assetTypeGroups;

	private File assetImage;

	private String assetImageDirectory;
	private boolean removeImage = false;
	private boolean newImage = false;
	
	private String nameFilter;
	private Long groupFilter;

	public AssetTypeCrud(PersistenceManager persistenceManager, LegacyAssetType assetTypeManager, AssetManager assetManager) {
		super(persistenceManager);
		this.assetTypeManager = assetTypeManager;
		this.assetManager = assetManager;
		entityLoader = new ConnectedEntityLoader(persistenceManager);
		infoFieldTypes = Arrays.asList(InfoFieldBean.InfoFieldType.values());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		assetType = getLoaderFactory().createAssetTypeLoader().setId(uniqueId).setStandardPostFetches().load();
	}

	@Override
	protected void initMemberFields() {
		assetType = new AssetType();
	}

	public void perpare() throws Exception {
		super.prepare();
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	@SkipValidation
	public String doLoadCopy() {
		testForAssetType();
		new CleanAssetTypeFactory(assetType, getTenant()).clean();
		setUniqueID(null);
		assetType.setName(null);
		return INPUT;
	}

	@SkipValidation
	public String doLoadEdit() {
		testForAssetType();
		assetImageDirectory = assetType.getImageName();
		setAttachments(assetType.getAttachments());
		return INPUT;
	}

	@SkipValidation
	public String doShow() {
		testForAssetType();
		return SUCCESS;
	}

	public String doSave() {
		testForAssetType();

		if (assetType.getInfoFields() == null) {
			assetType.setInfoFields(new ArrayList<InfoFieldBean>());
		}
		assetType.setTenant(getTenant());

		processInfoFields();
		processInfoOptions();

		Map<File, String> uploadedFilePaths = new HashMap<File, String>();
		File tmpDirectory = PathHandler.getTempRoot();
		if (!getUploadedFiles().isEmpty()) {
			for (FileAttachment attachment : getUploadedFiles()) {
				if (attachment != null) {
					uploadedFilePaths.put(new File(tmpDirectory, attachment.getFileName()), attachment.getComments());
				}
			}
		}

		processUploadedImage();

		try {
			updateAttachmentList(assetType, fetchCurrentUser());
			assetType = assetTypeManager.updateAssetType(assetType, getUploadedFiles(), assetImage);
			addFlashMessage("Data has been updated.");

			uniqueID = assetType.getId();

			infoFields = null;
			editInfoOptions = null;
			getUploadedFiles().clear();
			return SUCCESS;
		} catch (FileAttachmentException e) {
			addActionError(getText("error.attachingfile"));
			logger.error("Failed to attach file to Asset Type", e);
		} catch (ImageAttachmentException e) {
			addActionError(getText("error.attachingimage"));
			logger.error("Failed to attach image to Asset Type", e);
		} catch (Exception e) {
			addActionError(getText("error.failedtosave"));
			logger.error("Failed to update Asset Type", e);
		}
		return INPUT;
	}

	private void testForAssetType() {
		if (assetType == null) {
			addActionErrorText("error.noassettype");
			throw new MissingEntityException();
		}
	}

	private void processUploadedImage() {
		if (removeImage == true) {
			assetType.setImageName(null);
		}

		if (newImage == true && assetImageDirectory != null && assetImageDirectory.length() != 0) {
			File tmpDirectory = PathHandler.getTempRoot();
			assetImage = new File(tmpDirectory.getAbsolutePath() + '/' + assetImageDirectory);
			assetType.setImageName(assetImage.getName());
		}
	}

	// TODO: refactor to use the input info field to convert -- AA
	private void processInfoFields() {
		List<InfoFieldBean> deleted = new ArrayList<InfoFieldBean>();
		for (InfoFieldInput input : infoFields) {
			if (input.getUniqueID() == null) {
				if (!input.isDeleted()) {
					// TODO move this create infofield into the infofieldinput
					InfoFieldBean addedInfoField = new InfoFieldBean();
					addedInfoField.setName(input.getName().trim());
					addedInfoField.setWeight(input.getWeight());
					addedInfoField.setRequired(input.isRequired());
					addedInfoField.setIncludeTime(input.isIncludeTime());

					input.setInfoFieldFieldType(addedInfoField);
					addedInfoField.setUnfilteredInfoOptions(new HashSet<InfoOptionBean>());
					addedInfoField.setRetired(input.isRetired());
					assetType.getInfoFields().add(addedInfoField);
					assetType.associateFields();

					if (input.getDefaultUnitOfMeasure() != null) {
						addedInfoField.setUnitOfMeasure(persistenceManager.find(UnitOfMeasure.class, input
								.getDefaultUnitOfMeasure()));
					} else {
						addedInfoField.setUnitOfMeasure(null);
					}

					input.setInfoField(addedInfoField);
				}
			} else {
				for (InfoFieldBean infoField : assetType.getInfoFields()) {
					if (infoField.getUniqueID().equals(input.getUniqueID())) {
						if (input.isDeleted()) {
							deleted.add(infoField);
						} else {
							// TODO move this create infofield into the
							// infofieldinput
							infoField.setName(input.getName().trim());
							infoField.setWeight(input.getWeight());
							infoField.setRequired(input.isRequired());
							infoField.setIncludeTime(input.isIncludeTime());
							input.setInfoFieldFieldType(infoField);

							infoField.setRetired(input.isRetired());
							if (input.getDefaultUnitOfMeasure() != null) {
								infoField.setUnitOfMeasure(persistenceManager.find(UnitOfMeasure.class, input
										.getDefaultUnitOfMeasure()));
							} else {
								infoField.setUnitOfMeasure(null);
							}

						}
						input.setInfoField(infoField);
					}
				}
			}
		}

		assetType.getInfoFields().removeAll(deleted);
	}

	// TODO: refactor to use the input info option to convert  -- AA
	private void processInfoOptions() {
		for (InfoOptionInput input : editInfoOptions) {
			if (input.getInfoFieldIndex().intValue() < infoFields.size()) {
				InfoFieldBean infoField = infoFields.get(input.getInfoFieldIndex().intValue()).getInfoField();
				if (infoField != null) {
					if (input.getUniqueID() == null) {
						if (!input.isDeleted() && infoField.hasStaticInfoOption()) {
							// TODO move this create infooption into the
							// infooptioninput
							
							InfoOptionBean addedInfoOption = new InfoOptionBean();
							addedInfoOption.setName(input.getName().trim());
							addedInfoOption.setWeight(input.getWeight());
							addedInfoOption.setStaticData(true);
							infoField.getUnfilteredInfoOptions().add(addedInfoOption);
							infoField.associateOptions();
						}
					} else {
						for (InfoOptionBean infoOption : infoField.getUnfilteredInfoOptions()) {

							if (infoOption.getUniqueID() != null
									&& infoOption.getUniqueID().equals(input.getUniqueID())) {
								// TODO move this create infooption into the
								// infooptioninput
								if (input.isDeleted() || !infoField.hasStaticInfoOption()) {
									infoOption.setStaticData(false);
									infoOption.setWeight(0L);
								} else {
									infoOption.setName(input.getName().trim());
									infoOption.setWeight(input.getWeight());
									infoOption.setStaticData(true);
								}

							}

						}
					}
				}
			}
		}
	}

	@SkipValidation
	public String doConfirmDelete() {
		testForAssetType();
		try {
			removalSummary = assetManager.testArchive(assetType);
		} catch (Exception e) {
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		testForAssetType();
		try {
			assetManager.archive(assetType, getSessionUser().getUniqueID(), getText("label.beingdeleted"));
			addFlashMessageText("message.assettypedeleted");
			return SUCCESS;
		} catch (Exception e) {
			addFlashErrorText("error.deleteassettype");
			return ERROR;
		}
		
	}


	@CustomValidator(type = "imageUploadContentType", message = "", key = "errors.filemustbeanimage")
	public String getUploadedImageContentType() {
		if (newImage == true) {
            return ContentTypeUtil.getContentType(new File(assetImageDirectory).getName());
		}
		return null;
	}

	public String getName() {
		return assetType.getName();
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.assettypenamerequired")
	@CustomValidator(type = "uniqueValue", message = "", key = "error.assettypenameduplicate")
	public void setName(String name) {
		assetType.setName(name);
	}

	public String getWarnings() {
		return assetType.getWarnings();
	}

	public void setWarnings(String warnings) {
		assetType.setWarnings(warnings);
	}

	public String getCautions() {
		return assetType.getCautionUrl();
	}

	public void setCautions(String cautions) {
		assetType.setCautionUrl(cautions);
	}

	public String getCautionsUrl() {
		return assetType.getCautionUrl();
	}

	@UrlValidator(message = "", key = "error.cautionsurl")
	public void setCautionsUrl(String cautionsUrl) {
		assetType.setCautionUrl(cautionsUrl);
	}

	public String getInstructions() {
		return assetType.getInstructions();
	}

	public void setInstructions(String instructions) {
		assetType.setInstructions(instructions);
	}

	public boolean isHasManufacturerCertificate() {
		return assetType.isHasManufactureCertificate();
	}

	public void setHasManufacturerCertificate(boolean hasManufacturerCertificate) {
		assetType.setHasManufactureCertificate(hasManufacturerCertificate);
	}

	public String getManufacturerCertificateText() {
		return assetType.getManufactureCertificateText();
	}

	@StringLengthFieldValidator(maxLength="2000", key="errors.maxmancerttextlength", message="")
	public void setManufacturerCertificateText(String manufacturerCertificateText) {
		assetType.setManufactureCertificateText(manufacturerCertificateText);
	}

	public String getDescriptionTemplate() {
		return assetType.getDescriptionTemplate();
	}

	@CustomValidator(type = "assetDescriptionTemplate", message = "", key = "errors.infofieldnamenotblank")
	public void setDescriptionTemplate(String descriptionTemplate) {
		assetType.setDescriptionTemplate(descriptionTemplate);
	}

	public List<InfoFieldInput> getInfoFields() {
		if (infoFields == null) {
			infoFields = new ArrayList<InfoFieldInput>();
			Iterator<InfoFieldBean> iter = assetType.getInfoFields().iterator();
			while (iter.hasNext()) {
				InfoFieldBean infoField = iter.next();
				infoFields.add(new InfoFieldInput(infoField));

			}

		}
		return infoFields;
	}

	@Validations(customValidators = {
			@CustomValidator(type = "infoFields", message = "", key = "errors.infofieldnamenotblank"),
			@CustomValidator(type = "infoFieldComboBox", message = "", key = "errors.infofieldComboBox") })
	public void setInfoFields(List<InfoFieldInput> infoFields) {
		this.infoFields = infoFields;
	}

	public List<AssetType> getAssetTypes() {
		if (assetTypes == null) {
			assetTypes = getLoaderFactory().createAssetTypeListLoader()
			                               .setNameFilter(nameFilter)
			                               .setGroupFilter(groupFilter)
			                               .setPostFetchFields("modifiedBy", "createdBy")
			                               .load();
		}
		return assetTypes;
	}

	public Collection<InfoFieldBean.InfoFieldType> getInfoFieldTypes() {
		return infoFieldTypes;
	}

	public Collection<Long> getUndeletableInfoFields() {
		if (undeletableInfoFields == null) {
			if (assetType != null) {
				undeletableInfoFields = assetTypeManager.infoFieldsInUse(assetType.getInfoFields());
			}
			if (undeletableInfoFields == null) {
				undeletableInfoFields = new ArrayList<Long>();
			}
		}
		return undeletableInfoFields;
	}

	@CustomValidator(type = "infoOptions", message = "", key = "errors.infooptionnamenotblank")
	public List<InfoOptionInput> getEditInfoOptions() {
		if (editInfoOptions == null) {
			editInfoOptions = new ArrayList<InfoOptionInput>();

			List<InfoFieldBean> infoFieldList = new ArrayList<InfoFieldBean>(assetType.getInfoFields());
			for (int index = 0; index < infoFieldList.size(); index++) {
				for (InfoOptionBean infoOption : infoFieldList.get(index).getInfoOptions()) {
					InfoOptionInput infoOptionInput = new InfoOptionInput(infoOption);
					infoOptionInput.setInfoFieldIndex((long) index);
					editInfoOptions.add(infoOptionInput);
				}
			}

		}
		return editInfoOptions;
	}

	public void setEditInfoOptions(List<InfoOptionInput> infoOptions) {
		this.editInfoOptions = infoOptions;
	}

	@CustomValidator(type = "fileSizeValidator", message = "", key = "errors.file_too_large")
	public File getAssetImage() {
		return assetImage;
	}

	public void setAssetImage(File assetImage) {
		this.assetImage = assetImage;
	}

	public List<Long> getRetiredInfoFields() {
		return retiredInfoFields;
	}

	public void setRetiredInfoFields(List<Long> retiredInfoFields) {
		this.retiredInfoFields = retiredInfoFields;
	}

	public boolean duplicateValueExists(String formValue) {
		return !persistenceManager.uniqueNameAvailable(AssetType.class, formValue, uniqueID, getTenantId());
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public boolean isRemoveImage() {
		return removeImage;
	}

	public void setRemoveImage(boolean removeImage) {
		this.removeImage = removeImage;
	}

	public File returnFile(String fileName) {
		return new File(fileName);
	}

	public boolean isNewImage() {
		return newImage;
	}

	public void setNewImage(boolean newImage) {
		this.newImage = newImage;
	}

	public List<AssetType> getSubTypes() {
		return new ArrayList<AssetType>(assetType.getSubTypes());
	}

	public String getAssetImageDirectory() {
		return assetImageDirectory;
	}

	public void setAssetImageDirectory(String assetImageDirectory) {
		this.assetImageDirectory = assetImageDirectory;
	}

	public List<UnitOfMeasure> getUnitsOfMeasure() {

		if (unitOfMeasures == null) {
			unitOfMeasures = persistenceManager.findAll(UnitOfMeasure.class, "name");
		}

		return unitOfMeasures;
	}

	public AssetTypeRemovalSummary getRemovalSummary() {
		return removalSummary;
	}

	public Long getGroup() {
		return (assetType.getGroup() != null) ? assetType.getGroup().getId() : null;
	}

	public void setGroup(Long group) {
		assetType.setGroup(entityLoader.getEntity(AssetTypeGroup.class, group, assetType.getGroup(), getSecurityFilter()));
	}

	public List<AssetTypeGroup> getAssetTypeGroups() {
		if (assetTypeGroups == null) {
			assetTypeGroups = persistenceManager.findAll(new QueryBuilder<AssetTypeGroup>(AssetTypeGroup.class, getSecurityFilter()).addOrder("orderIdx"));
		}
		return assetTypeGroups;
	}
	
	public List<AssetTypeGroup> getFilteringAssetTypeGroups() {
		List<AssetTypeGroup> filteringGroups = getAssetTypeGroups();
		AssetTypeGroup noGroupOption = new AssetTypeGroup();
		noGroupOption.setID(-1L);
		noGroupOption.setName("Not in a Group");
		filteringGroups.add(0, noGroupOption);
		
		return filteringGroups;
	}

	public String getNameFilter() {
		return nameFilter;
	}

	public void setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
	}

	public Long getGroupFilter() {
		return groupFilter;
	}

	public void setGroupFilter(Long groupFilter) {
		this.groupFilter = groupFilter;
	}
	
	

}
