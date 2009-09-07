package com.n4systems.fieldid.actions.product;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.FileTypeMap;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.session.LegacyProductType;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.fieldid.actions.helpers.ConnectedEntityLoader;
import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.actions.helpers.UploadFileSupport;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.fieldid.validators.HasProductDescriptionTemplateValidator;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeGroup;
import com.n4systems.model.UnitOfMeasure;
import com.n4systems.model.utils.CleanProductTypeFactory;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ListingPair;
import com.n4systems.util.ProductTypeRemovalSummary;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.UrlValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.Validations;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Validation
public class ProductTypeCrud extends UploadFileSupport implements HasDuplicateValueValidator,
		HasProductDescriptionTemplateValidator {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ProductTypeCrud.class);

	private LegacyProductType productTypeManager;
	private ProductManager productManager;
	private ConnectedEntityLoader entityLoader;

	private ProductType productType;
	private ProductTypeRemovalSummary removalSummary;
		

	private Collection<InfoFieldBean.InfoFieldType> infoFieldTypes;
	private List<InfoOptionInput> editInfoOptions;
	private List<InfoFieldInput> infoFields;
	private Collection<Long> undeletableInfoFields;
	private List<Long> retiredInfoFields;
	private List<UnitOfMeasure> unitOfMeasures;
	private List<ProductTypeGroup> productTypeGroups;

	private File productImage;

	private String productImageDirectory;
	private boolean removeImage = false;
	private boolean newImage = false;

	public ProductTypeCrud(PersistenceManager persistenceManager, LegacyProductType productTypeManager, ProductManager productManager) {
		super(persistenceManager);
		this.productTypeManager = productTypeManager;
		this.productManager = productManager;
		entityLoader = new ConnectedEntityLoader(persistenceManager);
		infoFieldTypes = Arrays.asList(InfoFieldBean.InfoFieldType.values());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		productType = productTypeManager.findProductTypeAllFields(uniqueID, getTenantId());
	}

	@Override
	protected void initMemberFields() {
		productType = new ProductType();
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
		testForProductType();
		new CleanProductTypeFactory(productType, getTenant()).clean();
		setUniqueID(null);
		productType.setName(null);
		return INPUT;
	}

	@SkipValidation
	public String doLoadEdit() {
		testForProductType();
		productImageDirectory = productType.getImageName();
		setAttachments(productType.getAttachments());
		return INPUT;
	}

	@SkipValidation
	public String doShow() {
		testForProductType();
		return SUCCESS;
	}

	public String doSave() {
		testForProductType();

		if (productType.getInfoFields() == null) {
			productType.setInfoFields(new ArrayList<InfoFieldBean>());
		}
		productType.setTenant(getTenant());

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
			updateAttachmentList(productType, fetchCurrentUser());
			productType = productTypeManager.updateProductType(productType, getUploadedFiles(), productImage);
			addFlashMessage("Data has been updated.");

			uniqueID = productType.getId();

			infoFields = null;
			editInfoOptions = null;
			getUploadedFiles().clear();
			return SUCCESS;
		} catch (FileAttachmentException e) {
			addActionError(getText("error.attachingfile"));
			logger.error("Failed to attach file to Product Type", e);
		} catch (ImageAttachmentException e) {
			addActionError(getText("error.attachingimage"));
			logger.error("Failed to attach image to Product Type", e);
		} catch (Exception e) {
			addActionError(getText("error.failedtosave"));
			logger.error("Failed to update Product Type", e);
		}
		return INPUT;
	}

	private void testForProductType() {
		if (productType == null) {
			addActionErrorText("error.noproducttype");
			throw new MissingEntityException();
		}
	}

	private void processUploadedImage() {
		if (removeImage == true) {
			productType.setImageName(null);
		}

		if (newImage == true && productImageDirectory != null && productImageDirectory.length() != 0) {
			File tmpDirectory = PathHandler.getTempRoot();
			productImage = new File(tmpDirectory.getAbsolutePath() + '/' + productImageDirectory);
			productType.setImageName(productImage.getName());
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

					input.setInfoFieldFieldType(addedInfoField);
					addedInfoField.setUnfilteredInfoOptions(new HashSet<InfoOptionBean>());
					addedInfoField.setRetired(input.isRetired());
					productType.getInfoFields().add(addedInfoField);
					productType.associateFields();

					if (input.getDefaultUnitOfMeasure() != null) {
						addedInfoField.setUnitOfMeasure(persistenceManager.find(UnitOfMeasure.class, input
								.getDefaultUnitOfMeasure()));
					} else {
						addedInfoField.setUnitOfMeasure(null);
					}

					input.setInfoField(addedInfoField);
				}
			} else {
				for (InfoFieldBean infoField : productType.getInfoFields()) {
					if (infoField.getUniqueID().equals(input.getUniqueID())) {
						if (input.isDeleted()) {
							deleted.add(infoField);
						} else {
							// TODO move this create infofield into the
							// infofieldinput
							infoField.setName(input.getName().trim());
							infoField.setWeight(input.getWeight());
							infoField.setRequired(input.isRequired());
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

		productType.getInfoFields().removeAll(deleted);
	}

	// TODO: refactor to use the input info option to convert  -- AA
	private void processInfoOptions() {
		for (InfoOptionInput input : editInfoOptions) {
			if (input.getInfoFieldIndex().intValue() < infoFields.size()) {
				InfoFieldBean infoField = infoFields.get(input.getInfoFieldIndex().intValue()).getInfoField();
				if (infoField != null) {
					if (input.getUniqueID() == null) {
						if (!input.isDeleted()) {
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
		testForProductType();
		try {
			removalSummary = productManager.testArchive(productType);
		} catch (Exception e) {
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		testForProductType();
		try {
			productManager.archive(productType, getSessionUser().getUniqueID(), getText("label.beingdeleted"));
			addFlashMessageText("message.producttypedeleted");
			return SUCCESS;
		} catch (Exception e) {
			addFlashErrorText("error.deleteproducttype");
			return ERROR;
		}
		
	}

	public String doViewImage() {
		return SUCCESS;
	}

	@CustomValidator(type = "imageUploadContentType", message = "", key = "errors.filemustbeanimage")
	public String getUploadedImageContentType() {
		if (newImage == true) {
			return FileTypeMap.getDefaultFileTypeMap().getContentType(new File(productImageDirectory).getName());
		}
		return null;
	}

	public String getName() {
		return productType.getName();
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.producttypenamerequired")
	@CustomValidator(type = "uniqueValue", message = "", key = "error.producttypenameduplicate")
	public void setName(String name) {
		productType.setName(name);
	}

	public String getWarnings() {
		return productType.getWarnings();
	}

	public void setWarnings(String warnings) {
		productType.setWarnings(warnings);
	}

	public String getCautions() {
		return productType.getCautionUrl();
	}

	public void setCautions(String cautions) {
		productType.setCautionUrl(cautions);
	}

	public String getCautionsUrl() {
		return productType.getCautionUrl();
	}

	@UrlValidator(message = "", key = "error.cautionsurl")
	public void setCautionsUrl(String cautionsUrl) {
		productType.setCautionUrl(cautionsUrl);
	}

	public String getInstructions() {
		return productType.getInstructions();
	}

	public void setInstructions(String instructions) {
		productType.setInstructions(instructions);
	}

	public boolean isHasManufacturerCertificate() {
		return productType.isHasManufactureCertificate();
	}

	public void setHasManufacturerCertificate(boolean hasManufacturerCertificate) {
		productType.setHasManufactureCertificate(hasManufacturerCertificate);
	}

	public String getManufacturerCertificateText() {
		return productType.getManufactureCertificateText();
	}

	@StringLengthFieldValidator(maxLength="2000", key="errors.maxmancerttextlength", message="")
	public void setManufacturerCertificateText(String manufacturerCertificateText) {
		productType.setManufactureCertificateText(manufacturerCertificateText);
	}

	public String getDescriptionTemplate() {
		return productType.getDescriptionTemplate();
	}

	@CustomValidator(type = "productDescriptionTemplate", message = "", key = "errors.infofieldnamenotblank")
	public void setDescriptionTemplate(String descriptionTemplate) {
		productType.setDescriptionTemplate(descriptionTemplate);
	}

	public List<InfoFieldInput> getInfoFields() {
		if (infoFields == null) {
			infoFields = new ArrayList<InfoFieldInput>();
			Iterator<InfoFieldBean> iter = productType.getInfoFields().iterator();
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

	public Collection<ListingPair> getProductTypes() {
		return productTypeManager.getProductTypeListForTenant(getTenantId());
	}

	public Collection<InfoFieldBean.InfoFieldType> getInfoFieldTypes() {
		return infoFieldTypes;
	}

	public Collection<Long> getUndeletableInfoFields() {
		if (undeletableInfoFields == null) {
			if (productType != null) {
				undeletableInfoFields = productTypeManager.infoFieldsInUse(productType.getInfoFields());
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

			List<InfoFieldBean> infoFieldList = new ArrayList<InfoFieldBean>(productType.getInfoFields());
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
	public File getProductImage() {
		return productImage;
	}

	public void setProductImage(File productImage) {
		this.productImage = productImage;
	}

	public List<Long> getRetiredInfoFields() {
		return retiredInfoFields;
	}

	public void setRetiredInfoFields(List<Long> retiredInfoFields) {
		this.retiredInfoFields = retiredInfoFields;
	}

	public boolean duplicateValueExists(String formValue) {
		return !persistenceManager.uniqueNameAvailable(ProductType.class, formValue, uniqueID, getTenantId());
	}

	public ProductType getProductType() {
		return productType;
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

	public List<ProductType> getSubTypes() {
		return new ArrayList<ProductType>(productType.getSubTypes());
	}

	public String getProductImageDirectory() {
		return productImageDirectory;
	}

	public void setProductImageDirectory(String productImageDirectory) {
		this.productImageDirectory = productImageDirectory;
	}

	public List<UnitOfMeasure> getUnitsOfMeasure() {

		if (unitOfMeasures == null) {
			unitOfMeasures = persistenceManager.findAll(UnitOfMeasure.class, "name");
		}

		return unitOfMeasures;
	}

	public ProductTypeRemovalSummary getRemovalSummary() {
		return removalSummary;
	}

	public Long getGroup() {
		return (productType.getGroup() != null) ? productType.getGroup().getId() : null;
	}

	public void setGroup(Long group) {
		productType.setGroup(entityLoader.getEntity(ProductTypeGroup.class, group, productType.getGroup(), getSecurityFilter()));
	}

	public List<ProductTypeGroup> getProductTypeGroups() {
		if (productTypeGroups == null) {
			productTypeGroups = persistenceManager.findAll(new QueryBuilder<ProductTypeGroup>(ProductTypeGroup.class, getSecurityFilter()).addOrder("orderIdx"));
		}
		return productTypeGroups;
	}
	
	

}
