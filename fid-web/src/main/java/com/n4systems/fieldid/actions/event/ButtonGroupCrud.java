package com.n4systems.fieldid.actions.event;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.Status;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.security.Permissions;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class ButtonGroupCrud extends AbstractCrud implements HasDuplicateValueValidator{

	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ButtonGroupCrud.class);
	
	private StateSet stateSet;
	private Long eventTypeId;
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
		
		StrutsListHelper.clearNulls( stateSet.getStates() );
		StrutsListHelper.setTenants( stateSet.getStates(), getTenant() );
				
		stateSet.setTenant( getTenant() );
		
		try {
			stateSet = persistenceManager.update( stateSet );
			logger.info("Updated StateSet (ButtonGroup): " + stateSet.getName());
			touchEventTypes(stateSet);			
		} catch (Exception e) {
			addActionError( getText( "error.failedtosave" ) );
			return ERROR;
		}
		
		addActionMessage( getText( "message.buttongroupsaved" ) );
		return SUCCESS;
	}

	public Long getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(Long eventTypeId) {
		this.eventTypeId = eventTypeId;
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
		return Status.getValidEventStates();
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
			maxNumberOfImages = getConfigContext().getLong(ConfigEntry.WEB_TOTAL_INSPECTION_BUTTONS);
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
	
	// Touch affected EventTypes with criteria that use the modified stateSet.
	private void touchEventTypes(StateSet stateSet) {
		int updateCount = 0;
		String sql;
		Query query;
		OpenSecurityFilter securityFilter = new OpenSecurityFilter();
		QueryBuilder<OneClickCriteria> criteriaQuery = new QueryBuilder<OneClickCriteria>(OneClickCriteria.class, securityFilter);
		criteriaQuery.addWhere(WhereClauseFactory.create("states.id", stateSet.getId()));
		List<OneClickCriteria> criterias = persistenceManager.findAll(criteriaQuery);			
		for(OneClickCriteria criteria : criterias) {
			sql = "SELECT cs FROM " + CriteriaSection.class.getName() + " cs JOIN cs.criteria csc WHERE csc.id = :criteriaId";
			query = persistenceManager.getEntityManager().createQuery(sql, CriteriaSection.class);
			query.setParameter("criteriaId", criteria.getId());
			List<CriteriaSection> sections = query.getResultList();
			for(CriteriaSection section : sections) {
				sql = "SELECT f FROM " + EventForm.class.getName() + " f JOIN f.sections s WHERE s.id = :sectionId";
				query = persistenceManager.getEntityManager().createQuery(sql, EventForm.class);
				query.setParameter("sectionId", section.getId());
				List<EventForm> forms = query.getResultList();
				for(EventForm form : forms) {
					QueryBuilder<EventType> eventTypeQuery = new QueryBuilder<EventType>(EventType.class, securityFilter);
					eventTypeQuery.addWhere(WhereClauseFactory.create(Comparator.EQ, "eventForm.id", form.getId()));
					List<EventType> eventTypes = persistenceManager.findAll(eventTypeQuery);
					for(EventType eventType : eventTypes) {
						eventType.touch();
						persistenceManager.update(eventType);
						updateCount++;
					}
				}
			}
		}
		logger.info("Touched " + updateCount + " EventTypes.");
	}
}
