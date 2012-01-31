package com.n4systems.fieldid.ws.v1.resources.event;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.ejb.parameters.CreateEventParameterBuilder;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.ws.v1.exceptions.InternalErrorException;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.handlers.creator.events.factory.ProductionEventPersistenceFactory;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.ComboBoxCriteriaResult;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.DateFieldCriteriaResult;
import com.n4systems.model.Deficiency;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.NumberFieldCriteriaResult;
import com.n4systems.model.Observation;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.Recommendation;
import com.n4systems.model.Score;
import com.n4systems.model.ScoreCriteriaResult;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.State;
import com.n4systems.model.Status;
import com.n4systems.model.Tenant;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.UnitOfMeasureCriteriaResult;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

@Component
@Path("event")
public class ApiEventResource extends FieldIdPersistenceService {
	
	@Autowired private AssetService assetService;
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveEvent(ApiEvent apiEvent) {
		Event event = convertApiEvent(apiEvent);
		CreateEventParameterBuilder createEventParameterBuilder = new CreateEventParameterBuilder(event, securityContext.getUserSecurityFilter().getUserId());
		
		ProductionEventPersistenceFactory eventPersistenceFactory = new ProductionEventPersistenceFactory();
		eventPersistenceFactory.createEventCreator().create(createEventParameterBuilder.build());
	}
	
	private Event convertApiEvent(ApiEvent apiEvent) {
		Event event = new Event();
		event.setTenant(getCurrentTenant());
		event.setMobileGUID(apiEvent.getSid());
		event.setCreated(apiEvent.getModified());
		event.setModified(apiEvent.getModified());
		event.setDate(apiEvent.getDate());
		event.setPrintable(apiEvent.isPrintable());
		event.setComments(apiEvent.getComments());
		event.setOwner(persistenceService.find(BaseOrg.class, apiEvent.getOwnerId()));
		event.setPerformedBy(persistenceService.find(User.class, apiEvent.getPerformedById()));
		event.setType(persistenceService.find(EventType.class, apiEvent.getTypeId()));
		event.setAsset(assetService.findByMobileId(apiEvent.getAssetId()));
		
		if (apiEvent.getStatus() != null) {
			event.setStatus(Status.valueOf(apiEvent.getStatus()));
		} else {
			event.setStatus(null);
		}
		
		if (apiEvent.getAssignedUserId() != null) {
			if (apiEvent.getAssignedUserId() < 0) {
				event.setAssignedTo(AssignedToUpdate.unassignAsset());
			} else {
				event.setAssignedTo(AssignedToUpdate.assignAssetToUser(persistenceService.find(User.class, apiEvent.getAssignedUserId())));
			}
		}
		
		if (apiEvent.getEventBookId() != null) {
			event.setBook(persistenceService.find(EventBook.class, apiEvent.getEventBookId()));
		}
		
		if (apiEvent.getAssetStatusId() != null) {
			event.setAssetStatus(persistenceService.find(AssetStatus.class, apiEvent.getAssetStatusId()));
		}
		
		if (apiEvent.getPredefinedLocationId() != null) {
			event.getAdvancedLocation().setPredefinedLocation(persistenceService.find(PredefinedLocation.class, apiEvent.getPredefinedLocationId()));
		}
		
		if (apiEvent.getFreeformLocation() != null) {
			event.getAdvancedLocation().setFreeformLocation(apiEvent.getFreeformLocation());
		}
		
		if (apiEvent.getGpsLatitude() != null && apiEvent.getGpsLongitude() != null) {
			event.getGpsLocation().setLatitude(apiEvent.getGpsLatitude());
			event.getGpsLocation().setLongitude(apiEvent.getGpsLongitude());
		}
		
		if (apiEvent.getForm() != null) {
			EventForm form = persistenceService.find(EventForm.class, apiEvent.getForm().getFormId());
			event.setEventForm(form);
			
			List<CriteriaResult> results = convertEventFormResults(apiEvent.getForm(), form, event);
			event.getResults().addAll(results);
		}
		
		return event;
	}
	
	private List<CriteriaResult> convertEventFormResults(ApiEventFormResult eventFormResult, EventForm form, Event event) {
		List<CriteriaResult> results = new ArrayList<CriteriaResult>();
		
		for (ApiCriteriaSectionResult sectionResult: eventFormResult.getSections()) {
			results.addAll(convertSectionResult(form, sectionResult, event));
		}

		return results;
	}

	private List<CriteriaResult> convertSectionResult(EventForm form, ApiCriteriaSectionResult sectionResult, Event event) {
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

	private CriteriaResult convertCriteriaResult(CriteriaSection section, ApiCriteriaResult apiResult, Event event) {
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
				((OneClickCriteriaResult) result).setState(persistenceService.find(State.class, apiResult.getOneClickValue()));
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
				((ScoreCriteriaResult) result).setScore(persistenceService.find(Score.class, apiResult.getScoreValue()));
				break;
			case NUMBER_FIELD:
				result = new NumberFieldCriteriaResult();
				((NumberFieldCriteriaResult) result).setValue(apiResult.getNumberValue());
				break;
			default:
				throw new InternalErrorException("Unhandled Criteria type: " + criteria.getCriteriaType().name());
		}
		result.setEvent(event);
		result.setCriteria(criteria);
		result.setTenant(event.getTenant());
		
		for (ApiObservation rec: apiResult.getRecommendations()) {
			result.getRecommendations().add(convertRecommenation(rec, event.getTenant()));
		}
		
		for (ApiObservation def: apiResult.getDeficiencies()) {
			result.getDeficiencies().add(convertDeficiency(def, event.getTenant()));
		}
		
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
