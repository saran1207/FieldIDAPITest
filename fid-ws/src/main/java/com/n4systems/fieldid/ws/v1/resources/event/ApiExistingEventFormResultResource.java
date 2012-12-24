package com.n4systems.fieldid.ws.v1.resources.event;

import java.util.Hashtable;

import com.n4systems.model.*;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v1.exceptions.InternalErrorException;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventSchedule;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventScheduleResource;
import com.n4systems.model.Button;

public class ApiExistingEventFormResultResource extends FieldIdPersistenceService {
	@Autowired private ApiEventScheduleResource apiEventScheduleResource;
	
	public void convertApiEventFormResults(ApiEventFormResult eventFormResult, AbstractEvent event) {
		// Flatten ApiEventFormResult Sections into a Hashtable so it can be looked up easily by mobileGuid.
		Hashtable<String, ApiCriteriaResult> apiCriteriaResults = new Hashtable<String, ApiCriteriaResult>();
		for(ApiCriteriaSectionResult apiSectionResult : eventFormResult.getSections()) {
			for(ApiCriteriaResult apiCriteriaResult : apiSectionResult.getCriteria()) {
				apiCriteriaResults.put(apiCriteriaResult.getSid(), apiCriteriaResult);
			}
		}
		
		for(CriteriaResult criteriaResult : event.getResults()) {
			convertApiCriteriaResult(apiCriteriaResults.get(criteriaResult.getMobileId()), criteriaResult, event);
		}
	}
	
	private void convertApiCriteriaResult(ApiCriteriaResult apiResult, CriteriaResult result, AbstractEvent event) {
		switch (result.getCriteria().getCriteriaType()) {
			case ONE_CLICK:
				Button button = persistenceService.find(Button.class, apiResult.getOneClickValue());
				((OneClickCriteriaResult) result).setButton(button);
				break;
			case TEXT_FIELD:
				((TextFieldCriteriaResult) result).setValue(apiResult.getTextValue());
				break;
			case COMBO_BOX:
				((ComboBoxCriteriaResult) result).setValue(apiResult.getComboBoxValue());
				break;
			case SELECT:
				((SelectCriteriaResult) result).setValue(apiResult.getSelectValue());
				break;
			case UNIT_OF_MEASURE:
				((UnitOfMeasureCriteriaResult) result).setPrimaryValue(apiResult.getUnitOfMeasurePrimaryValue());
				((UnitOfMeasureCriteriaResult) result).setSecondaryValue(apiResult.getUnitOfMeasureSecondaryValue());
				break;
			case SIGNATURE:
				((SignatureCriteriaResult) result).setSigned(apiResult.getSignatureValue() != null);
				((SignatureCriteriaResult) result).setImage(apiResult.getSignatureValue());
				break;
			case DATE_FIELD:
				((DateFieldCriteriaResult) result).setValue(apiResult.getDateValue());
				break;
			case SCORE:		
				if(apiResult.getScoreValue() == null)
					throw new NullArgumentException("Score value cannot be null. Client need to set a value.");				
				((ScoreCriteriaResult) result).setScore(persistenceService.find(Score.class, apiResult.getScoreValue()));
				break;
			case NUMBER_FIELD:
				((NumberFieldCriteriaResult) result).setValue(apiResult.getNumberValue());
				break;
			default:
				throw new InternalErrorException("Unhandled Criteria type: " + result.getCriteria().getCriteriaType().name());
		}
		
		for (ApiObservation apiRecommendation: apiResult.getRecommendations()) {
			Recommendation recommendation = null;
			for(Recommendation existingRecommendation : result.getRecommendations()) {
				if(existingRecommendation.getMobileId().equals(apiRecommendation.getSid())) {
					recommendation = existingRecommendation;
					break;
				}
			}
			
			if(recommendation == null) {
				recommendation = new Recommendation();
				convertNewObservation(apiRecommendation, recommendation, event.getTenant());
				result.getRecommendations().add(recommendation);
			} else {
				convertExistingObservation(apiRecommendation, recommendation);
			}
		}
		
		for (ApiObservation apiDeficiency: apiResult.getDeficiencies()) {
			Deficiency deficiency = null;
			for(Deficiency existingDeficiency : result.getDeficiencies()) {
				if(existingDeficiency.getMobileId().equals(apiDeficiency.getSid())) {
					deficiency = existingDeficiency;
					break;
				}
			}
			
			if(deficiency == null) {
				deficiency = new Deficiency();
				convertNewObservation(apiDeficiency, deficiency, event.getTenant());
				result.getDeficiencies().add(deficiency);
			} else {
				convertExistingObservation(apiDeficiency, deficiency);
			}
		}
		
		for(ApiEventSchedule action: apiResult.getActions()) {
			Event actionEvent = apiEventScheduleResource.converApiEventSchedule(action);
			result.getActions().add(actionEvent);
		}
	}
	
	private void convertNewObservation(ApiObservation apiObservation, Observation observation, Tenant tenant) {
		observation.setMobileId(apiObservation.getSid());
		observation.setText(apiObservation.getText());
		observation.setTenant(tenant);
		observation.setState(Observation.State.valueOf(apiObservation.getState()));
	}
	
	private void convertExistingObservation(ApiObservation apiObservation, Observation observation) {
		observation.setText(apiObservation.getText());
		observation.setState(Observation.State.valueOf(apiObservation.getState()));
	}
}
