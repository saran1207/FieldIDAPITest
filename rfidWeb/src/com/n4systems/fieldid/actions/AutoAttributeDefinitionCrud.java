package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.InfoFieldBean;
import rfid.web.helper.Constants;

import com.n4systems.ejb.AutoAttributeManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.ProductType;
import com.n4systems.tools.Pager;
import com.n4systems.util.StringListingPair;

public class AutoAttributeDefinitionCrud extends AbstractCrud {

private static final long serialVersionUID = 1L;
	
	private AutoAttributeManager autoAttributeManager;
	private static final String AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_INPUTS = "AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_INPUTS";
	private static final String AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_OUTPUTS = "AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_OUTPUTS";
	
	private Collection<AutoAttributeDefinition> autoAttributeDefinitions;
	private AutoAttributeCriteria autoAttributeCriteria;
	private AutoAttributeDefinition autoAttributeDefinition;
	private ProductType productType;
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

	public void prepare() throws Exception {
		super.prepare();
		String[] fetches = { "inputs", "outputs", "productType.name" };
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
		convertOutputsToInfoOptions();
		convertInputsToInfoOptions();
		
		AutoAttributeDefinition existingDefinition = autoAttributeManager.findTemplateToApply( autoAttributeCriteria, autoAttributeDefinition.getInputs() ); 
		if( existingDefinition != null && !existingDefinition.getId().equals( uniqueID ) ) {
			addActionError( "These inputs have already been used on an auto attribute definition." );
			return INPUT;
		}
		
		try {
			autoAttributeDefinition = autoAttributeManager.saveDefinition( autoAttributeDefinition );
		} catch (Exception e) {
			addActionError( "Could not save the auto attribute definition." );
			return INPUT;
		}
		
		uniqueID = autoAttributeDefinition.getId();
		addFlashMessage("Definition saved!" );
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
			addFlashError( "Could not remove auto attribute definition." );
			return ERROR;
		}
		
		addFlashMessage( "Definition removeded!" );
		
		return SUCCESS;
	}
	
	
	
	private void convertOutputsToInfoOptions() {
		autoAttributeDefinition.setOutputs( InfoOptionInput.convertInputInfoOptionsToInfoOptions( outputInfoOptions, autoAttributeCriteria.getOutputs() ) );
	}
	
	private void convertInputsToInfoOptions() {
		autoAttributeDefinition.setInputs( InfoOptionInput.convertInputInfoOptionsToInfoOptions( inputInfoOptions, autoAttributeCriteria.getInputs() ) );
	}
	
	@SuppressWarnings("unchecked")
	private void getSavedInputs() {
		inputInfoOptions = (List<InfoOptionInput>)getSession().get( AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_INPUTS );
		outputInfoOptions = (List<InfoOptionInput>)getSession().get( AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_OUTPUTS );
		getSession().remove( AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_INPUTS );
		getSession().remove( AUTO_ATTRIBUTE_DEFINITION_QUICK_FILL_OUTPUTS );
	}
	
	@SuppressWarnings("unchecked")
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
	
	public ProductType getProductType() {
		return productType;
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
				outputInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions( autoAttributeDefinition.getOutputs(), autoAttributeCriteria.getOutputs() );
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
				inputInfoOptions = InfoOptionInput.convertInfoOptionsToInputInfoOptions( autoAttributeDefinition.getInputs(), autoAttributeCriteria.getInputs() );
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
