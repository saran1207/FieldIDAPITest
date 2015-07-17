package com.n4systems.fieldid.ws.v2.resources.setupdata.eventtype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.fieldid.ws.v2.resources.setupdata.eventtype.criteria.*;
import com.n4systems.model.*;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("eventType")
public class ApiEventTypeResource extends SetupDataResourceReadOnly<ApiEventType, EventType> {

	public ApiEventTypeResource() {
		super(EventType.class, true);
	}

	@Override
	protected ApiEventType convertEntityToApiModel(EventType eventType) {
		// for now we are filtering out PlaceEventTypes.  The nulls will be removed by the convertAllEntitiesToApiModels call
		if (eventType instanceof PlaceEventType) {
			return null;
		}

		ApiEventType apiEventType = new ApiEventType();
		apiEventType.setSid(eventType.getId());
		apiEventType.setActive(eventType.isActive());
		apiEventType.setModified(eventType.getModified());
		
		// We only set the rest of the fields if the entity is active.
		if (eventType.isActive()) {
			apiEventType.setAssignedToAvailable(eventType.isAssignedToAvailable());
			apiEventType.setDescription(eventType.getDescription());
			apiEventType.setMaster(eventType instanceof ThingEventType && ((ThingEventType)eventType).isMaster());
			apiEventType.setName(eventType.getName());
			apiEventType.setAction(eventType.isActionEventType());
			apiEventType.setPrintable(eventType.isPrintable());
			apiEventType.setHasPrintOut(eventType.getGroup().hasPrintOut());
			apiEventType.setHasObservationPrintOut(eventType.getGroup().hasObservationPrintOut());
			apiEventType.getAttributes().addAll(eventType.getInfoFieldNames());
	
			if (eventType.getGroup() != null) {
				apiEventType.setGroupName(eventType.getGroup().getName());
			}
	
			if (eventType.getEventForm() != null) {
				apiEventType.setForm(convertEventForm(eventType.getEventForm()));
			}
		}

		return apiEventType;
	}

	private ApiEventForm convertEventForm(EventForm eventForm) {
		ApiEventForm apiEventForm = new ApiEventForm();
		apiEventForm.setSid(eventForm.getId());
		apiEventForm.setActive(eventForm.isActive());
		apiEventForm.setModified(eventForm.getModified());
		apiEventForm.setUseScoreForResult(eventForm.isUseScoreForResult());
        apiEventForm.setUseObservationCountForResult(eventForm.isUseObservationCountForResult());

		for (CriteriaSection section : eventForm.getSections()) {
			apiEventForm.getSections().add(convertCriteriaSection(section));
		}

		return apiEventForm;
	}

	private ApiCriteriaSection convertCriteriaSection(CriteriaSection section) {
		ApiCriteriaSection apiSection = new ApiCriteriaSection();
		apiSection.setSid(section.getId());
		apiSection.setActive(!section.isRetired());
		apiSection.setModified(section.getModified());
		apiSection.setTitle(section.getTitle());
        apiSection.setOptional(section.isOptional());

		for (Criteria criteria : section.getCriteria()) {
			apiSection.getCriteria().add(convertCriteria(criteria));
		}

		return apiSection;
	}

	public ApiCriteria convertCriteria(Criteria criteria) {
		ApiCriteria apiCriteria = convertCriteriaSubType(criteria);
		apiCriteria.setSid(criteria.getId());
		apiCriteria.setActive(!criteria.isRetired());
		apiCriteria.setModified(criteria.getModified());
		apiCriteria.setDisplayText(criteria.getDisplayText());
		apiCriteria.getRecommendations().addAll(criteria.getRecommendations());
		apiCriteria.getDeficiencies().addAll(criteria.getDeficiencies());
		apiCriteria.setInstructions(criteria.getInstructions());

		return apiCriteria;
	}

