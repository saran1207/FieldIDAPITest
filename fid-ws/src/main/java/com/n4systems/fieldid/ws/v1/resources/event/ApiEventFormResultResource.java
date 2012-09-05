package com.n4systems.fieldid.ws.v1.resources.event;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v1.exceptions.InternalErrorException;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventSchedule;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventScheduleResource;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.ComboBoxCriteriaResult;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.DateFieldCriteriaResult;
import com.n4systems.model.Deficiency;
import com.n4systems.model.EventForm;
import com.n4systems.model.NumberFieldCriteriaResult;
import com.n4systems.model.Observation;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.Recommendation;
import com.n4systems.model.Score;
import com.n4systems.model.ScoreCriteriaResult;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.State;
import com.n4systems.model.Tenant;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.UnitOfMeasureCriteriaResult;

public class ApiEventFormResultResource extends FieldIdPersistenceService{
	@Autowired private ApiEventScheduleResource apiEventScheduleResource;
	
	public List<CriteriaResult> convertApiEventFormResults(ApiEventFormResult eventFormResult, EventForm form, AbstractEvent event) {
		List<CriteriaResult> results = new ArrayList<CriteriaResult>();
		
		for (ApiCriteriaSectionResult sectionResult: eventFormResult.getSections()) {
			results.addAll(convertSectionResult(form, sectionResult, event));
		}

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
				State state = persistenceService.find(State.class, apiResult.getOneClickValue());
				((OneClickCriteriaResult) result).setState(state);
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
			default:
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
		
		/*for(ApiEventSchedule action: apiResult.getActions()) {
			result.getActions().add(apiEventScheduleResource.converApiEventSchedule(action));
		}*/
		
		return result;
	}
	
	private Recommendation convertRecommenation(ApiObservation observation, Tenant tenant) {
		Recommendation recommendation = new Recommendation();
		recommendation.setText(observation.getText());
		recommendation.setTenant(tenant);
		recommendation.setState(Observation.State.valueOf(observation.getState()));
		return recommendation;
	}
	
	private Deficiency convertDeficiency(ApiObservation observation, Tenant tenant) {
		Deficiency deficiency = new Deficiency();
		deficiency.setText(observation.getText());
		deficiency.setTenant(tenant);
		deficiency.setState(Observation.State.valueOf(observation.getState()));
		return deficiency;
	}
}
