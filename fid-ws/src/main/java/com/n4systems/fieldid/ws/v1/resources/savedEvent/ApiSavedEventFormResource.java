package com.n4systems.fieldid.ws.v1.resources.savedEvent;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.ws.v1.exceptions.InternalErrorException;
import com.n4systems.fieldid.ws.v1.resources.event.ApiCriteriaResult;
import com.n4systems.fieldid.ws.v1.resources.event.ApiObservation;
import com.n4systems.fieldid.ws.v1.resources.event.criteria.ApiCriteriaImageDownload;
import com.n4systems.fieldid.ws.v1.resources.event.criteria.ApiObservationCountResult;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiCriteriaSection;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiEventForm;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiEventTypeResource;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiCriteria;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiObservationCount;
import com.n4systems.model.*;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.services.signature.SignatureService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ApiSavedEventFormResource extends FieldIdPersistenceService{
	private static Logger logger = Logger.getLogger(ApiSavedEventFormResource.class);
	
	@Autowired private ApiEventTypeResource eventTypeResource;
    @Autowired private SignatureService signatureService;
	@Autowired private S3Service s3Service;

	public ApiEventForm convertToApiEventForm(AbstractEvent event) {
		if(event.getEventForm() != null) {
			ApiEventForm apiEventForm = new ApiEventForm();
			apiEventForm.setSid(event.getEventForm().getId());
            //You probably want to add the flags here... why are they never being set here?!?!??!?!
            apiEventForm.setUseObservationCountForResult(event.getEventForm().isUseObservationCountForResult());
            apiEventForm.setUseScoreForResult(event.getEventForm().isUseScoreForResult());

			List<CriteriaSection> sections = event.getEventForm().getAvailableSections();			
			if(sections != null && sections.size() > 0) {				
				
				Set<CriteriaResult> results = event.getResults();				
				for(CriteriaSection section : event.getEventForm().getAvailableSections()) {
					apiEventForm.getSections().add(convertCriteriaSection(section, results, event.getId()));
				}
			}
			
			return apiEventForm;
		}		
		
		return null;
	}
	
	private ApiCriteriaSection convertCriteriaSection(CriteriaSection section, Set<CriteriaResult> results, Long eventId) {
		ApiCriteriaSection apiSection = new ApiCriteriaSection();
		
		apiSection.setSid(section.getId());
		apiSection.setActive(!section.isRetired());
		apiSection.setModified(section.getModified());
		apiSection.setTitle(section.getTitle());
        apiSection.setOptional(section.isOptional());

		ApiCriteria criteriaResult;
		for (Criteria criteria : section.getCriteria()) {
			criteriaResult = convertCriteria(criteria, results, eventId);
			if (criteriaResult != null)
				apiSection.getCriteria().add(criteriaResult);
		}
		
		return apiSection;
	}
	
	private ApiCriteria convertCriteria(Criteria criteria, Set<CriteriaResult> results, Long eventId) {
		// findResult can return null in cases of retired criteria.  In that case we should not send or convert the criteria at all
		ApiCriteriaResult result = findResult(criteria, results, eventId);
		if (result == null) return null;

		ApiCriteria apiCriteria = eventTypeResource.convertCriteria(criteria);
        apiCriteria.setResult(result);
		return apiCriteria;
	}
	
	private ApiCriteriaResult findResult(Criteria criteria, Set<CriteriaResult> results, Long eventId) {
		if(results != null && results.size() > 0) {
			for(CriteriaResult result : results) {
				if(result.getCriteria().getId() == criteria.getId()) {
					return convertCriteriaResult(result, eventId);
				}
			}
		}
		
		return null;
	}
	
	private ApiCriteriaResult convertCriteriaResult(CriteriaResult criteriaResult, Long eventId) {
		ApiCriteriaResult apiResult = new ApiCriteriaResult();

		//Convert Criteria Images
		for(CriteriaResultImage image:criteriaResult.getCriteriaImages()) {
			ApiCriteriaImageDownload temp = new ApiCriteriaImageDownload();
			temp.setCriteriaResultSid(Long.toString(criteriaResult.getId()));
			temp.setSid(image.getMobileGUID());
			temp.setComments(image.getComments());
			temp.setImage(s3Service.getCriteriaResultImageMediumURL(image));
			apiResult.getCriteriaImages().add(temp);
		}

		apiResult.setSid(criteriaResult.getMobileId());
		apiResult.setCriteriaId(criteriaResult.getCriteria().getId());
		apiResult.setRecommendations(convertRecommendations(criteriaResult.getRecommendations()));
		apiResult.setDeficiencies(convertDeficiencies(criteriaResult.getDeficiencies()));
		
		switch(criteriaResult.getCriteria().getCriteriaType()) {
			case ONE_CLICK: 
				OneClickCriteriaResult oneClickResult = (OneClickCriteriaResult)criteriaResult;
                if(oneClickResult.getButton() != null){
                    apiResult.setOneClickValue(oneClickResult.getButton().getId());
                }
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
				if(signatureResult.isSigned()) {
					try {
						byte[] image = signatureService.loadSignatureImage(getCurrentTenant(), eventId, criteriaResult.getCriteria().getId());
						apiResult.setSignatureValue(image);
					} catch (IOException ex) {
						logger.error("Error loading signature image for event: " + eventId, ex);
					}
				}				
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
            case OBSERVATION_COUNT:
                ObservationCountCriteriaResult observationCountResultCriteria = (ObservationCountCriteriaResult)criteriaResult;

                List<ApiObservationCountResult> apiObservationCountResultList = new ArrayList<>();

                observationCountResultCriteria.getObservationCountResults().forEach(observationCountResult -> {
                    //Create the base ApiObservationCountResult object and add the "value" (which is the count)
                    ApiObservationCountResult apiObservationCountResult = new ApiObservationCountResult();
                    apiObservationCountResult.setValue(observationCountResult.getValue());

                    //Create the embedded ApiObservationCount object and add the fields from the related JPA Entity
                    ApiObservationCount apiObservationCount = new ApiObservationCount();
                    apiObservationCount.setSid(observationCountResult.getObservationCount().getId());
                    apiObservationCount.setCounted(observationCountResult.getObservationCount().isCounted());
                    apiObservationCount.setName(observationCountResult.getObservationCount().getName());
                    apiObservationCount.setModified(observationCountResult.getObservationCount().getModified());
                    apiObservationCount.setActive(observationCountResult.getObservationCount().isActive());

                    //Attach the ApiObservationCount to the ApiObservationCountResult
                    apiObservationCountResult.setObservationCount(apiObservationCount);

                    //Add the ApiObservationCountResult to the list
                    apiObservationCountResultList.add(apiObservationCountResult);
                });

                apiResult.setObservationCountValue(apiObservationCountResultList);
                break;
			default:
                logger.error("Unhandled Criteria type: " + criteriaResult.getCriteria().getCriteriaType().name());
				throw new InternalErrorException("Unhandled Criteria type: " + criteriaResult.getCriteria().getCriteriaType().name());	
		}	
		
		return apiResult;
	}
	
	private List<ApiObservation> convertRecommendations(List<Recommendation> recommendations) {
		List<ApiObservation> observations = new ArrayList<ApiObservation>();
		
		if(recommendations != null  && recommendations.size() > 0) {			
			for(Observation observation : recommendations) {
				observations.add(convertObservation(observation));
			}
		}
		
		return observations;
	}
	
	private List<ApiObservation> convertDeficiencies(List<Deficiency> deficiencies) {
		List<ApiObservation> observations = new ArrayList<ApiObservation>();
		
		if(deficiencies != null && deficiencies.size() > 0) {
			for(Observation observation : deficiencies) {
				observations.add(convertObservation(observation));
			}			
		}
		
		return observations;
	}
	
	private ApiObservation convertObservation(Observation observation) {
		ApiObservation apiObservation = new ApiObservation();
		apiObservation.setSid(observation.getMobileId());
		apiObservation.setText(observation.getText());
		apiObservation.setState(observation.getStateString());
		return apiObservation;
	}
}
