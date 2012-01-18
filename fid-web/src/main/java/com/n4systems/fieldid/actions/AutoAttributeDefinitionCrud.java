package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.InfoFieldBean;
import rfid.web.helper.Constants;
import rfid.web.helper.SessionUser;

import com.n4systems.ejb.AutoAttributeManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.actions.helpers.SessionUserDateConverter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.n4systems.util.StringListingPair;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class AutoAttributeDefinitionCrud extends AbstractCrud {

private static final long serialVersionUID = 1L;
	
	private AutoAttributeManager autoAttributeManager;
	private static final String AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_INPUTS = "AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_INPUTS";
	private static final String AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_OUTPUTS = "AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_OUTPUTS";
	
	private Collection<AutoAttributeDefinition> autoAttributeDefinitions;
	private AutoAttributeCriteria autoAttributeCriteria;
	private AutoAttributeDefinition autoAttributeDefinition;
	private AssetType assetType;
	private Long criteriaId;
	
	private List<InfoOptionInput> outputInfoOptions;
	private List<InfoOptionInput> inputInfoOptions;
	
	private Pager<AutoAttributeDefinition> page;
	
	private String saveandadd;
	
	
	public AutoAttributeDefinitionCrud( PersistenceManager persistenceManager, AutoAttributeManager autoAttributeManager ) {
		super(persistenceManager);
		this.autoAttributeManager = autoAttributeManager;
	}
	
	@Override
	protected void initMemberFields() {
		autoAttributeDefinition = new AutoAttributeDefinition();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		String[] fetches = { "outputs" };
		autoAttributeDefinition = persistenceManager.find( AutoAttributeDefinition.class, uniqueId, getTenant(), fetches );
	}

	@Override
	public void prepare() throws Exception {
		super.prepare();
		String[] fetches = { "inputs", "outputs", "assetType.name" };
		autoAttributeCriteria = persistenceManager.find( AutoAttributeCriteria.class, getCriteriaId(), getTenant(), fetches );
	}


	@SkipValidation
	public String doList() {
		page = autoAttributeManager.findAllPage( autoAttributeCriteria, getTenant(), getCurrentPage(), Constants.PAGE_SIZE );
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doEdit() {
		
		if( autoAttributeCriteria == null || autoAttributeDefinition == null ) {
			addFlashMessage( getText( "error.noautoattributecriteria" ) );
			return ERROR;
		}
		if( autoAttributeDefinition.getId() == null ) {
			getSavedInputs();
		}
		
		return SUCCESS;
	}
	
	
	public String doSave() {
		
		if( autoAttributeCriteria == null || autoAttributeDefinition == null ) {
			addFlashMessage( getText( "error.noautoattributecriteria" ) );
			return ERROR;
		}
		
		autoAttributeDefinition.setCriteria( autoAttributeCriteria );
		autoAttributeDefinition.setTenant( getTenant() );
		
		String error = validateOutputs();
		if (error!=null) {
			addActionErrorText(error);
			return INPUT;
		}
		
		convertOutputsToInfoOptionsWithValidation();
		convertInputsToInfoOptions();
		
		AutoAttributeDefinition existingDefinition = autoAttributeManager.findTemplateToApply( autoAttributeCriteria, autoAttributeDefinition.getInputs() ); 
		if( existingDefinition != null && !existingDefinition.getId().equals( uniqueID ) ) {
			addActionError( "These inputs have already been used on an auto attribute definition." );
			return INPUT;
		}
		
		try {
			autoAttributeDefinition = autoAttributeManager.saveDefinition( autoAttributeDefinition );
		} catch (Exception e) {
			addActionErrorText("error.definition_save_failed");
			return INPUT;
		}
		
		uniqueID = autoAttributeDefinition.getId();
		addFlashMessageText("message.definition_saved");
		if( saveandadd != null ) {
			setSavedInputs();
			return "saveandadd";
		}
		return "saved";
	}
	
	
	
	@SkipValidation
	public String doRemove() {
				
		if( autoAttributeCriteria == null ) {
			addFlashMessage( getText( "error.noautoattributecriteria" ) );
			return ERROR;
		}
		if( autoAttributeDefinition == null ) {
			addFlashMessage( getText( "error.noautoattributedefinition" ) );
			return ERROR;
		}
				
		try {
			autoAttributeManager.removeDefinition( autoAttributeDefinition );
		} catch (Exception e) {
			addActionErrorText("error.remove_definition_failed");
			return ERROR;
		}
		
		addFlashMessageText("message.definition_removed");
		
		return SUCCESS;
	}
	

	// WEB-2583 NOTE : previously, no validation was done because the field types didn't require it. 
	//  this was added to handle date fields.  if more and more field types are added this should be refactored
	//  into a separate class.   
	private String validateOutputs() {				
		SessionUser user = getSessionUser();
		for( InfoOptionInput input : outputInfoOptions ) {
			for (InfoFieldBean field: autoAttributeCriteria.getOutputs()) {		
				if(field.getUniqueID().equals(input.getInfoFieldId()) && input!=null) {				
					String error = validateField(field, user, input.getName());
					if (error!=null) { 
						return error;
					}
				}
			}
		}
		return null;
	}
	
	private String validateField(InfoFieldBean field, SessionUser user, String name) {
		if (field.getFieldType().equals( InfoFieldBean.DATEFIELD_FIELD_TYPE)) {
			SessionUserDateConverter dateConverter = user.createUserDateConverter();
			Date date = dateConverter.convertDate(name, field.isIncludeTime());
			return date==null ? getText("error.invalidate_date_value") : null;
		}
		return null;
	}

	private void convertOutputsToInfoOptionsWithValidation() {
		autoAttributeDefinition.setOutputs( InfoOptionInput.convertInputInfoOptionsToInfoOptions( outputInfoOptions, autoAttributeCriteria.getOutputs(), getSessionUser() ) );
	}
	
	private void convertInputsToInfoOptions() {
		autoAttributeDefinition.setInputs( InfoOptionInput.convertInputInfoOptionsToInfoOptions( inputInfoOptions, autoAttributeCriteria.getInputs(), getSessionUser() ) );
	}
	
	@SuppressWarnings("unchecked")
	private void getSavedInputs() {
		inputInfoOptions = (List<InfoOptionInput>)getSession().get( AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_INPUTS );
		outputInfoOptions = (List<InfoOptionInput>)getSession().get( AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_OUTPUTS );
		getSession().remove( AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_INPUTS );
		getSession().remove( AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_OUTPUTS );
	}
	
	private void setSavedInputs() {
		getSession().put( AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_INPUTS, inputInfoOptions );
		getSession().put( AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_OUTPUTS, outputInfoOptions );
		
	}
	
	public AutoAttributeDefinition getAutoAttributeDefinition() {
		return autoAttributeDefinition;
	}
	
	public void setAutoAttributeDefinition(	AutoAttributeDefinition autoAttributeDefinition ) {
		this.autoAttributeDefinition = autoAttributeDefinition;
	}
	
	public Collection<AutoAttributeDefinition> getAutoAttributeDefinitions() {
		return autoAttributeDefinitions;
	}
	
	public AutoAttributeCriteria getAutoAttributeCriteria() {
		return autoAttributeCriteria;
	}
	
	public AssetType getAssetType() {
		return assetType;
	}
	
	public Long getCriteriaId() {
		return criteriaId;
	}
	
	public void setCriteriaId(Long criteriaId) {
		this.criteriaId = criteriaId;
	}
	
	
	public List<InfoOptionInput> getOutputInfoOptions() {
		if( outputInfoOptions == null ) {
			outputInfoOptions = new ArrayList<InfoOptionInput>();
			if( autoAttributeCriteria.getOutputs() != null ) {
				outputInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions( autoAttributeDefinition.getOutputs(), autoAttributeCriteria.getOutputs(), getSessionUser());
			}
		}
		return outputInfoOptions;
	}


	public void setOutputInfoOptions(List<InfoOptionInput> outputInfoOptions) {
		this.outputInfoOptions = outputInfoOptions;
	}
	
	public List<InfoOptionInput> getInputInfoOptions() {
		if( inputInfoOptions == null ) {
			
			inputInfoOptions = new ArrayList<InfoOptionInput>();
			if( autoAttributeCriteria.getInputs() != null ) {
				inputInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions( autoAttributeDefinition.getInputs(), autoAttributeCriteria.getInputs(), getSessionUser() );
			}
		}
		return inputInfoOptions;
	}


	public void setInputInfoOptions(List<InfoOptionInput> inputInfoOptions) {
		this.inputInfoOptions = inputInfoOptions;
	}
	
	public List<InfoFieldBean> getOutputInfoFields() {
		return autoAttributeCriteria.getOutputs();
	}
	
	
	public List<InfoFieldBean> getInputInfoFields() {
		return autoAttributeCriteria.getInputs();
	}



	public Pager<AutoAttributeDefinition> getPage() {
		return page;
	}


	public void setSaveandadd(String saveandadd) {
		this.saveandadd = saveandadd;
	}
	
	public List<StringListingPair> getComboBoxInfoOptions( InfoFieldBean field, InfoOptionInput inputOption ) {
		return InfoFieldInput.getComboBoxInfoOptions( field, inputOption  );
	}
}