	private ApiCriteria convertCriteriaSubType(Criteria criteria) {
		ApiCriteria apiCriteria;
		if (criteria instanceof ComboBoxCriteria) {
			apiCriteria = new ApiComboBoxCriteria(((ComboBoxCriteria) criteria).getOptions());
		} else if (criteria instanceof DateFieldCriteria) {
			apiCriteria = new ApiDateFieldCriteria(((DateFieldCriteria) criteria).isIncludeTime());
		} else if (criteria instanceof NumberFieldCriteria) {
			apiCriteria = new ApiNumberFieldCriteria(((NumberFieldCriteria) criteria).getDecimalPlaces());
		} else if (criteria instanceof OneClickCriteria) {
			OneClickCriteria oneClick = (OneClickCriteria) criteria;
			apiCriteria = new ApiOneClickCriteria(oneClick.isPrincipal(), convertStateSet(oneClick.getButtonGroup()));
		} else if (criteria instanceof ScoreCriteria) {
			apiCriteria = new ApiScoreCriteria(convertScoreGroup(((ScoreCriteria) criteria).getScoreGroup()));
		} else if (criteria instanceof SelectCriteria) {
			apiCriteria = new ApiSelectCriteria(((SelectCriteria) criteria).getOptions());
		} else if (criteria instanceof SignatureCriteria) {
			apiCriteria = new ApiSignatureCriteria();
		} else if (criteria instanceof TextFieldCriteria) {
			apiCriteria = new ApiTextFieldCriteria();
		} else if (criteria instanceof UnitOfMeasureCriteria) {
			UnitOfMeasureCriteria uomCriteria = (UnitOfMeasureCriteria) criteria;
			Long secondaryUnitId = uomCriteria.getSecondaryUnit() != null ? uomCriteria.getSecondaryUnit().getId() : null;
			apiCriteria = new ApiUnitOfMeasureCriteria(uomCriteria.getPrimaryUnit().getId(), secondaryUnitId);
        } else if (criteria instanceof ObservationCountCriteria) {
            apiCriteria = new ApiObservationCountCriteria(convertObservationCountGroup(((ObservationCountCriteria) criteria).getObservationCountGroup()));
		} else {
			throw new IllegalArgumentException("Unsupported Criteria type: " + criteria.getClass().getName());
		}
		return apiCriteria;
	}

    private List<ApiOneClickState> convertStateSet(ButtonGroup buttonGroup) {
		List<ApiOneClickState> apiStates = new ArrayList<>();
		for (Button button : buttonGroup.getButtons()) {
			ApiOneClickState apiState = new ApiOneClickState();
			apiState.setSid(button.getId());
			apiState.setModified(button.getModified());
			apiState.setActive(!button.isRetired());
			apiState.setDisplayText(button.getDisplayText());
			apiState.setButtonName(button.getButtonName());
			apiState.setStatus(ApiEventStatus.convert(button.getEventResult()));
			apiStates.add(apiState);
		}
		return apiStates;
	}

	private List<ApiScore> convertScoreGroup(ScoreGroup group) {
		List<ApiScore> apiScores = new ArrayList<>();
		for (Score score: group.getScores()) {
			ApiScore apiScore = new ApiScore();
			apiScore.setSid(score.getId());
			apiScore.setModified(score.getModified());
			apiScore.setActive(score.isActive());
			apiScore.setName(score.getName());
			apiScore.setNotApplicable(score.isNa());
			apiScore.setValue(score.getValue());
			apiScores.add(apiScore);
		}
		return apiScores;
	}

    private List<ApiObservationCount> convertObservationCountGroup(ObservationCountGroup group) {
        List<ApiObservationCount> apiObservationCounts = Lists.newArrayList();
        for (ObservationCount count: group.getObservationCounts()) {
            ApiObservationCount apiCount = new ApiObservationCount();
            apiCount.setSid(count.getId());
            apiCount.setModified(count.getModified());
            apiCount.setName(count.getName());
            apiCount.setCounted(count.isCounted());
            apiCount.setActive(count.isActive());
            apiObservationCounts.add(apiCount);
        }
        return apiObservationCounts;
    }
}
