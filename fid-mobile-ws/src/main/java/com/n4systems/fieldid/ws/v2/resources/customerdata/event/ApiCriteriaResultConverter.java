package com.n4systems.fieldid.ws.v2.resources.customerdata.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.services.signature.SignatureService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ApiCriteriaResultConverter extends FieldIdPersistenceService {
	private static Logger logger = Logger.getLogger(ApiCriteriaResult.class);

	@Autowired private SignatureService signatureService;

	public ApiCriteriaResult convert(CriteriaResult criteriaResult, Long eventId) {
		ApiCriteriaResult apiResult = new ApiCriteriaResult();

		apiResult.setSid(criteriaResult.getMobileId());
		apiResult.setCriteriaId(criteriaResult.getCriteria().getId());
		apiResult.setRecommendations(criteriaResult.getRecommendations().stream().map(this::convertObservation).collect(Collectors.toList()));
		apiResult.setDeficiencies(criteriaResult.getDeficiencies().stream().map(this::convertObservation).collect(Collectors.toList()));

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
						logger.warn("Failed loading signature image for event: " + eventId, ex);
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
				throw new InternalServerErrorException("Unhandled Criteria type: " + criteriaResult.getCriteria().getCriteriaType().name());
		}

		return apiResult;
	}

	private ApiObservation convertObservation(Observation observation) {
		ApiObservation apiObservation = new ApiObservation();
		apiObservation.setSid(observation.getMobileId());
		apiObservation.setText(observation.getText());
		apiObservation.setState(observation.getStateString());
		return apiObservation;
	}

}
