package com.n4systems.fieldid.ws.v1.resources.eventtype;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiComboBoxCriteria;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiCriteria;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiDateFieldCriteria;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiEventStatus;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiNumberFieldCriteria;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiOneClickCriteria;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiOneClickState;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiScore;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiScoreCriteria;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiSelectCriteria;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiSignatureCriteria;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiTextFieldCriteria;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiUnitOfMeasureCriteria;
import com.n4systems.model.ComboBoxCriteria;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.DateFieldCriteria;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.NumberFieldCriteria;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.Score;
import com.n4systems.model.ScoreCriteria;
import com.n4systems.model.ScoreGroup;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.SignatureCriteria;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.TextFieldCriteria;
import com.n4systems.model.UnitOfMeasureCriteria;

@Component
@Path("eventType")
public class ApiEventTypeResource extends SetupDataResource<ApiEventType, EventType> {

	public ApiEventTypeResource() {
		super(EventType.class, false);
	}

	@Override
	protected ApiEventType convertEntityToApiModel(EventType eventType) {
		ApiEventType apiEventType = new ApiEventType();
		apiEventType.setSid(eventType.getId());
		apiEventType.setActive(eventType.isActive());
		apiEventType.setModified(eventType.getModified());
		apiEventType.setAssignedToAvailable(eventType.isAssignedToAvailable());
		apiEventType.setDescription(eventType.getDescription());
		apiEventType.setMaster(eventType.isMaster());
		apiEventType.setName(eventType.getName());
		apiEventType.setPrintable(eventType.isPrintable());
		apiEventType.getAttributes().addAll(eventType.getInfoFieldNames());

		if (eventType.getGroup() != null) {
			apiEventType.setGroupName(eventType.getGroup().getName());
		}

		if (eventType.getEventForm() != null) {
			apiEventType.setForm(convertEventForm(eventType.getEventForm()));
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

		for (Criteria criteria : section.getCriteria()) {
			apiSection.getCriteria().add(convertCriteria(criteria));
		}

		return apiSection;
	}

	private ApiCriteria convertCriteria(Criteria criteria) {
		ApiCriteria apiCriteria = convertCriteriaSubType(criteria);
		apiCriteria.setSid(criteria.getId());
		apiCriteria.setActive(!criteria.isRetired());
		apiCriteria.setModified(criteria.getModified());
		apiCriteria.setDisplayText(criteria.getDisplayText());
		apiCriteria.getRecommendations().addAll(criteria.getRecommendations());
		apiCriteria.getDeficiencies().addAll(criteria.getDeficiencies());

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
			apiCriteria = new ApiOneClickCriteria(oneClick.isPrincipal(), convertStateSet(oneClick.getStates()));
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

	private List<ApiOneClickState> convertStateSet(StateSet stateSet) {
		List<ApiOneClickState> apiStates = new ArrayList<ApiOneClickState>();
		for (State state : stateSet.getStates()) {
			ApiOneClickState apiState = new ApiOneClickState();
			apiState.setSid(state.getId());
			apiState.setModified(state.getModified());
			apiState.setActive(!state.isRetired());
			apiState.setDisplayText(state.getDisplayText());
			apiState.setButtonName(state.getButtonName());
			apiState.setStatus(ApiEventStatus.convert(state.getStatus()));
			apiStates.add(apiState);
		}
		return apiStates;
	}

	private List<ApiScore> convertScoreGroup(ScoreGroup group) {
		List<ApiScore> apiScores = new ArrayList<ApiScore>();
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
