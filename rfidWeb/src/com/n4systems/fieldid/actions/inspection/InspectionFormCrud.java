package com.n4systems.fieldid.actions.inspection;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.StateSet;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class InspectionFormCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;

	protected InspectionManager inspectionManager;
	
	private List<StateSet> stateSets;	
	protected List<CriteriaSection> criteriaSections;
	protected InspectionType inspectionType;
	
	public InspectionFormCrud(PersistenceManager persistenceManager, InspectionManager inspectionManager) {
		super(persistenceManager);
		this.inspectionManager = inspectionManager;
	}

	@Override
	protected void initMemberFields() {}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		inspectionType = persistenceManager.find( InspectionType.class, uniqueId, getTenantId(), "sections" ); 
	}

	@SkipValidation
	public String doEdit() {
		if( inspectionType == null ) {
			addActionError( getText( "error.noinspectiontype" ) );
			return MISSING;
		}
		
		criteriaSections = inspectionType.getSections();
		
		return SUCCESS;
	}
	
	public String doSave() {
		if( inspectionType == null ) {
			addActionError( getText( "error.noinspectiontype" ) );
			return MISSING;
		}
		
		processCriteriaSections();
		
		try{
			inspectionType.setSections( criteriaSections );
			inspectionManager.updateInspectionForm( inspectionType, getSessionUser().getUniqueID() );
			addFlashMessage( getText( "message.inspectionformsaved" ) );
		} catch (Exception e) {
			addActionError( getText( "error.couldnotsave" ) );
			return ERROR;
		}
		
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
	 * @param section
	 */
	private void processCriteria( CriteriaSection section ) {
		StrutsListHelper.clearNulls( section.getCriteria() );
		
		for( Criteria criteria : section.getCriteria() ) {
			criteria.setTenant( getTenant() );
			criteria.setStates( persistenceManager.find( StateSet.class, criteria.getStates().getId(), getTenantId() ) );
			
			StrutsListHelper.clearNulls(criteria.getRecommendations());
			StrutsListHelper.removeMarkedEntries("--deleted--", criteria.getRecommendations());
			
			StrutsListHelper.clearNulls(criteria.getDeficiencies());
			StrutsListHelper.removeMarkedEntries("--deleted--", criteria.getDeficiencies());
		}
	}
	
	
	public List<CriteriaSection> getCriteriaSections() {
		if( criteriaSections == null ) {
			criteriaSections = inspectionType.getSections();
		}
		return criteriaSections; 
	}

	@Validations(
			customValidators = {
					@CustomValidator( type="requiredSet", message="", key="error.sectionsrequired" ),
					@CustomValidator( type="requiredNameSet", message="", key="error.sectionnamerequired" ),
					@CustomValidator( type="requiredCriteriaName", message="", key="error.criterianamerequired" ),
					@CustomValidator( type="requiredCriteriaSets", message="", key="error.criteriarequired" ),
					@CustomValidator( type="requiredCriteriaStateSet", message="", key="error.criteriastatesetrequired" ),
					@CustomValidator( type="requiredSetsResult", message="", key="error.setsresultrequired" ) }
			)
	public void setCriteriaSections(List<CriteriaSection> criteriaSections) {
		this.criteriaSections = criteriaSections;
	}

	public List<StateSet> getStateSets() {
		if( stateSets == null ) {
			stateSets = persistenceManager.findAll( StateSet.class, getTenantId() );
		}
		return stateSets;
	}

	public InspectionType getInspectionType() {
		return inspectionType;
	}

	public boolean isNewForm() {
		return (inspectionType.getSections().isEmpty());
	}
}
