package com.n4systems.fieldid.ws.v1.resources.savedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v1.exceptions.InternalErrorException;
import com.n4systems.fieldid.ws.v1.resources.event.ApiCriteriaResult;
import com.n4systems.fieldid.ws.v1.resources.event.ApiObservation;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiCriteriaSection;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiEventForm;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiEventTypeResource;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiCriteria;
import com.n4systems.model.ComboBoxCriteriaResult;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.DateFieldCriteriaResult;
import com.n4systems.model.Deficiency;
import com.n4systems.model.Event;
import com.n4systems.model.NumberFieldCriteriaResult;
import com.n4systems.model.Observation;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.Recommendation;
import com.n4systems.model.ScoreCriteriaResult;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.UnitOfMeasureCriteriaResult;

public class ApiSavedEventFormResource extends FieldIdPersistenceService{
	@Autowired private ApiEventTypeResource eventTypeResource;

	public ApiEventForm convertToApiEventForm(Event event) {
		if(event.getEventForm() != null) {
			List<CriteriaSection> sections = event.getEventForm().getAvailableSections();
			
			if(sections != null && sections.size() > 0) {
				ApiEventForm apiEventForm = new ApiEventForm();				
				Set<CriteriaResult> results = event.getResults();				
				for(CriteriaSection section : event.getEventForm().getAvailableSections()) {
					apiEventForm.getSections().add(convertCriteriaSection(section, results));
				}
				
				return apiEventForm;
			}
		}		
		
		return null;
	}
	
	private ApiCriteriaSection convertCriteriaSection(CriteriaSection section, Set<CriteriaResult> results) {
		ApiCriteriaSection apiSection = new ApiCriteriaSection();
		
		for (Criteria criteria : section.getCriteria()) {
			apiSection.getCriteria().add(convertCriteria(criteria, results));
		}
		
		return apiSection;
	}
	
	private ApiCriteria convertCriteria(Criteria criteria, Set<CriteriaResult> results) {
		ApiCriteria apiCriteria = eventTypeResource.convertCriteria(criteria);
		apiCriteria.setResult(findResult(criteria, results));
		return apiCriteria;
	}
	
	private ApiCriteriaResult findResult(Criteria criteria, Set<CriteriaResult> results) {
		if(results != null && results.size() > 0) {
			for(CriteriaResult result : results) {
				if(result.getCriteria().getId() == criteria.getId()) {
					return convertCriteriaResult(result);
				}
			}
		}
		
		return null;
	}
	
	private ApiCriteriaResult convertCriteriaResult(CriteriaResult criteriaResult) {
		ApiCriteriaResult apiResult = new ApiCriteriaResult();
		
		apiResult.setCriteriaId(criteriaResult.getCriteria().getId());
		apiResult.setRecommendations(convertRecommendations(criteriaResult.getRecommendations()));
		apiResult.setDeficiencies(convertDeficiencies(criteriaResult.getDeficiencies()));
		
		switch(criteriaResult.getCriteria().getCriteriaType()) {
			case ONE_CLICK: 
				OneClickCriteriaResult oneClickResult = (OneClickCriteriaResult)criteriaResult;
				apiResult.setOneClickValue(oneClickResult.getState().getId());
				break;
			case TEXT_FIELD:
				TextFieldCriteriaResult textFieldResult = (TextFieldCriteriaResult)criteriaResult;
				apiResult.setTextValue(textFieldResult.getValue());
				break;
			case COMBO_BOX:
				ComboBoxCriteriaResult comboResult = (ComboBoxCriteriaResult)criteriaResult;
				apiResult.setComboBoxValue(comboResult.getValue());
				break;
			case SELECT:
				SelectCriteriaResult selectResult = (SelectCriteriaResult)criteriaResult;
				apiResult.setSelectValue(selectResult.getValue());
				break;
			case UNIT_OF_MEASURE:
				UnitOfMeasureCriteriaResult uomResult = (UnitOfMeasureCriteriaResult)criteriaResult;
				apiResult.setUnitOfMeasurePrimaryValue(uomResult.getPrimaryValue());
				apiResult.setUnitOfMeasureSecondaryValue(uomResult.getSecondaryValue());
				break;
			case SIGNATURE:
				SignatureCriteriaResult signatureResult = (SignatureCriteriaResult)criteriaResult;
				apiResult.setSignatureValue(signatureResult.getImage());
				break;
			case DATE_FIELD:
				DateFieldCriteriaResult dateResult = (DateFieldCriteriaResult)criteriaResult;
				apiResult.setDateValue(dateResult.getValue());
				break;
			case SCORE:
				ScoreCriteriaResult scoreResult = (ScoreCriteriaResult)criteriaResult;
				apiResult.setScoreValue(scoreResult.getScore().getId());
				break;
			case NUMBER_FIELD:
				NumberFieldCriteriaResult numberResult = (NumberFieldCriteriaResult)criteriaResult;
				apiResult.setNumberValue(numberResult.getValue());
				break;
			default:
				throw new InternalErrorException("Unhandled Criteria type: " + criteriaResult.getCriteria().getCriteriaType().name());	
		}	
		
		return apiResult;
	}
	
	private List<ApiObservation> convertRecommendations(List<Recommendation> recommendations) {
		if(recommendations != null && recommendations.size() > 0) {
			List<ApiObservation> observations = new ArrayList<ApiObservation>();
			
			for(Observation observation : recommendations) {
				observations.add(convertObservation(observation));
			}
			
			return observations;
		}
		
		return null;
	}
	
	private List<ApiObservation> convertDeficiencies(List<Deficiency> deficiencies) {
		if(deficiencies != null && deficiencies.size() > 0) {
			List<ApiObservation> observations = new ArrayList<ApiObservation>();
			
			for(Observation observation : deficiencies) {
				observations.add(convertObservation(observation));
			}
			
			return observations;
		}
		
		return null;
	}
	
	private ApiObservation convertObservation(Observation observation) {
		ApiObservation apiObservation = new ApiObservation();
		apiObservation.setText(observation.getText());
		apiObservation.setState(observation.getStateString());
		return apiObservation;
	}
}
