package com.n4systems.fieldid.viewhelpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.utils.ListHelper;
import com.n4systems.model.AbstractInspection;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Inspection;
import com.n4systems.model.Observation;
import com.n4systems.model.State;
import com.n4systems.model.Tenant;

/**
 * A helper class for the InspectionCrud and SubInspectionCrud.  Consolidates form processing, 
 * and pre-save/update logic into a single place.
 */
public class InspectionHelper {
	private final PersistenceManager pm;
	
	public InspectionHelper(final PersistenceManager pm) {
		this.pm = pm;
	}
	
	/**
	 * Converts the CriteriaResult Set on an Inspection into a List in the 
	 * order it would appear on a form (by CriteriaSection, then Criteria).
	 * @param inspection	The inspection to find CriteriaResults from
	 * @return				A List of ordered CriteriaResults.  Returns an empty List if the inspection's results are empty.
	 */
	public List<CriteriaResult> orderCriteriaResults(AbstractInspection inspection) {
		List<CriteriaResult> orderedResults = new ArrayList<CriteriaResult>();
		
		// don't go through all the trouble if the inspection has no results.
		if (inspection != null && !inspection.getResults().isEmpty()) {
			// go through the InspectionType's sections and criteria, hunting down the corresponding CriteriaResult.
			for(CriteriaSection section: inspection.getType().getSections()) {
				for(Criteria criteria: section.getCriteria()) {
					// find the corresponding CriteriaResult on the Inspection
					for(CriteriaResult result: inspection.getResults()) {
						if (result.getCriteria().equals(criteria)) {
							// we also need to order our observations before adding this criteria
							orderedResults.add(orderObservations(result));
							break;
						}
					}
				}
			}
		}
		
		return orderedResults;
	}
	
	/**
	 * Reorders both the recommendations and deficiencies on a CriteriaResult.
	 * @see #orderObservations(List, List)
	 * @param result	A CriteriaResult
	 * @return			The modified result for Chaining
	 */
	public CriteriaResult orderObservations(CriteriaResult result) {
		
		result.setRecommendations(orderObservations(result.getRecommendations(), result.getCriteria().getRecommendations()));
		result.setDeficiencies(orderObservations(result.getDeficiencies(), result.getCriteria().getDeficiencies()));
		
		return result;
	}
	
	/**
	 * Reorders the observations so that they match the exact order of the observationTexts.  If there is no matching
	 * observation for an observationText, a null is inserted as a place holder.  We do this to put them 
	 * back into a form that can be more easily handled by the templates.
	 * @param observations		A list of Observations
	 * @param observationTexts	A list of Observation texts
	 * @return					A reordered list of Observations
	 */
	public <T extends Observation> List<T> orderObservations(List<T> observations, List<String> observationTexts) {
		List<T> reOrderedObservations = new ArrayList<T>(observationTexts.size() + 1);
		
		// for each observationText, hunt down it's corresponding observation and add it to the list.
		// if one can't be found, add a null in the list as a placeholder
		for (String observationText: observationTexts) {
			reOrderedObservations.add(getObservationForText(observations, observationText));
		}
		
		// finally we need to add the comment in the last place
		reOrderedObservations.add(getCommentObservation(observations));
		
		return reOrderedObservations;
	}
	
	/**
	 * Processes CriteriaResults coming back from a create inspection form.  These results will only have the id of the Criteria and State on
	 * them.  Each skeleton CriteriaResult is processed into a full result by looking up the Criteria and State by id, and then setting the inspection
	 * and tenant fields.  The results Observations are then processed before adding it to the inspeciton's result list.  When complete
	 * inspection will have it's full list of processed CriteriaResults attached.
	 * @see #processObservations(CriteriaResult, UserBean)
	 * @param inspection			An AbstractInspection (sub or master)
	 * @param formCriteriaResults	CriteriaResults input from a form.
	 * @param modifiedBy			A modifiedBy user to set on the Observations
	 * @param pm					A PersistenceManager instance
	 */
	public void processFormCriteriaResults(AbstractInspection inspection, List<CriteriaResult> formCriteriaResults, UserBean modifiedBy) {
		// if our forms criteria results are null (as is the case with a repair), just stop.  
		if (formCriteriaResults == null) {
			return;
		}
		
		// since we'll add later, this list should be empty
		inspection.getResults().clear();
		
		CriteriaResult realResult;
		// process the criteria and state lists. and attach to the real objects.
		for (CriteriaResult formResult: formCriteriaResults) {
			
			// clean the observations for this result
			processObservations(formResult, inspection.getTenant(), modifiedBy);
			
			// on edit the results will come in with an id
			// we want to load the real result and then modify it's fields
			if (formResult.getId() != null) {
				realResult = pm.find(CriteriaResult.class, formResult.getId());
				
				reconstructEditObservations(realResult.getRecommendations(), formResult.getRecommendations());
				reconstructEditObservations(realResult.getDeficiencies(), formResult.getDeficiencies());
				
			} else {
				// this is a new result, need to set the basics
				realResult = new CriteriaResult();
				realResult.setInspection(inspection);
				realResult.setTenant(inspection.getTenant());
				
				realResult.setDeficiencies(formResult.getDeficiencies());
				realResult.setRecommendations(formResult.getRecommendations());
			}
			
			// these get re-attached every time
			realResult.setCriteria(pm.find(Criteria.class, formResult.getCriteria().getId(), inspection.getTenant()));
			realResult.setState(pm.find(State.class, formResult.getState().getId(), inspection.getTenant()));
			
			// and attach back onto the inspection
			inspection.getResults().add(realResult);
		}
	}
	
