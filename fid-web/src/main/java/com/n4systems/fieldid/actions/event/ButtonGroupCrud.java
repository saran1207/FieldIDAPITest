package com.n4systems.fieldid.actions.event;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.*;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.security.Permissions;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import javax.persistence.Query;
import java.util.List;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class ButtonGroupCrud extends AbstractCrud implements HasDuplicateValueValidator{

	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ButtonGroupCrud.class);
	
	private ButtonGroup buttonGroup;
	private Long eventTypeId;
	private Long buttonGroupIndex;
	private Long buttonIndex;
	
	private List<ButtonGroup> buttonGroups;
	
	private Long maxNumberOfImages;
	
	public ButtonGroupCrud( PersistenceManager persistenceManager ) {
		super(persistenceManager);
	}
	
	@Override
	protected void initMemberFields() {
		buttonGroup = new ButtonGroup();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		buttonGroup = persistenceManager.find( ButtonGroup.class, uniqueId, getTenantId() );
	}
	
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doAddButton() {
		if( buttonGroup == null ) {
			addActionError( getText( "error.statesetnotfound" ) );
			return MISSING;
		}
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doAdd() {
		if( buttonGroup == null ) {
			addActionError( getText( "error.statesetnotfound" ) );
			return MISSING;
		}
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doEdit() {
		if( buttonGroup == null || buttonGroup.getId() == null ) {
			addActionError( getText( "error.statesetnotfound" ) );
			return MISSING;
		}
		return SUCCESS;
	}
	
	@SkipValidation
	public String doRetire() {
		if( buttonGroup == null || buttonGroup.getId() == null ) {
			addActionError( getText( "error.statesetnotfound" ) );
			return MISSING;
		}
		return SUCCESS;
	}
	
	
	public String doSave() {
		if( buttonGroup == null ) {
			addActionError( getText( "error.statesetnotfound" ) );
			return MISSING;
		}
		
		StrutsListHelper.clearNulls( buttonGroup.getButtons() );
		StrutsListHelper.setTenants( buttonGroup.getButtons(), getTenant() );
				
		buttonGroup.setTenant( getTenant() );
		
		try {
			buttonGroup = persistenceManager.update(buttonGroup);
			logger.info("Updated ButtonGroup (ButtonGroup): " + buttonGroup.getName());
			touchEventTypes(buttonGroup);
		} catch (Exception e) {
            logger.error("Error updating event types?", e);
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

	public ButtonGroup getButtonGroup() {
			return buttonGroup;
	}

	public List<ButtonGroup> getButtonGroups() {
		if( buttonGroups == null ) {
			buttonGroups = persistenceManager.findAll( ButtonGroup.class, getTenantId() );
		}
		return buttonGroups;
	}

	public List<EventResult> getButtonStatuses() {
		return EventResult.getValidEventResults();
	}
		
	public String getName() {
		return buttonGroup.getName();
	}
	
	
	@RequiredStringValidator( message="", key="error.groupnamerequired")
	@CustomValidator(type="uniqueValue", message = "", key="error.groupnameunique")
	public void setName( String name ) {
		buttonGroup.setName( name );
	}
	
	public List<Button> getButtons(){
		return buttonGroup.getButtons();
	}
	
	
	@Validations(
			customValidators = {
					@CustomValidator(type="requiredSet", message = "", key="error.buttonsempty"),
					@CustomValidator( type="requiredNameSet", message="", key="error.buttonsrequirenames" ) 
					}
			)
	public void setButtons(List<Button> buttons) {
		if( buttonGroup != null ){
			buttonGroup.setButtons(buttons);
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
		return !persistenceManager.uniqueNameAvailable( ButtonGroup.class, formValue, uniqueID, getTenantId() );
	}

	public Long getButtonIndex() {
		return buttonIndex;
	}

	public void setButtonIndex( Long buttonIndex ) {
		this.buttonIndex = buttonIndex;
	}
	
	// Touch affected EventTypes with criteria that use the modified buttonGroup.
	private void touchEventTypes(ButtonGroup buttonGroup) {
		int updateCount = 0;
		String sql;
		Query query;
		OpenSecurityFilter securityFilter = new OpenSecurityFilter();
		QueryBuilder<OneClickCriteria> criteriaQuery = new QueryBuilder<OneClickCriteria>(OneClickCriteria.class, securityFilter);
		criteriaQuery.addWhere(WhereClauseFactory.create("buttonGroup.id", buttonGroup.getId()));
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
