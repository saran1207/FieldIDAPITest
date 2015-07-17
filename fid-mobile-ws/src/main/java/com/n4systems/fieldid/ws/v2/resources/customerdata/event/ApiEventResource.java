package com.n4systems.fieldid.ws.v2.resources.customerdata.event;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.ws.v2.resources.ApiKeyString;
import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;
import com.n4systems.fieldid.ws.v2.resources.ApiResource;
import com.n4systems.fieldid.ws.v2.resources.ApiSortedModelHeader;
import com.n4systems.model.*;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.offlineprofile.OfflineProfile.SyncDuration;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.ActionDescriptionUtil;
import com.n4systems.model.utils.AssetEvent;
import com.n4systems.services.signature.SignatureService;
import com.n4systems.util.persistence.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Path("event")
public class ApiEventResource extends ApiResource<ApiEvent, ThingEvent> {
	private static Logger logger = Logger.getLogger(ApiEventResource.class);

	@Autowired
	private EventService eventService;

	@Autowired
	private SignatureService signatureService;

	@Autowired
	private S3Service s3Service;

	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> queryById(@QueryParam("id") List<ApiKeyString> eventIds) {
		if (eventIds.isEmpty()) return new ArrayList<>();

		List<ApiModelHeader> headers = persistenceService.findAll(
				createModelHeaderQueryBuilder(ThingEvent.class, "mobileGUID", "modified")
						.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "mobileGUID", unwrapKeys(eventIds)))
		);
		return headers;
	}

	@GET
	@Path("query/completed")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> queryCompleted(@QueryParam("assetId") List<ApiKeyString> assetIds) {
		if (assetIds.isEmpty()) return new ArrayList<>();

		List<ApiModelHeader> headers = eventService.getLastEventOfEachType(unwrapKeys(assetIds))
				.stream()
				.map(evt -> new ApiModelHeader(evt.getMobileId(), evt.getModified()))
				.collect(Collectors.toList());

		return headers;
	}

	@GET
	@Path("query/open")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiSortedModelHeader> queryOpen(@QueryParam("assetId") List<ApiKeyString> assetIds, @DefaultValue("YEAR") @QueryParam("syncDuration") SyncDuration syncDuration) {
		if (assetIds.isEmpty()) return new ArrayList<>();

		List<ApiSortedModelHeader> headers = findAllOpenEventHeaders(unwrapKeys(assetIds), syncDuration);
		return headers;
	}

	@GET
	@Path("query/open_completed")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> queryOpenCompleted(@QueryParam("assetId") List<ApiKeyString> assetIds, @DefaultValue("YEAR") @QueryParam("syncDuration") SyncDuration syncDuration) {
		if (assetIds.isEmpty()) return new ArrayList<>();

		List<ApiModelHeader> headers = new ArrayList<>();
		headers.addAll(queryOpen(assetIds, syncDuration));
		headers.addAll(queryCompleted(assetIds));
		return headers;
	}

	@GET
	@Path("query/assigned")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiSortedModelHeader> queryAssigned(@QueryParam("startDate") Date startDate, @QueryParam("endDate") Date endDate) {
		List<ApiSortedModelHeader> headers = persistenceService.findAll(
				prepareAssignedEventsQuery(createModelHeaderQueryBuilder(ThingEvent.class, "mobileGUID", "modified", "dueDate", true), startDate, endDate, getCurrentUser())
		);
		return headers;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiEvent> findAll(@QueryParam("id") List<ApiKeyString> eventIds) {
		if (eventIds.isEmpty()) return new ArrayList<>();

		QueryBuilder<ThingEvent> queryBuilder = createUserSecurityBuilder(ThingEvent.class);
		queryBuilder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "mobileGUID", unwrapKeys(eventIds)));
		List<ApiEvent> apiAttachment = convertAllEntitiesToApiModels(persistenceService.findAll(queryBuilder));
		return apiAttachment;
	}

	@GET
	@Path("assignedCounts")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<Long> findAssignedOpenEventCounts(
			@QueryParam("year") int year,
			@QueryParam("month") int month) {

		List<Long> counts = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 1);
		int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		for(int i = 1; i <= days; i++) {
			Date startDate = new DateTime(year, month + 1, i, 0, 0).toDate();
			Date endDate = new DateTime(year, month + 1, i, 0, 0).plusDays(1).toDate();

			QueryBuilder<ThingEvent> query = prepareAssignedEventsQuery(createUserSecurityBuilder(ThingEvent.class), startDate, endDate, getCurrentUser());
			Long count = persistenceService.count(query);
			counts.add(count);
		}
		return counts;
	}

	private <T> QueryBuilder<T> prepareAssignedEventsQuery(QueryBuilder<T> query, @QueryParam("startDate") Date startDate, @QueryParam("endDate") Date endDate, User user) {
		query
				.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "workflowState", WorkflowState.OPEN))
				.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GE, "startDate", "dueDate", startDate))
				.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LT, "endDate", "dueDate", endDate));	//excludes end date.

		if (user.getGroups().isEmpty()) {
			query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "assignee.id", user.getId()));
		} else {
			// WE need to do AND ( assignee.id = user.GetId() OR assignedGroup.id = user.getGroup().getId() )
			WhereParameterGroup group = new WhereParameterGroup();
			group.setChainOperator(WhereClause.ChainOp.AND);
			group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "assignee.id", user.getId(), WhereClause.ChainOp.OR));
			group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.IN, "assignedGroup", user.getGroups(), WhereClause.ChainOp.OR));
			query.addWhere(group);
		}
		return query;
	}

	public List<ApiSortedModelHeader> findAllOpenEventHeaders(List<String> assetIds, SyncDuration syncDuration) {
		QueryBuilder<ApiSortedModelHeader> query = createModelHeaderQueryBuilder(ThingEvent.class, "mobileGUID", "modified", "dueDate", true)
				.addWhere(WhereClauseFactory.create("workflowState", WorkflowState.OPEN))
				.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "asset.mobileGUID", assetIds));

		LocalDateTime endDateTime = syncDuration.getEndDate(LocalDate.now().atStartOfDay());
		if (endDateTime != null) {
			query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LE, "dueDate", Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant())));
		}
		return persistenceService.findAll(query);
	}

	@Override
	protected ApiEvent convertEntityToApiModel(ThingEvent event) {
		ApiEvent apiEvent = new ApiEvent();

		convertAbstractEventToApiEvent(apiEvent, event);

		apiEvent.setWorkflowState(event.getWorkflowState());
		apiEvent.setDueDate(event.getDueDate());
		apiEvent.setDate(event.getDate());
		apiEvent.setOwnerId(event.getOwner().getId());
		apiEvent.setPrintable(event.isPrintable());

		if (event.getPerformedBy() != null) {
			apiEvent.setPerformedById(event.getPerformedBy().getId());
		}

		if (event.getGpsLocation() != null && !event.getGpsLocation().isEmpty()) {
			apiEvent.setGpsLatitude(event.getGpsLocation().getLatitude());
			apiEvent.setGpsLongitude(event.getGpsLocation().getLongitude());
		}

		if(event.getAssignedTo() != null && event.getAssignedTo().getAssignedUser() != null) {
			apiEvent.setAssignedUserId(event.getAssignedTo().getAssignedUser().getId());
		}

		if (event.getAssignee() != null) {
			apiEvent.setAssignedUserId(event.getAssignee().getId());
		}

		if(event.getAssignedGroup() != null) {
			apiEvent.setAssignedUserGroupId(event.getAssignedGroup().getId());
		}

		if(event.getBook() != null) {
			apiEvent.setEventBookId(event.getBook().getMobileId());
		}

		if(event.getEventResult() != null) {
			apiEvent.setStatus(event.getEventResult().toString());
		}

		if(event.getAdvancedLocation() != null) {
			if(event.getAdvancedLocation().getPredefinedLocation() != null) {
				apiEvent.setPredefinedLocationId(event.getAdvancedLocation().getPredefinedLocation().getId());
			}
			apiEvent.setFreeformLocation(event.getAdvancedLocation().getFreeformLocation());
		}

		apiEvent.setSubEvents(convertToSubApiEvents(event.getSubEvents()));

		if (event.isAction()) {
			apiEvent.setAction(true);
			apiEvent.setPriorityId(event.getPriority().getId());
			apiEvent.setNotes(event.getNotes());
			apiEvent.setTriggerEventInfo(convertTriggerEventToApiModel(event));
		}

		return apiEvent;
	}

	private void convertAbstractEventToApiEvent(ApiEvent apiEvent, AbstractEvent<ThingEventType, Asset> event) {
		apiEvent.setSid(event.getMobileGUID());
		apiEvent.setModified(event.getModified());
		apiEvent.setActive(true);
		apiEvent.setComments(event.getComments());
		apiEvent.setTypeId(event.getType().getId());
		apiEvent.setAssetId(event.getTarget().getMobileGUID());

		if (event.getModifiedBy() != null) {
			apiEvent.setModifiedById(event.getModifiedBy().getId());
		}

		if (event instanceof AssetEvent && ((AssetEvent) event).getAssetStatus() != null) {
			apiEvent.setAssetStatusId(((AssetEvent) event).getAssetStatus().getId());
		}

		if(event.getEventStatus() != null) {
			apiEvent.setEventStatusId(event.getEventStatus().getId());
		}

		apiEvent.setAttributes(convertToApiEventAttributes(event.getInfoOptionMap()));
		apiEvent.setForm(convertToApiEventForm(event));
	}

	private List<ApiEventAttribute> convertToApiEventAttributes(Map<String, String> infoOptionMap) {
		return infoOptionMap.entrySet().stream().map(e -> {
			ApiEventAttribute attribute = new ApiEventAttribute();
			attribute.setName(e.getKey());
			attribute.setValue(e.getValue());
			return attribute;
		}).collect(Collectors.toList());
	}

	private List<ApiEvent> convertToSubApiEvents(List<SubEvent> subEvents) {
		return subEvents.stream().map(e -> {
			ApiEvent apiSubEvent = new ApiEvent();
			convertAbstractEventToApiEvent(apiSubEvent, e);
			return apiSubEvent;
		}).collect(Collectors.toList());
	}

	public ApiEventFormResult convertToApiEventForm(AbstractEvent event) {
		if(event.getEventForm() != null) {
			ApiEventFormResult apiEventForm = new ApiEventFormResult();
			apiEventForm.setFormId(event.getEventForm().getId());

			List<CriteriaSection> sections = event.getEventForm().getAvailableSections();
			for(CriteriaSection section: sections) {
				apiEventForm.getSections().add(convertCriteriaSection(section, event.getResults(), event.getId()));
			}
			return apiEventForm;
		}

		return null;
	}

	private ApiCriteriaSectionResult convertCriteriaSection(CriteriaSection section, Set<CriteriaResult> results, Long eventId) {
		ApiCriteriaSectionResult apiSection = new ApiCriteriaSectionResult();
		apiSection.setSectionId(section.getId());

		ApiCriteriaResult criteriaResult;
		for (Criteria criteria : section.getCriteria()) {
			criteriaResult = convertCriteria(criteria, results, eventId);
			if (criteriaResult != null)
				apiSection.getCriteria().add(criteriaResult);
		}

		return apiSection;
	}

	private ApiCriteriaResult convertCriteria(Criteria criteria, Set<CriteriaResult> results, Long eventId) {
		CriteriaResult criteriaResult = results.stream().filter(r -> r.getCriteria().getId().equals(criteria.getId())).findFirst().orElse(null);
		return criteriaResult == null ? null : convertCriteriaResult(criteriaResult, eventId);
	}

	private ApiCriteriaResult convertCriteriaResult(CriteriaResult criteriaResult, Long eventId) {
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

	private ApiTriggerEvent convertTriggerEventToApiModel(Event actionEvent) {
		ApiTriggerEvent triggerEvent = new ApiTriggerEvent();
		triggerEvent.setName(actionEvent.getTriggerEvent().getType().getName());
		triggerEvent.setDate(actionEvent.getTriggerEvent().getDate());
		triggerEvent.setPerformedBy(actionEvent.getTriggerEvent().getPerformedBy().getFullName());
		triggerEvent.setCriteria(ActionDescriptionUtil.getDescription(actionEvent.getTriggerEvent(), actionEvent.getSourceCriteriaResult()));

		List<CriteriaResultImage> criteriaImages = actionEvent.getSourceCriteriaResult().getCriteriaImages();
		triggerEvent.setImageUrls(criteriaImages.stream().map(s3Service::getCriteriaResultImageMediumURL).collect(Collectors.toList()));
		triggerEvent.setImageComments(criteriaImages.stream().map(CriteriaResultImage::getComments).collect(Collectors.toList()));
		return triggerEvent;
	}
}