	/**
	 * Ensures nulls have been removed from the lists, sets security information
	 * on the Observations, and ensures Recommendations/Deficiencies only
	 * contain elements if they're allowed to by the results Status
	 * (NA/PASS/FAIL)<br/> This should be called on each result prior to
	 * persisting an inspection.
	 * 
	 * @param result		A CriteriaResult
	 * @param modifiedBy	A modifiedBy user to set on the result
	 */
	public void processObservations(CriteriaResult result, Tenant tenant, UserBean modifiedBy) {
		// these lists can have nulls in them
		ListHelper.clearNulls(result.getDeficiencies());
		ListHelper.clearNulls(result.getRecommendations());

		// remove blank comments
		clearBlankObservationComment(result.getDeficiencies());
		clearBlankObservationComment(result.getRecommendations());

		ListHelper.setSecurity(result.getDeficiencies(), tenant, modifiedBy);
		ListHelper.setSecurity(result.getRecommendations(), tenant, modifiedBy);
	}

	/**
	 * Reconstructs a list of 'real' Observations from a list of 'form' Observations.  When finished,
	 * realObservations will have been updated with new observations from formObservations, had any
	 * observations removed that did not exist in formObservations and had states and comments 
	 * updated on existing observations ... it's actually way simpler then I just made it sound ...
	 * @param realObservations	A list of Observations loaded from a CriteriaResult entity
	 * @param formObservations	A list of Observations as input from the Action
	 */
	public <T extends Observation> void reconstructEditObservations(List<T> realObservations, List<T> formObservations) {
		
		// we need to capture our old observations before we clear them out
		Map<Long, T> origObservationMap = new HashMap<Long, T>(realObservations.size());
		for (T observation: realObservations) {
			origObservationMap.put(observation.getId(), observation);
		}
		
		// clear the observations so that any removed ones go away
		realObservations.clear();
		
		T observation;
		for (T formObservation: formObservations) {
			if (formObservation.getId() != null) {
				// if the observation has an id, look it up and update it
				observation = origObservationMap.get(formObservation.getId());
				observation.setModifiedBy(formObservation.getModifiedBy());
				
				if (formObservation.getState().equals(Observation.State.COMMENT)) {
					// comments need their text updated
					observation.setText(formObservation.getText());
				} else {
					// all others simply get their state updated
					observation.setState(formObservation.getState());
				}
				
			} else {
				// this observation is new, let's just add it.
				observation = formObservation;
			}
			
			realObservations.add(observation);
		}
	}
	
	/**
	 * Removes the last Observation with state COMMENT and zero length text.
	 * Used in processObservations.
	 * 
	 * @param observations	A list of observations.
	 */
	public <T extends Observation> void clearBlankObservationComment(List<T> observations) {
		// we're walking the array backwards since the Comment is almost always
		// the last in the list
		for (int i = observations.size() - 1; i >= 0; i--) {
			if (observations.get(i).getState() == Observation.State.COMMENT) {
				// observation is a Comment
				if (observations.get(i).getText().trim().length() == 0) {
					// trimmed text is zero length
					observations.remove(i);
				}
				// found the comment, we're done
				break;
			}
		}
	}
	
	/**
	 * Finds a Criteria from an Inspection's InspectionType, by Id.
	 * @param inspection	Inspection to find Criteria from
	 * @param criteriaId	Id of a Criteria
	 * @return				The Criteria or null if one could not be located
	 */
	public Criteria findCriteriaOnInspectionType(Inspection inspection, Long criteriaId) {
		// first we need to hunt down our criteria by id
		Criteria criteria = null;
		for (CriteriaSection sec: inspection.getType().getSections()) {
			for (Criteria crit: sec.getCriteria()) {
				if (crit.getId().equals(criteriaId)) {
					criteria = crit;
					break;
				}
			}
		}
		return criteria;
	}
	
	/**
	 * Given a list of CriteriaResults, finds the CriteriaResult with a Criteria.id matching criteriaId
	 * @param criteriaId	Id of a Criteria
	 * @return				The found CriteriaResult or null
	 */
	public CriteriaResult findResultInCriteriaResultsByCriteriaId(List<CriteriaResult> results, Long criteriaId) {
		CriteriaResult result = null;
		for (CriteriaResult critRes: results) {
			if (critRes.getCriteria().getId().equals(criteriaId)) {
				result = critRes;
				break;
			}
		}
		return result;
	}
	
	/**
	 * Given a list of Observation, hunts down the first Observation with a State of COMMENT
	 * @param observations	A list of observations
	 * @return				A comment Observation or null 
	 */
	public <T extends Observation> T getCommentObservation(List<T> observations) {
		T observation = null;
		for (T obs: observations) {
			if (obs.getState().equals(Observation.State.COMMENT)) {
				observation = obs;
				break;
			}
		}
		return observation;
	}
	
	/**
	 * Given a list of Observations and it's text, hunts down the first Observation with matching text.
	 * @param observations		A list of observations
	 * @param observationText	Text of the Observation
	 * @return					An Observation or null 
	 */
	public <T extends Observation> T getObservationForText(List<T> observations, String observationText) {
		T observation = null;
		for (T obs: observations) {
			if (obs.getText().equals(observationText)) {
				observation = obs;
				break;
			}
		}
		return observation;
	}
	
	/**
	 * Given a list of Observations, counts the number of not null Observation.
	 * @see ListHelper#countNotNull(java.util.Collection)
	 * @param observations		A list of observations
	 * @return					The number of observations in the list.
	 */
	public <T extends Observation> int countObservations(List<T> observations) {
		return ListHelper.countNotNull(observations);
	}
}
