package com.n4systems.fieldid.ws.v2.resources.customerdata.event;

import com.n4systems.api.conversion.event.CriteriaResultFactory;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventCreationService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.ws.v2.resources.ApiKeyString;
import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;
import com.n4systems.fieldid.ws.v2.resources.ApiResource;
import com.n4systems.fieldid.ws.v2.resources.ApiSortedModelHeader;
import com.n4systems.fieldid.ws.v2.resources.model.DateParam;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.offlineprofile.OfflineProfile.SyncDuration;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("event")
public class ApiEventResource extends ApiResource<ApiEvent, ThingEvent> {

	@Autowired private EventService eventService;
	@Autowired private EventCreationService eventCreationService;
	@Autowired private AssetService assetService;
	@Autowired private ApiEventToModelConverter apiEventToModelConverter;

	private CriteriaResultFactory criteriaResultFactory = new CriteriaResultFactory();

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
				.map(evt -> new ApiModelHeader<>(evt.getMobileId(), evt.getModified()))
				.collect(Collectors.toList());
		return headers;
	}

	@GET
	@Path("query/open")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiSortedModelHeader> queryOpen(@QueryParam("assetId") List<ApiKeyString> assetIds, @DefaultValue("YEAR") @QueryParam("syncDuration") SyncDuration syncDuration) {
		if (assetIds.isEmpty()) return new ArrayList<>();

		QueryBuilder<ApiSortedModelHeader> query = createModelHeaderQueryBuilder(ThingEvent.class, "mobileGUID", "modified", "dueDate", true)
				.addWhere(WhereClauseFactory.create("workflowState", WorkflowState.OPEN))
				.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "asset.mobileGUID", unwrapKeys(assetIds)));

		LocalDateTime endDateTime = syncDuration.getEndDate(LocalDate.now().atStartOfDay());
		if (endDateTime != null) {
			query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LE, "dueDate", Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant())));
		}
		List<ApiSortedModelHeader> headers = persistenceService.findAll(query);
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
				prepareAssignedEventsQuery(createModelHeaderQueryBuilder(ThingEvent.class, "mobileGUID", "modified", "dueDate", true), (DateParam)startDate, (DateParam)endDate, getCurrentUser())
		);
		return headers;
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

			QueryBuilder<ThingEvent> query = prepareAssignedEventsQuery(createUserSecurityBuilder(ThingEvent.class), (DateParam)startDate, (DateParam)endDate, getCurrentUser());
			Long count = persistenceService.count(query);
			counts.add(count);
		}
		return counts;
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

	private <T> QueryBuilder<T> prepareAssignedEventsQuery(QueryBuilder<T> query, @QueryParam("startDate") DateParam startDate, @QueryParam("endDate") DateParam endDate, User user) {
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

	@Override
	protected ApiEvent convertEntityToApiModel(ThingEvent event) {
		return apiEventToModelConverter.convertEntityToApiModel(event);
	}

//	@PUT
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Transactional
//	public void saveEvent(ApiEvent apiEvent) {
//		if(apiEvent.getSid() == null) {
//			throw new NullPointerException("ApiEvent has null sid");
//		}
//
//		ThingEvent existingEvent = eventService.findByMobileId(apiEvent.getSid(), true);
//		if (existingEvent == null || existingEvent.getWorkflowState() == WorkflowState.OPEN) {
//			createEvent(apiEvent, existingEvent);
//		} else {
//			updateEvent(apiEvent, existingEvent);
//		}
//	}
//
//	@PUT
//	@Path("multi")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Transactional
//	public void multiAddEvent(ApiMultiAddEvent multiAddEvent) {
//		ApiEvent apiEvent = multiAddEvent.getEventTemplate();
//		for(ApiMultiAddEventItem eventItem : multiAddEvent.getItems()) {
//			apiEvent.setSid(eventItem.getEventId());
//			apiEvent.setAssetId(eventItem.getAssetId());
//
//			ThingEvent event = null;
//			if(eventItem.isScheduled()) {
//				apiEvent.setEventScheduleId(eventItem.getEventId());
//				event = eventService.findByMobileId(eventItem.getEventId(), true);
//			} else {
//				apiEvent.setEventScheduleId(null);
//			}
//
//			if(event == null) {
//				event = new ThingEvent();
//			}
//
//			event.setAsset(assetService.findByMobileId(apiEvent.getAssetId(), true));
//
//			if(multiAddEvent.isCopyAssignedTo()) {
//				User user = event.getAsset().getAssignedUser();
//				apiEvent.setAssignedUserId(user != null ? user.getId() : null);
//			}
//
//			if(multiAddEvent.isCopyOwner()) {
//				BaseOrg owner = event.getAsset().getOwner();
//				apiEvent.setOwnerId(owner != null ? owner.getId() : null);
//			}
//
//			if(multiAddEvent.isCopyAssetStatus() != null && multiAddEvent.isCopyAssetStatus()) {
//				//Set the AssetStatus on the event to the AssetStatus from the Asset.  Obviously, if the Asset or its
//				//Status is missing, we're not going to be updating the event with that data... it doesn't exist!!
//				if(event.getAsset() != null && event.getAsset().getAssetStatus() != null) {
//					apiEvent.setAssetStatusId(event.getAsset().getAssetStatus().getId());
//				}
//			}
//
//			if(multiAddEvent.isCopyLocation()) {
//				Location location = event.getAsset().getAdvancedLocation();
//				if(location != null) {
//					apiEvent.setPredefinedLocationId(location.getPredefinedLocation() != null ? location.getPredefinedLocation().getId() : null);
//					apiEvent.setFreeformLocation(location.getFreeformLocation());
//				} else {
//					apiEvent.setPredefinedLocationId(null);
//					apiEvent.setFreeformLocation(null);
//				}
//			}
//
//			// Update CriteriaResult, Actions if event had a form.
//			if(apiEvent.getForm() != null) {
//				// Set sid to eventItem's results and remove it from the list.
//				apiEvent.getForm()
//						.getSections()
//						.stream()
//						.filter(sectionResult -> !sectionResult.isHidden())
//						.forEach(sectionResult -> {
//							for (ApiCriteriaResult criteriaResult : sectionResult.getCriteria()) {
//								// Set sid to eventItem's results and remove it from the list.
//								// These sids are sent by client so it can be used to look up when sending criteria images later.
//								criteriaResult.setSid(eventItem.getResults().remove(0).getSid());
//
//								// Set actions' assetId to eventItem's assetId.
//								for (ApiEventSchedule action : criteriaResult.getActions()) {
//									action.setAssetId(eventItem.getAssetId());
//
//									if (multiAddEvent.isCopyOwner()) {
//										action.setOwnerId(apiEvent.getOwnerId());
//									}
//								}
//
//								// NOTE: In the future we can set recommendations, deficiencies collection's sids too if we want.
//							}
//						});
//			}
//
//			createEvent(apiEvent, event);
//		}
//	}
//
//	private void createEvent(ApiEvent apiEvent, ThingEvent event) {
//		// event is either open event or null.
//		if(event == null) {
//			event = new ThingEvent();
//		}
//		convertApiEvent(apiEvent, event, false);
//		eventCreationService.createEvent(event, null, null, null);
//	}
//
//	private void updateEvent(ApiEvent apiEvent, ThingEvent existingEvent) {
//		convertApiEvent(apiEvent, existingEvent, true);
//		eventCreationService.updateEvent(existingEvent, null, null);
//	}
//
//	private void convertApiEvent(ApiEvent apiEvent, ThingEvent event, boolean isUpdate) {
//		event.setWorkflowState(WorkflowState.COMPLETED);
//
//		// Step 1: Convert abstract-event fields first.
//		convertApiEventForAbstractEvent(apiEvent, event, isUpdate);
//
//		//If asset is archived, Archive event also.
//		if(event.getAsset().isArchived())
//			event.archiveEntity();
//
//		// Step 2: Convert the non-abstract-event fields
//		event.setDate(apiEvent.getDate());
//		event.setPrintable(apiEvent.isPrintable());
//		event.setOwner(persistenceService.findUsingTenantOnlySecurityWithArchived(BaseOrg.class, apiEvent.getOwnerId()));
//		event.setPerformedBy(persistenceService.findUsingTenantOnlySecurityWithArchived(User.class, apiEvent.getPerformedById()));
//
//
//		if (apiEvent.getStatus() != null) {
//			event.setEventResult(EventResult.valueOf(apiEvent.getStatus()));
//		} else {
//			event.setEventResult(EventResult.VOID);
//		}
//
//		if (apiEvent.getAssignedUserId() != null) {
//			if (apiEvent.getAssignedUserId() < 0) {
//				event.setAssignedTo(AssignedToUpdate.unassignAsset());
//			} else {
//				event.setAssignedTo(AssignedToUpdate.assignAssetToUser(persistenceService.findUsingTenantOnlySecurityWithArchived(User.class, apiEvent.getAssignedUserId())));
//			}
//		}
//
//		if (apiEvent.getEventBookId() != null) {
//			event.setBook(findEventBook(apiEvent.getEventBookId()));
//		}
//
//		if (apiEvent.getPredefinedLocationId() != null && apiEvent.getPredefinedLocationId() > 0) {
//			event.getAdvancedLocation().setPredefinedLocation(persistenceService.findUsingTenantOnlySecurityWithArchived(PredefinedLocation.class, apiEvent.getPredefinedLocationId()));
//		}
//
//		if (apiEvent.getFreeformLocation() != null) {
//			event.getAdvancedLocation().setFreeformLocation(apiEvent.getFreeformLocation());
//		}
//
//		if (apiEvent.getGpsLatitude() != null && apiEvent.getGpsLongitude() != null) {
//			if (event.getGpsLocation() == null) event.setGpsLocation(new GpsLocation());
//			event.getGpsLocation().setLatitude(apiEvent.getGpsLatitude());
//			event.getGpsLocation().setLongitude(apiEvent.getGpsLongitude());
//		}
//
//		//TODO Need to refactor the block below once it is functional.
//		if(apiEvent.getSubEvents() != null && apiEvent.getSubEvents().size() > 0) {
//			if(!isUpdate) {
//				List<SubEvent> subEvents = new ArrayList<SubEvent>();
//				for(ApiEvent apiSubEvent: apiEvent.getSubEvents()) {
//					SubEvent subEvent = new SubEvent();
//					convertApiEventForAbstractEvent(apiSubEvent, subEvent, isUpdate);
//					subEvents.add(subEvent);
//				}
//				event.setSubEvents(subEvents);
//			} else {
//				for(ApiEvent apiSubEvent : apiEvent.getSubEvents()) {
//					SubEvent subEvent = null;
//					for(SubEvent existingSubEvent : event.getSubEvents()) {
//						if(existingSubEvent.getMobileGUID().equals(apiSubEvent.getSid())) {
//							subEvent = existingSubEvent;
//							break;
//						}
//					}
//
//					// If we did not find subevent, add a new one.
//					if(subEvent == null) {
//						subEvent = new SubEvent();
//						convertApiEventForAbstractEvent(apiSubEvent, subEvent, isUpdate);
//						event.getSubEvents().add(subEvent);
//					} else {
//						convertApiEventForAbstractEvent(apiSubEvent, subEvent, isUpdate);
//					}
//				}
//			}
//		}
//	}
//
//	private void convertApiEventForAbstractEvent(ApiEvent apiEvent, AbstractEvent<ThingEventType,Asset> event, boolean isUpdate) {
//		event.setTenant(getCurrentTenant());
//		event.setMobileGUID(apiEvent.getSid());
//		event.setModified(apiEvent.getModified());
//		event.setComments(apiEvent.getComments());
//		event.setType(persistenceService.find(EventType.class, apiEvent.getTypeId()));
//		event.setModifiedBy(persistenceService.findUsingTenantOnlySecurityWithArchived(User.class, apiEvent.getModifiedById()));
//
//		if(event.getTarget() == null) { // In the case of MultiEvent, we set event.asset much early on.
//			event.setTarget(assetService.findByMobileId(apiEvent.getAssetId(), true));
//		}
//
//		if (apiEvent.getAssetStatusId() != null) {
//			((AssetEvent)event).setAssetStatus(persistenceService.findUsingTenantOnlySecurityWithArchived(AssetStatus.class, apiEvent.getAssetStatusId()));
//		}
//
//		if(apiEvent.getEventStatusId() != null) {
//			event.setEventStatus(persistenceService.findUsingTenantOnlySecurityWithArchived(EventStatus.class, apiEvent.getEventStatusId()));
//		}
//
//		if (apiEvent.getFormId() != null) {
//			EventForm form = persistenceService.findUsingTenantOnlySecurityWithArchived(EventForm.class, apiEvent.getFormId());
//			event.setEventForm(form);
//
//			if(!isUpdate) {
//				List<CriteriaResult> results = convertApiEventFormResults(apiEvent.getResults(), form, event);
//				event.getResults().addAll(results);
//			} else {
//				convertApiEventFormResults(apiEvent.getForm(), event);
//			}
//		}
//
//		if(apiEvent.getAttributes() != null) {
//			for(ApiEventAttribute attribute : apiEvent.getAttributes()) {
//				event.getInfoOptionMap().put(attribute.getName(), attribute.getValue());
//			}
//		}
//	}
//
//	private EventBook findEventBook(String eventBookId) {
//		QueryBuilder<EventBook> query = createTenantSecurityBuilder(EventBook.class, true);
//		query.addWhere(WhereClauseFactory.create("mobileId", eventBookId));
//
//		EventBook book = persistenceService.find(query);
//		return book;
//	}
//
//	public List<CriteriaResult> convertApiEventFormResults(List<ApiCriteriaResult> apiResults, EventForm form, AbstractEvent event) {
//		List<CriteriaResult> results = new ArrayList<>();
//
//		//Only add the results if the form isn't hidden... otherwise, the CriteriaResults - should they exist - must go.
//		//Making the "go" should theoretically be as easy as not including them in the JPA model.
//		eventFormResult.getSections()
//				.stream()
//				.filter(sectionResult -> !sectionResult.isHidden())
//				.forEach(sectionResult -> results.addAll(convertSectionResult(form, sectionResult, event)));
//
//		return results;
//	}
//
//	private List<CriteriaResult> convertSectionResult(EventForm form, ApiCriteriaSectionResult sectionResult, AbstractEvent event) {
//		CriteriaSection section = null;
//		for (CriteriaSection sect: form.getSections()) {
//			if (sectionResult.getSectionId().equals(sect.getId())) {
//				section = sect;
//				break;
//			}
//		}
//
//		if (section == null) {
//			throw new NotFoundException("CriteriaSection", sectionResult.getSectionId());
//		}
//
//		List<CriteriaResult> results = new ArrayList<CriteriaResult>();
//		for (ApiCriteriaResult apiResult: sectionResult.getCriteria()) {
//			results.add(convertCriteriaResult(section, apiResult, event));
//		}
//		return results;
//	}
//
//	private CriteriaResult convertCriteriaResult(CriteriaSection section, ApiCriteriaResult apiResult, AbstractEvent event) {
//		Criteria criteria = null;
//		for (Criteria crit: section.getCriteria()) {
//			if (apiResult.getCriteriaId().equals(crit.getId())) {
//				criteria = crit;
//				break;
//			}
//		}
//
//		if (criteria == null) {
//			throw new NotFoundException("Criteria", apiResult.getCriteriaId());
//		}
//
//		CriteriaResult result = null;
//		switch (criteria.getCriteriaType()) {
//			case ONE_CLICK:
//				result = new OneClickCriteriaResult();
//				Button button = persistenceService.find(Button.class, apiResult.getOneClickValue());
//				((OneClickCriteriaResult) result).setButton(button);
//				break;
//			case TEXT_FIELD:
//				result = new TextFieldCriteriaResult();
//				((TextFieldCriteriaResult) result).setValue(apiResult.getTextValue());
//				break;
//			case COMBO_BOX:
//				result = new ComboBoxCriteriaResult();
//				((ComboBoxCriteriaResult) result).setValue(apiResult.getComboBoxValue());
//				break;
//			case SELECT:
//				result = new SelectCriteriaResult();
//				((SelectCriteriaResult) result).setValue(apiResult.getSelectValue());
//				break;
//			case UNIT_OF_MEASURE:
//				result = new UnitOfMeasureCriteriaResult();
//				((UnitOfMeasureCriteriaResult) result).setPrimaryValue(apiResult.getUnitOfMeasurePrimaryValue());
//				((UnitOfMeasureCriteriaResult) result).setSecondaryValue(apiResult.getUnitOfMeasureSecondaryValue());
//				break;
//			case SIGNATURE:
//				result = new SignatureCriteriaResult();
//				((SignatureCriteriaResult) result).setSigned(apiResult.getSignatureValue() != null);
//				((SignatureCriteriaResult) result).setImage(apiResult.getSignatureValue());
//				break;
//			case DATE_FIELD:
//				result = new DateFieldCriteriaResult();
//				((DateFieldCriteriaResult) result).setValue(apiResult.getDateValue());
//				break;
//			case SCORE:
//				result = new ScoreCriteriaResult();
//				Long scoreValue = apiResult.getScoreValue();
//
//				if(scoreValue == null)
//					throw new BadRequestException("Score value cannot be null. Client need to set a value.");
//
//				((ScoreCriteriaResult) result).setScore(persistenceService.find(Score.class, scoreValue));
//				break;
//			case NUMBER_FIELD:
//				result = new NumberFieldCriteriaResult();
//				((NumberFieldCriteriaResult) result).setValue(apiResult.getNumberValue());
//				break;
//			case OBSERVATION_COUNT:
//				//NOTE: The assumption here is that we don't need a mobileGUID, since we can more or less get the same
//				//      information by extrapolation.  We know that there can only be one of any given type of
//				//      ObservationCount for an ObservationCountCriteriaResult.  We know that these are tied to
//				//      ObservationCountResult objects, which lack a mobileGUID directly.  However, we can use the ID
//				//      from the ObservationCount and ApiObservationCount objects to determine relationships.
//				result = observationCountService.getObservationCountCriteriaResultByMobileId(apiResult.getSid());
//
//				List<ObservationCountResult> observationCountResults = new ArrayList<>();
//
//				//If we get a null back, this means that these CriteriaResults haven't been recorded yet!!
//				if(result == null) {
//					//New Entry Mode... this is where we build the data from scratch, more or less.
//
//					result = new ObservationCountCriteriaResult();
//
//					apiResult.getObservationCountValue().forEach(apiObservationCountResult -> {
//						//For each of these objects, we need to process it into the new data structure.  This is made
//						//easy by the fact that we should just be able to load up the ReadOnly portion of the model
//						//directly.
//						ObservationCount observationCount = persistenceService.find(ObservationCount.class, apiObservationCountResult.getObservationCount().getSid());
//
//						//Now we need to build the result...
//						ObservationCountResult observationCountResult = new ObservationCountResult();
//						observationCountResult.setObservationCount(observationCount); //We'll just put this here...
//						observationCountResult.setValue(apiObservationCountResult.getValue()); //...and take that value.
//						observationCountResult.setTenant(getCurrentTenant());
//
//						//After all of this, we bold it on to the result, like so:
//						observationCountResults.add(observationCountResult);
//					});
//				} else {
//					//Existing Work Mode... this is where we have to carefully match provided data with existing data.
//					//We will try to replace the whole list, if that is possible...
//
//					((ObservationCountCriteriaResult) result).getObservationCountResults()
//							.forEach(observationCountResult -> {
//								//Trust me, it looks like we're processing more than one entry below, but we're not.  Just like
//								//highlander, there can be only one.
//								apiResult.getObservationCountValue()
//										.stream()
//												//Notice how we're filtering in a way that makes it impossible to return more than
//												//one result (unless the data is corrupt, in which case this might have problems).
//										.filter(apiObservationCountResult -> apiObservationCountResult.getObservationCount().getSid().equals(observationCountResult.getObservationCount().getId()))
//										.forEach(apiObservationCountResult -> observationCountResult.setValue(apiObservationCountResult.getValue()));
//
//								observationCountResults.add(observationCountResult);
//							});
//
//				}
//
//				((ObservationCountCriteriaResult) result).setObservationCountResults(observationCountResults);
//
//				break;
//			default:
//				logger.error("Unhandled Criteria type: " + result.getCriteria().getCriteriaType().name());
//				throw new InternalServerErrorException("Unhandled Criteria type: " + criteria.getCriteriaType().name());
//		}
//		result.setMobileId(apiResult.getSid());
//		result.setEvent(event);
//		result.setCriteria(criteria);
//		result.setTenant(event.getTenant());
//
//		for (ApiObservation rec: apiResult.getRecommendations()) {
//			result.getRecommendations().add(convertRecommenation(rec, event.getTenant()));
//		}
//
//		for (ApiObservation def: apiResult.getDeficiencies()) {
//			result.getDeficiencies().add(convertDeficiency(def, event.getTenant()));
//		}
//
//		for(ApiEventSchedule action: apiResult.getActions()) {
//			Event actionEvent = apiEventScheduleResource.converApiEventSchedule(action);
//			result.getActions().add(actionEvent);
//		}
//
//		return result;
//	}
//
//	private Recommendation convertRecommenation(ApiObservation observation, Tenant tenant) {
//		Recommendation recommendation = new Recommendation();
//		recommendation.setMobileId(observation.getSid());
//		recommendation.setText(observation.getText());
//		recommendation.setTenant(tenant);
//		recommendation.setState(Observation.State.valueOf(observation.getState()));
//		return recommendation;
//	}
//
//	private Deficiency convertDeficiency(ApiObservation observation, Tenant tenant) {
//		Deficiency deficiency = new Deficiency();
//		deficiency.setMobileId(observation.getSid());
//		deficiency.setText(observation.getText());
//		deficiency.setTenant(tenant);
//		deficiency.setState(Observation.State.valueOf(observation.getState()));
//		return deficiency;
//	}
}
