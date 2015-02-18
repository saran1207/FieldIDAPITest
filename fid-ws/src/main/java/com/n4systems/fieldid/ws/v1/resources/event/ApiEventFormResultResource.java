package com.n4systems.fieldid.ws.v1.resources.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.event.ObservationCountService;
import com.n4systems.fieldid.ws.v1.exceptions.InternalErrorException;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventSchedule;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventScheduleResource;
import com.n4systems.model.*;
import org.apache.commons.lang.NullArgumentException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ApiEventFormResultResource extends FieldIdPersistenceService {
	@Autowired private ApiEventScheduleResource apiEventScheduleResource;
    @Autowired private ObservationCountService observationCountService;

    private static final Logger logger = Logger.getLogger(ApiEventFormResultResource.class);
	
	public List<CriteriaResult> convertApiEventFormResults(ApiEventFormResult eventFormResult, EventForm form, AbstractEvent event) {
		List<CriteriaResult> results = new ArrayList<>();

        //Only add the results if the form isn't hidden... otherwise, the CriteriaResults - should they exist - must go.
        //Making the "go" should theoretically be as easy as not including them in the JPA model.
        eventFormResult.getSections()
                       .stream()
                       .filter(sectionResult -> !sectionResult.isHidden())
                       .forEach(sectionResult -> results.addAll(convertSectionResult(form, sectionResult, event)));

		return results;
	}

	private List<CriteriaResult> convertSectionResult(EventForm form, ApiCriteriaSectionResult sectionResult, AbstractEvent event) {
        CriteriaSection section = null;
		for (CriteriaSection sect: form.getSections()) {
			if (sectionResult.getSectionId().equals(sect.getId())) {
				section = sect;
				break;
			}
		}
		
		if (section == null) {
			throw new NotFoundException("CriteriaSection", sectionResult.getSectionId());
		}
		
		List<CriteriaResult> results = new ArrayList<CriteriaResult>();
		for (ApiCriteriaResult apiResult: sectionResult.getCriteria()) {
			results.add(convertCriteriaResult(section, apiResult, event));
		}
		return results;
	}
	
	private CriteriaResult convertCriteriaResult(CriteriaSection section, ApiCriteriaResult apiResult, AbstractEvent event) {
		Criteria criteria = null;
		for (Criteria crit: section.getCriteria()) {
			if (apiResult.getCriteriaId().equals(crit.getId())) {
				criteria = crit;
				break;
			}
		}
		
		if (criteria == null) {
			throw new NotFoundException("Criteria", apiResult.getCriteriaId());
		}
		
		CriteriaResult result = null;
		switch (criteria.getCriteriaType()) {
			case ONE_CLICK:
				result = new OneClickCriteriaResult();
				Button button = persistenceService.find(Button.class, apiResult.getOneClickValue());
				((OneClickCriteriaResult) result).setButton(button);
				break;
			case TEXT_FIELD:
				result = new TextFieldCriteriaResult();
				((TextFieldCriteriaResult) result).setValue(apiResult.getTextValue());
				break;
			case COMBO_BOX:
				result = new ComboBoxCriteriaResult();
				((ComboBoxCriteriaResult) result).setValue(apiResult.getComboBoxValue());
				break;
			case SELECT:
				result = new SelectCriteriaResult();
				((SelectCriteriaResult) result).setValue(apiResult.getSelectValue());
				break;
			case UNIT_OF_MEASURE:
				result = new UnitOfMeasureCriteriaResult();
				((UnitOfMeasureCriteriaResult) result).setPrimaryValue(apiResult.getUnitOfMeasurePrimaryValue());
				((UnitOfMeasureCriteriaResult) result).setSecondaryValue(apiResult.getUnitOfMeasureSecondaryValue());
				break;
			case SIGNATURE:
				result = new SignatureCriteriaResult();
				((SignatureCriteriaResult) result).setSigned(apiResult.getSignatureValue() != null);
				((SignatureCriteriaResult) result).setImage(apiResult.getSignatureValue());
				break;
			case DATE_FIELD:
				result = new DateFieldCriteriaResult();
				((DateFieldCriteriaResult) result).setValue(apiResult.getDateValue());
				break;
			case SCORE:
				result = new ScoreCriteriaResult();
				Long scoreValue = apiResult.getScoreValue();
				
				if(scoreValue == null)
					throw new NullArgumentException("Score value cannot be null. Client need to set a value.");
				
				((ScoreCriteriaResult) result).setScore(persistenceService.find(Score.class, scoreValue));
				break;
			case NUMBER_FIELD:
				result = new NumberFieldCriteriaResult();
				((NumberFieldCriteriaResult) result).setValue(apiResult.getNumberValue());
				break;
            case OBSERVATION_COUNT:
                //NOTE: The assumption here is that we don't need a mobileGUID, since we can more or less get the same
                //      information by extrapolation.  We know that there can only be one of any given type of
                //      ObservationCount for an ObservationCountCriteriaResult.  We know that these are tied to
                //      ObservationCountResult objects, which lack a mobileGUID directly.  However, we can use the ID
                //      from the ObservationCount and ApiObservationCount objects to determine relationships.
                result = observationCountService.getObservationCountCriteriaResultByMobileId(apiResult.getSid());

                List<ObservationCountResult> observationCountResults = new ArrayList<>();

                //If we get a null back, this means that these CriteriaResults haven't been recorded yet!!
                if(result == null) {
                    //New Entry Mode... this is where we build the data from scratch, more or less.

                    result = new ObservationCountCriteriaResult();

                    apiResult.getObservationCountValue().forEach(apiObservationCountResult -> {
                        //For each of these objects, we need to process it into the new data structure.  This is made
                        //easy by the fact that we should just be able to load up the ReadOnly portion of the model
                        //directly.
                        ObservationCount observationCount = persistenceService.find(ObservationCount.class, apiObservationCountResult.getObservationCount().getSid());

                        //Now we need to build the result...
                        ObservationCountResult observationCountResult = new ObservationCountResult();
                        observationCountResult.setObservationCount(observationCount); //We'll just put this here...
                        observationCountResult.setValue(apiObservationCountResult.getValue()); //...and take that value.

                        //After all of this, we bold it on to the result, like so:
                        observationCountResults.add(observationCountResult);
                    });
                } else {
                    //Existing Work Mode... this is where we have to carefully match provided data with existing data.
                    //We will try to replace the whole list, if that is possible...

                    ((ObservationCountCriteriaResult) result).getObservationCountResults()
                         .forEach(observationCountResult -> {
                             //Trust me, it looks like we're processing more than one entry below, but we're not.  Just like
                             //highlander, there can be only one.
                             apiResult.getObservationCountValue()
                                     .stream()
                                     //Notice how we're filtering in a way that makes it impossible to return more than
                                     //one result (unless the data is corrupt, in which case this might have problems).
                                     .filter(apiObservationCountResult -> apiObservationCountResult.getObservationCount().getSid().equals(observationCountResult.getId()))
                                     .forEach(apiObservationCountResult -> observationCountResult.setValue(apiObservationCountResult.getValue()));

                             observationCountResults.add(observationCountResult);
                         });

                }

                ((ObservationCountCriteriaResult) result).setObservationCountResults(observationCountResults);

                break;
			default:
                logger.error("Unhandled Criteria type: " + result.getCriteria().getCriteriaType().name());
				throw new InternalErrorException("Unhandled Criteria type: " + criteria.getCriteriaType().name());
		}
		result.setMobileId(apiResult.getSid());
		result.setEvent(event);
		result.setCriteria(criteria);
		result.setTenant(event.getTenant());
		
		for (ApiObservation rec: apiResult.getRecommendations()) {
			result.getRecommendations().add(convertRecommenation(rec, event.getTenant()));
		}
		
		for (ApiObservation def: apiResult.getDeficiencies()) {
			result.getDeficiencies().add(convertDeficiency(def, event.getTenant()));
		}
		
		for(ApiEventSchedule action: apiResult.getActions()) {
			Event actionEvent = apiEventScheduleResource.converApiEventSchedule(action);
			result.getActions().add(actionEvent);
		}
		
		return result;
	}
	
	private Recommendation convertRecommenation(ApiObservation observation, Tenant tenant) {
		Recommendation recommendation = new Recommendation();
		recommendation.setMobileId(observation.getSid());
		recommendation.setText(observation.getText());
		recommendation.setTenant(tenant);
		recommendation.setState(Observation.State.valueOf(observation.getState()));
		return recommendation;
	}
	
	private Deficiency convertDeficiency(ApiObservation observation, Tenant tenant) {
		Deficiency deficiency = new Deficiency();
		deficiency.setMobileId(observation.getSid());
		deficiency.setText(observation.getText());
		deficiency.setTenant(tenant);
		deficiency.setState(Observation.State.valueOf(observation.getState()));
		return deficiency;
	}
}
