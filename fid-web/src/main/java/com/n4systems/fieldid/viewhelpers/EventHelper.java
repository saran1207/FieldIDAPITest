package com.n4systems.fieldid.viewhelpers;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.Score;
import com.n4systems.model.ScoreCriteriaResult;
import rfid.web.helper.SessionUser;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.event.viewmodel.CriteriaResultWebModel;
import com.n4systems.fieldid.actions.event.viewmodel.CriteriaResultWebModelConverter;
import com.n4systems.fieldid.actions.helpers.SessionUserDateConverter;
import com.n4systems.fieldid.actions.helpers.UserDateConverter;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.ComboBoxCriteriaResult;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.DateFieldCriteria;
import com.n4systems.model.DateFieldCriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.NumberFieldCriteriaResult;
import com.n4systems.model.Observation;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.State;
import com.n4systems.model.Tenant;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.UnitOfMeasureCriteriaResult;
import com.n4systems.model.user.User;
import com.n4systems.services.signature.SignatureService;

/**
 * A helper class for the EventCrud and SubEventCrud.  Consolidates form processing, 
 * and pre-save/update logic into a single place.
 */
public class EventHelper {
	private final PersistenceManager pm;
	
	public EventHelper(final PersistenceManager pm) {
		this.pm = pm;
	}
	
	/**
	 * Converts the CriteriaResult Set on an Event into a List in the
	 * order it would appear on a form (by CriteriaSection, then Criteria).
	 * @param event	The event to find CriteriaResults from
	 * @param user 
	 * @return				A List of ordered CriteriaResults.  Returns an empty List if the event's results are empty.
	 */
	public List<CriteriaResultWebModel> orderCriteriaResults(AbstractEvent event, SessionUser user) {
		List<CriteriaResult> orderedResults = new ArrayList<CriteriaResult>();
		
		// don't go through all the trouble if the event has no results.
		if (event != null && !event.getResults().isEmpty() && event.getEventForm() != null) {
			// go through the EventType's sections and criteria, hunting down the corresponding CriteriaResult.
			for(CriteriaSection section: event.getEventForm().getSections()) {
				for(Criteria criteria: section.getCriteria()) {
					// find the corresponding CriteriaResult on the Event
					for(CriteriaResult result: event.getResults()) {
						if (result.getCriteria().equals(criteria)) {
							// we also need to order our observations before adding this criteria
							orderedResults.add(orderObservations(result));
							break;
						}
					}
				}
			}
		}
		
		return convertAll(orderedResults, user);
	}

