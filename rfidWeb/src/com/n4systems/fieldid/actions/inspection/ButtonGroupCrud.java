package com.n4systems.fieldid.actions.inspection;

import java.util.Arrays;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.ListHelper;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.Status;
import com.n4systems.security.Permissions;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class ButtonGroupCrud extends AbstractCrud implements HasDuplicateValueValidator{

	
	private static final long serialVersionUID = 1L;
	
	private StateSet stateSet;
	private Long inspectionTypeId;
	private Long buttonGroupIndex;
	private Long buttonIndex;
	
	private List<StateSet> stateSets;
	
	private Long maxNumberOfImages;
	
	public ButtonGroupCrud( PersistenceManager persistenceManager ) {
		super(persistenceManager);
	}
	
	@Override
	protected void initMemberFields() {
		stateSet = new StateSet();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		stateSet = persistenceManager.find( StateSet.class, uniqueId, getTenantId() );
	}
	
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doAddButton() {
		if( stateSet == null ) {
			addActionError( getText( "error.statesetnotfound" ) );
			return MISSING;
		}
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doAdd() {
		if( stateSet == null ) {
			addActionError( getText( "error.statesetnotfound" ) );
			return MISSING;
		}
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doEdit() {
		if( stateSet == null || stateSet.getId() == null ) {
			addActionError( getText( "error.statesetnotfound" ) );
			return MISSING;
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String doRetire() {
		if( stateSet == null || stateSet.getId() == null ) {
			addActionError( getText( "error.statesetnotfound" ) );
			return MISSING;
		}
		return SUCCESS;
	}
	
	
	public String doSave() {
		if( stateSet == null ) {
			addActionError( getText( "error.statesetnotfound" ) );
			return MISSING;
		}
		
		ListHelper.clearNulls( stateSet.getStates() );
		ListHelper.setTenants( stateSet.getStates(), getTenant() );
				
		stateSet.setTenant( getTenant() );
		
		try {
			stateSet = persistenceManager.update( stateSet );
		} catch (Exception e) {
			addActionError( getText( "error.failedtosave" ) );
			return ERROR;
		}
		
		addActionMessage( getText( "message.buttongroupsaved" ) );
		return SUCCESS;
	}

	public Long getInspectionTypeId() {
		return inspectionTypeId;
	}

	public void setInspectionTypeId(Long inspectionTypeId) {
		this.inspectionTypeId = inspectionTypeId;
	}

	public StateSet getStateSet() {
			return stateSet;
	}

	public List<StateSet> getStateSets() {
		if( stateSets == null ) {
			stateSets = persistenceManager.findAll( StateSet.class, getTenantId() );
		}
		return stateSets;
	}

	public List<Status> getButtonStatuses() {
		return Arrays.asList( Status.values() );
	}
		
	public String getName() {
		return stateSet.getName();
	}
	
	
	@RequiredStringValidator( message="", key="error.groupnamerequired")
	@CustomValidator(type="uniqueValue", message = "", key="error.groupnameunique")
	public void setName( String name ) {
		stateSet.setName( name );
	}
	
	public List<State> getStates(){
		return stateSet.getStates();
	}
	
	
	@Validations(
			customValidators = {
					@CustomValidator(type="requiredSet", message = "", key="error.buttonsempty"),
					@CustomValidator( type="requiredNameSet", message="", key="error.buttonsrequirenames" ) 
					}
			)
	public void setStates( List<State> states ) {
		if( stateSet != null ){
			stateSet.setStates( states );
		}
	}

	public Long getMaxNumberOfImages() {
		if( maxNumberOfImages == null ) {
			maxNumberOfImages = ConfigContext.getCurrentContext().getLong(ConfigEntry.WEB_TOTAL_INSPECTION_BUTTONS);
		}
		return maxNumberOfImages;
	}

	public Long getButtonGroupIndex() {
		return buttonGroupIndex;
	}

	public void setButtonGroupIndex(Long buttonGroupIndex) {
		this.buttonGroupIndex = buttonGroupIndex;
	}

	public boolean duplicateValueExists( String formValue ) {
		return !persistenceManager.uniqueNameAvailable( StateSet.class, formValue, uniqueID, getTenantId() );
	}

	public Long getButtonIndex() {
		return buttonIndex;
	}

	public void setButtonIndex( Long buttonIndex ) {
		this.buttonIndex = buttonIndex;
	}
}
