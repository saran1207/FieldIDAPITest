package com.n4systems.fieldid.actions;

import com.n4systems.ejb.AutoAttributeManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAssetType;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.security.Permissions;
import com.n4systems.util.ListingPair;
import com.n4systems.util.StringListingPair;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import org.apache.struts2.interceptor.validation.SkipValidation;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_SYSTEM_CONFIG})
public class AutoAttributeCriteriaCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;

	private LegacyAssetType assetTypeManager;
	private AutoAttributeManager attributeManager;

	private Collection<ListingPair> assetTypes;
	private AutoAttributeCriteria autoAttributeCriteria;
	private AssetType assetType;

	private List<Long> inputs;
	private List<Long> outputs;
	private List<Long> available;

	private List<ListingPair> lookUpInputs = new ArrayList<ListingPair>();
	private List<StringListingPair> lookUpOutputs = new ArrayList<StringListingPair>();

	public AutoAttributeCriteriaCrud(LegacyAssetType assetTypeManager, PersistenceManager persistenceManager,
			AutoAttributeManager attributeManager) {
		super(persistenceManager);
		this.assetTypeManager = assetTypeManager;
		this.attributeManager = attributeManager;
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		assetType = getLoaderFactory().createAssetTypeLoader().setId(uniqueId).setStandardPostFetches().load();
		
		if (assetType != null && assetType.hasCriteria()) {
			String[] fetchList = { "inputs", "outputs" };
			autoAttributeCriteria = persistenceManager.find(AutoAttributeCriteria.class, assetType
					.getAutoAttributeCriteria().getId(), getTenant(), fetchList);
		} else {
			autoAttributeCriteria = new AutoAttributeCriteria();
		}

	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.TAG})
	public String doShow() {

		if (autoAttributeCriteria == null || autoAttributeCriteria.isNew()) {
			autoAttributeCriteria = null;
			addActionError(getText("error.noautoattributecriteria"));
			return SUCCESS;
		}

		
		List<InfoOptionBean> infoOptionValues = prepareInputOptions(assetType.getInfoFields());
		
		AutoAttributeDefinition definition = attributeManager.findTemplateToApply(assetType, infoOptionValues);

		if (isDefinitionFound(definition)) {
			prepareDefinitionOutputs(definition);
		} else {
			autoAttributeCriteria = null;
		}
		
		
		return SUCCESS;
	}

	private void prepareDefinitionOutputs(AutoAttributeDefinition definition) {
		for (InfoOptionBean infoOptionBean : definition.getSanitizedOutputs()) {
			if (infoOptionBean.getInfoField().hasStaticInfoOption()) {
				lookUpOutputs.add(new StringListingPair(infoOptionBean.getInfoField().getUniqueID().toString(),
						infoOptionBean.getUniqueID().toString()));
			} else {
                if(infoOptionBean.getInfoField().getFieldType().equals(InfoFieldBean.DATEFIELD_FIELD_TYPE)) {
                    lookUpOutputs.add(new StringListingPair(infoOptionBean.getInfoField().getUniqueID().toString(),
                            getDateString(infoOptionBean)));
                } else {
				lookUpOutputs.add(new StringListingPair(infoOptionBean.getInfoField().getUniqueID().toString(),
						infoOptionBean.getName()));
                }
			}
			
		}
	}

    private String getDateString(InfoOptionBean infoOptionBean) {
        Date date = new Date(Long.valueOf(infoOptionBean.getName()));
        if (infoOptionBean.getInfoField().isIncludeTime())
            return formatAnyDate(date, false, true);
        else
            return formatDate(date, false);
    }

	private boolean isDefinitionFound(AutoAttributeDefinition definition) {
		return definition != null;
	}

	private List<InfoOptionBean> prepareInputOptions(Collection<InfoFieldBean> infoFields) {
		List<InfoOptionBean> infoOptionValues = new ArrayList<InfoOptionBean>();
		for (InfoFieldBean infoField : infoFields) {
			Long optionValueId = null;
			if (infoField.getFieldType().equals(InfoFieldBean.TEXTFIELD_FIELD_TYPE)) {
			} else {
				String optionValueIdString = null;
				for (ListingPair input : lookUpInputs) {
					if (input.getId().equals(infoField.getUniqueID())) {
						optionValueIdString = input.getName();
						break;
					}
				}
				if (!(optionValueIdString == null || optionValueIdString.length() == 0)
						&& !(optionValueIdString.startsWith("!"))) {
					optionValueId = Long.parseLong(optionValueIdString);
					InfoOptionBean infoOptionBean = new InfoOptionBean();
					infoOptionBean.setUniqueID(optionValueId);
					infoOptionValues.add(infoOptionBean);
				}
			}
		}
		return infoOptionValues;
	}

	

		@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	@SkipValidation
	public String doEdit() {
		if (assetType == null) {
			addFlashMessageText("error.noassettype");
			return ERROR;
		}

		if (!assetHasEnoughFields()) {
			addFlashError("The asset needs at least 2 attributes to use a template.");
			return ERROR;
		}

		if (!assetHasStaticFields()) {
			addFlashError("The asset needs at least 1 attribute that is a select or combo box to use a template.");
			return ERROR;
		}

		return SUCCESS;
	}

	public String doSave() {

		if (assetType == null) {
			addFlashMessageText("error.noassettype");
			return ERROR;
		}

		List<InfoFieldBean> inputFields = new ArrayList<InfoFieldBean>();
		List<InfoFieldBean> outputFields = new ArrayList<InfoFieldBean>();
		for (InfoFieldBean infoFieldBean : assetType.getInfoFields()) {
			if (inputs != null) {
				for (Long inputId : inputs) {
					if (infoFieldBean.getUniqueID().equals(inputId)) {
						inputFields.add(infoFieldBean);
					}
				}
			}
			if (outputs != null) {
				for (Long outputId : outputs) {
					if (infoFieldBean.getUniqueID().equals(outputId)) {
						outputFields.add(infoFieldBean);
					}
				}
			}
		}

		autoAttributeCriteria.setInputs(inputFields);
		autoAttributeCriteria.setOutputs(outputFields);
		autoAttributeCriteria.setTenant(getTenant());
		autoAttributeCriteria.setAssetType(assetType);
		autoAttributeCriteria.setDefinitions(new ArrayList<AutoAttributeDefinition>());

		if (autoAttributeCriteria.getId() != null) {

			attributeManager.update(autoAttributeCriteria);
		} else {
			persistenceManager.save(autoAttributeCriteria, getSessionUser().getUniqueID());
		}

		addFlashMessage("Criteria Saved.");
		return "saved";
	}

	@SkipValidation
	public String doOpen() {
		if (assetType == null) {
			addFlashMessageText("error.noassettype");
			return ERROR;
		}

		if (assetType.hasCriteria()) {
			return "found";
		}

		return "new";
	}

	@SkipValidation
	public String doRemove() {

		if (assetType == null) {
			addFlashMessageText("error.noassettype");
			return ERROR;
		}
		try {
			persistenceManager.delete(autoAttributeCriteria);
		} catch (Exception e) {
			addFlashError("The auto attribute criteria could not be removed.");
			return ERROR;
		}

		addFlashMessage("Criteria removed.");
		return SUCCESS;
	}

	public Collection<ListingPair> getAssetTypes() {
		if (assetTypes == null) {
			assetTypes = assetTypeManager.getAssetTypeListForTenant(getTenantId());
		}
		return assetTypes;
	}

	public AutoAttributeCriteria getAutoAttributeCriteria() {
		return autoAttributeCriteria;
	}

	public void setAutoAttributeCriteria(AutoAttributeCriteria autoAttributeCriteria) {
		this.autoAttributeCriteria = autoAttributeCriteria;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public List<Long> getInputs() {
		if (inputs == null) {
			inputs = new ArrayList<Long>();
			List<InfoFieldBean> infoFields = autoAttributeCriteria.getInputs();
			if (infoFields != null) {
				for (InfoFieldBean infoFieldBean : infoFields) {
					inputs.add(infoFieldBean.getUniqueID());
				}
			}
		}
		return inputs;
	}

	@CustomValidator(type = "listNotEmptyValidator", message = "", key = "error.inputsnotempty")
	public void setInputs(List<Long> inputs) {
		this.inputs = inputs;
	}

	public List<Long> getOutputs() {

		if (outputs == null) {
			outputs = new ArrayList<Long>();
			List<InfoFieldBean> infoFields = autoAttributeCriteria.getOutputs();
			if (infoFields != null) {
				for (InfoFieldBean infoFieldBean : infoFields) {
					outputs.add(infoFieldBean.getUniqueID());
				}
			}
		}
		return outputs;
	}

	@CustomValidator(type = "listNotEmptyValidator", message = "", key = "error.outputsnotempty")
	public void setOutputs(List<Long> outputs) {
		this.outputs = outputs;
	}

	public List<Long> getAvailable() {
		if (available == null) {
			available = new ArrayList<Long>();
			Collection<InfoFieldBean> infoFields = assetType.getAvailableInfoFields();
			if (infoFields != null) {
				for (InfoFieldBean infoFieldBean : infoFields) {
					available.add(infoFieldBean.getUniqueID());
				}
			}

			// remove the ones that are used in inputs or outputs.
			getOutputs();
			getInputs();

			available.removeAll(inputs);
			available.removeAll(outputs);

		}
		return available;
	}

	private boolean assetHasStaticFields() {
		for (InfoFieldBean infoField : assetType.getAvailableInfoFields()) {
			if (infoField.hasStaticInfoOption()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * an assetType requires at least 2 fields to have a template.
	 * 
	 */
	private boolean assetHasEnoughFields() {
		return assetType.getAvailableInfoFields().size() >= 2;
	}

	public List<ListingPair> getLookUpInputs() {
		return lookUpInputs;
	}

	public List<StringListingPair> getLookUpOutputs() {
		return lookUpOutputs;
	}

    @Override
    public String getProtoypeVersion() {
        return "prototype_1.7.1";
    }

}
