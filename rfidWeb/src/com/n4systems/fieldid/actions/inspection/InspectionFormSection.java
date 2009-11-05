package com.n4systems.fieldid.actions.inspection;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Deficiency;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Observation;
import com.n4systems.model.Recommendation;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class InspectionFormSection extends InspectionFormCrud {
	private static final long serialVersionUID = 1L;
	
	private Long sectionIndex;	
	private Long criteriaIndex;
	private Long observationIndex;
	private Observation.Type observationType;

	public InspectionFormSection(PersistenceManager persistenceManager, InspectionManager inspectionManager ) {
		super(persistenceManager, inspectionManager);
	}

	public Long getSectionIndex() {
		return sectionIndex;
	}

	public void setSectionIndex(Long sectionIndex) {
		this.sectionIndex = sectionIndex;
	}

	public Long getCriteriaIndex() {
		return criteriaIndex;
	}

	public void setCriteriaIndex(Long criteriaIndex) {
		this.criteriaIndex = criteriaIndex;
	}

	public String getObservationType() {
		return observationType.name();
	}

	public void setObservationType(String observationType) {
		this.observationType = Observation.Type.valueOf(observationType);
	}
	
	private CriteriaSection getSection(long sectionIdx) {
		return getCriteriaSections().get((int)sectionIdx);
	}
	
	private Criteria getCriteria(long sectionIdx, long criteriaIdx) {
		return getSection(sectionIdx).getCriteria().get((int)criteriaIdx);
	}
	
	public Long getObservationIndex() {
		return observationIndex;
	}
	
	public void setObservationIndex(Long observationIndex) {
		this.observationIndex = observationIndex;
	}

	/**
	 * Adds default {@link CriteriaSection}s, {@link Criteria} and {@link Observation}s ({@link Recommendation}/{@link Deficiency}) to an {@link InspectionType}.<br />
	 * Since this is an ajax method, and the InspectionType is loaded on every ajax request, it is necessary to create new Sections, Criteria (and so on) up to the requested index
	 * such that:<br />
	 * Given a <tt>sectionIndex</tt>; new, default CriteriaSections will be added so that <tt>inspectionType.getSections().size() == sectionIndex + 1</tt>.<br />
	 * Given a <tt>criteriaIndex</tt>; new, default Criteria will be added so that <tt>criteriaSection.getCriteria().size() == criteriaIndex + 1</tt>.<br />
	 * Given an <tt>observationIndex</tt> and <tt>observationType</tt>; new, empty strings will be added to the recomendation or deficiency list (depending on <tt>observationType</tt>)
	 * such that <tt>criteria.get</tt>(Recomendations or Deficiencies)<tt>.size() == observationIndex + 1</tt>.
	 * 
	 * @see InspectionFormCrud
	 * @see InspectionFormCrud#loadMemberFields(Long)
	 * 
	 * @return MISSING if no InspectionType could be found for the given uniqueID.
	 */
	@SkipValidation
	public String doAdd() {
		if( inspectionType == null ) {
			addActionError( getText( "error.noinspectiontype" ) );
			return MISSING;
		}
		
		// create default CriteriaSections up to sectionIndex
		for(long i = getCriteriaSections().size(); i <= sectionIndex; i++) {
			getCriteriaSections().add((int)i, new CriteriaSection());
		}
		
		// if criteriaIndex is set, then we also need to create Criteria
		if( criteriaIndex != null  ) {
			// get the targeted section.  Note that this may have just been created above.
			CriteriaSection section = getSection(sectionIndex);
			
			// create default Criteria up to criteriaIndex
			for(long i = section.getCriteria().size(); i <= criteriaIndex; i++) {
				section.getCriteria().add((int)i, new Criteria());
			}
			
			/*
			 * Now we need to add any recommendations or deficiencies.  We'll do this the same as sections and criteria 
			 * but first we'll use the observationType to figure out which one we're dealing with.  Since you can only
			 * add either a recommendation or deficiency per ajax request, we use a single index (observationIndex)
			 * to figure out the last index.  aka observationIndex changes what list we're targeting
			 * depending on the observationType.
			 */
			if(observationIndex != null && observationType != null) {
				// get the targeted criteria.  Note that this may have just been created above.
				Criteria criteria = getCriteria(sectionIndex, criteriaIndex);
				
				// decide which list to target
				List<String> observations = null;
				switch(observationType) {
					case RECOMMENDATION:
						observations = criteria.getRecommendations();
						break;
					case DEFICIENCY:
						observations = criteria.getDeficiencies();
						break;
				}
				
				// now add our empty strings to the selected list
				for(long i = observations.size(); i <= observationIndex; i++) {
					observations.add( (int)i, "" );
				}
			}
		}
		
		return SUCCESS;
	}
}