    private List<CriteriaResultWebModel> convertAll(List<CriteriaResult> results, SessionUser user) {
        List<CriteriaResultWebModel> resultWebModels = new ArrayList<CriteriaResultWebModel>(results.size());
        CriteriaResultWebModelConverter converter = new CriteriaResultWebModelConverter();
        for (CriteriaResult result : results) {
            resultWebModels.add(converter.convertToWebModel(result, user));
        }
        return resultWebModels;
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
	 * Processes CriteriaResults coming back from a create event form.  These results will only have the id of the Criteria and State on
	 * them.  Each skeleton CriteriaResult is processed into a full result by looking up the Criteria and State by id, and then setting the event
	 * and tenant fields.  The results Observations are then processed before adding it to the inspeciton's result list.  When complete
	 * event will have it's full list of processed CriteriaResults attached.
	 * @param event			An AbstractEvent (sub or master)
	 * @param formCriteriaResults	CriteriaResults input from a form.
	 * @param modifiedBy			A modifiedBy user to set on the Observations
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public void processFormCriteriaResults(AbstractEvent event, List<CriteriaResultWebModel> formCriteriaResults, User modifiedBy, SessionUser sessionUser) throws IOException, ParseException {
		// if our forms criteria results are null (as is the case with a repair), just stop.  
		if (formCriteriaResults == null) {
			return;
		}
		
		UserDateConverter dateConverter = new SessionUserDateConverter(sessionUser);
		
		// since we'll add later, this list should be empty
		event.getResults().clear();
		
		CriteriaResult realResult;
		// process the criteria and state lists. and attach to the real objects.
		for (CriteriaResultWebModel formResult: formCriteriaResults) {
			
			// clean the observations for this result
			processObservations(formResult);

            StrutsListHelper.setSecurity(formResult.getDeficiencies(), event.getTenant(), modifiedBy);
            StrutsListHelper.setSecurity(formResult.getRecommendations(), event.getTenant(), modifiedBy);

			// on edit the results will come in with an id
			// we want to load the real result and then modify its fields
			if (formResult.getId() != null) {
				realResult = pm.find(CriteriaResult.class, formResult.getId());
				
				reconstructEditObservations(realResult.getRecommendations(), formResult.getRecommendations());
				reconstructEditObservations(realResult.getDeficiencies(), formResult.getDeficiencies());
				
			} else {
				// this is a new result, need to set the basics
                realResult = new CriteriaResultWebModelConverter().convertFromWebModel(formResult, pm, event.getTenant());
				realResult.setEvent(event);
				realResult.setTenant(event.getTenant());
				
				realResult.setDeficiencies(formResult.getDeficiencies());
				realResult.setRecommendations(formResult.getRecommendations());
			}
			
			realResult.setCriteria(pm.find(Criteria.class, formResult.getCriteriaId(), event.getTenant()));
            if (realResult instanceof OneClickCriteriaResult) {
                ((OneClickCriteriaResult)realResult).setState(pm.find(State.class, formResult.getStateId(), event.getTenant()));
            } else if (realResult instanceof TextFieldCriteriaResult) {
                ((TextFieldCriteriaResult)realResult).setValue(formResult.getTextValue());
            } else if (realResult instanceof SelectCriteriaResult) {
                ((SelectCriteriaResult)realResult).setValue(formResult.getTextValue());
            } else if (realResult instanceof ComboBoxCriteriaResult) {
            	String textValue = formResult.getTextValue();
            	if(textValue.startsWith("!")) {
            		textValue = textValue.substring(1); 
            	}           		
            	((ComboBoxCriteriaResult)realResult).setValue(textValue);
            } else if (realResult instanceof UnitOfMeasureCriteriaResult) {
                ((UnitOfMeasureCriteriaResult)realResult).setPrimaryValue(formResult.getTextValue());
                ((UnitOfMeasureCriteriaResult)realResult).setSecondaryValue(formResult.getSecondaryTextValue());
            } else if (realResult instanceof SignatureCriteriaResult) {
                ((SignatureCriteriaResult)realResult).setSigned(formResult.isSigned());
                
                if (formResult.getSignatureFileId() != null) {
                	((SignatureCriteriaResult)realResult).setImage(new SignatureService().loadSignatureImage(event.getTenant().getId(), formResult.getSignatureFileId()));
                }
            } else if (realResult instanceof DateFieldCriteriaResult) {
            	Date dateResult = dateConverter.convertDate(formResult.getTextValue(), ((DateFieldCriteria)realResult.getCriteria()).isIncludeTime());
            	((DateFieldCriteriaResult) realResult).setValue(dateResult);
            } else if (realResult instanceof NumberFieldCriteriaResult) {
            	if(formResult.getTextValue() != null && !formResult.getTextValue().isEmpty()) {
            		double numberResult = Double.parseDouble(formResult.getTextValue());
            		((NumberFieldCriteriaResult)realResult).setValue(numberResult);
            	}
            } else if (realResult instanceof ScoreCriteriaResult) {
                ((ScoreCriteriaResult)realResult).setScore(pm.find(Score.class, formResult.getStateId(), event.getTenant()));
            }

			// and attach back onto the event
			event.getResults().add(realResult);
		}
	}

    /**
	 * Ensures nulls have been removed from the lists, sets security information
	 * on the Observations, and ensures Recommendations/Deficiencies only
	 * contain elements if they're allowed to by the results Status
	 * (NA/PASS/FAIL)<br/> This should be called on each result prior to
	 * persisting an event.
	 * 
	 * @param result		A CriteriaResult
	 * @param modifiedBy	A modifiedBy user to set on the result
	 */
	public void processObservations(CriteriaResultWebModel result) {
		// these lists can have nulls in them
		StrutsListHelper.clearNulls(result.getDeficiencies());
		StrutsListHelper.clearNulls(result.getRecommendations());

		// remove blank comments
		clearBlankObservationComment(result.getDeficiencies());
		clearBlankObservationComment(result.getRecommendations());
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
	 * Finds a Criteria from an Event's EventType, by Id.
	 * @param event	Event to find Criteria from
	 * @param criteriaId	Id of a Criteria
	 * @return				The Criteria or null if one could not be located
	 */
	public Criteria findCriteriaOnEventType(Event event, Long criteriaId) {
		// first we need to hunt down our criteria by id
        if (event.getType().getEventForm() == null) {
            return null;
        }
		for (CriteriaSection sec: event.getType().getEventForm().getSections()) {
			for (Criteria criteria: sec.getCriteria()) {
				if (criteria.getId().equals(criteriaId)) {
					return criteria;
				}
			}
		}
        return null;
	}
	
	/**
	 * Given a list of CriteriaResults, finds the CriteriaResult with a Criteria.id matching criteriaId
	 * @param criteriaId	Id of a Criteria
	 * @return				The found CriteriaResult or null
	 */
	public CriteriaResultWebModel findResultInCriteriaResultsByCriteriaId(List<CriteriaResultWebModel> results, Long criteriaId) {
		for (CriteriaResultWebModel critRes: results) {
			if (critRes.getCriteriaId().equals(criteriaId)) {
                return critRes;
			}
		}
		return null;
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
	 * @see StrutsListHelper#countNotNull(java.util.Collection)
	 * @param observations		A list of observations
	 * @return					The number of observations in the list.
	 */
	public <T extends Observation> int countObservations(List<T> observations) {
		return StrutsListHelper.countNotNull(observations);
	}
}
