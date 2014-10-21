package com.n4systems.fieldid.ws.v1.resources.eventtype;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.*;
import com.n4systems.fieldid.ws.v1.resources.model.DateParam;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.*;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("eventType")
public class ApiEventTypeResource extends SetupDataResource<ApiEventType, EventType> {

	public ApiEventTypeResource() {
		super(EventType.class, true);
	}

	@Override
	protected ListResponse<ApiEventType> getApiPage(DateParam after, int page, int pageSize) {
		//XXX - filtering out PlaceEventTypes, will be removed when place event types are implemented on mobile
		List<EventType> nonPlaceEventTypes = new ArrayList<>();
		for (EventType eventType: persistenceService.findAll(createFindAllBuilder(after))) {
			if (!(eventType instanceof PlaceEventType)) {
				nonPlaceEventTypes.add(eventType);
			}
		}

		List<ApiEventType> apiModels;
		int startIndex = page * pageSize;

		if (startIndex > nonPlaceEventTypes.size()) {
			apiModels = new ArrayList<>();
		} else {
			int endIndex = startIndex + pageSize;
			if (endIndex > nonPlaceEventTypes.size()) {
				endIndex = nonPlaceEventTypes.size();
			}
			apiModels = convertAllEntitiesToApiModels(nonPlaceEventTypes.subList(startIndex, endIndex));
		}
		return new ListResponse<>(apiModels, page, pageSize, nonPlaceEventTypes.size());
	}

	@Override
	protected ApiEventType convertEntityToApiModel(EventType eventType) {
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

}
