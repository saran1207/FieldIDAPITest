package com.n4systems.fieldid.actions.event;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.model.ButtonGroup;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventType;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.Collections;
import java.util.List;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class EventFormCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;

	protected EventManager eventManager;
	
	private List<ButtonGroup> buttonGroups;
	protected List<CriteriaSection> criteriaSections;
	protected EventType eventType;
	
	public EventFormCrud(PersistenceManager persistenceManager, EventManager eventManager) {
		super(persistenceManager);
		this.eventManager = eventManager;
	}

	@Override
	protected void initMemberFields() {}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		eventType = persistenceManager.find( EventType.class, uniqueId, getTenantId(), "eventForm.sections" );
	}

	@SkipValidation
	public String doEdit() {
		if( eventType == null ) {
			addActionError( getText( "error.noeventtype" ) );
			return MISSING;
		}
		
		criteriaSections = eventType.getEventForm() != null ? eventType.getEventForm().getSections() : Collections.<CriteriaSection>emptyList(); ;
		
		return SUCCESS;
	}

	/**
	 * this will remove nulls that have come in from the from so that they don't get stored in the db.
	 * also assign the right tenant.
	 */
	private void processCriteriaSections() {
		StrutsListHelper.clearNulls( criteriaSections );
		
		for( CriteriaSection section : criteriaSections ) {
			section.setTenant( getTenant() );
			processCriteria( section );
		}
	}

	/**
	 * clears out the nulls for criteria that can come from the form.
	 * also assign the right tenant.
	 * @param sectionaddCriteriaUrl
	 */
	private void processCriteria( CriteriaSection section ) {
		StrutsListHelper.clearNulls( section.getCriteria() );
		
		for( Criteria criteria : section.getCriteria() ) {
			criteria.setTenant( getTenant() );
//			criteria.setStates( persistenceManager.find( ButtonGroup.class, criteria.getStates().getId(), getTenantId() ) );
			
			StrutsListHelper.clearNulls(criteria.getRecommendations());
			StrutsListHelper.removeMarkedEntries("--deleted--", criteria.getRecommendations());
			
			StrutsListHelper.clearNulls(criteria.getDeficiencies());
			StrutsListHelper.removeMarkedEntries("--deleted--", criteria.getDeficiencies());
		}
	}
	
	
	public List<CriteriaSection> getCriteriaSections() {
		if( criteriaSections == null) {
            if (eventType.getEventForm() != null) {
                criteriaSections = eventType.getEventForm().getSections();
            } else {
                criteriaSections = Collections.emptyList();
            }
		}
		return criteriaSections; 
	}

	@Validations(
			customValidators = {
					@CustomValidator( type="requiredSet", message="", key="error.sectionsrequired" ),
					@CustomValidator( type="requiredNameSet", message="", key="error.sectionnamerequired" ),
					@CustomValidator( type="requiredCriteriaName", message="", key="error.criterianamerequired" ),
					@CustomValidator( type="requiredCriteriaSets", message="", key="error.criteriarequired" ),
					@CustomValidator( type="requiredCriteriaStateSet", message="", key="error.criteriastatesetrequired" )}
			)
	public void setCriteriaSections(List<CriteriaSection> criteriaSections) {
		this.criteriaSections = criteriaSections;
	}

	public List<ButtonGroup> getButtonGroups() {
		if( buttonGroups == null ) {
			buttonGroups = persistenceManager.findAll( ButtonGroup.class, getTenantId() );
		}
		return buttonGroups;
	}

	public EventType getEventType() {
		return eventType;
	}

	public boolean isNewForm() {
		return (eventType.getEventForm() == null || eventType.getEventForm().getSections().isEmpty());
	}
	
	public String getName(){
		return eventType.getName();
	}
}
