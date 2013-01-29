package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.asset.ApiAssetLink;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.fieldid.ws.v1.resources.synchronization.ApiSynchronizationResource;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.PriorityCode;
import com.n4systems.model.offlineprofile.OfflineProfile.SyncDuration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Path("eventSchedule")
public class ApiEventScheduleResource extends ApiResource<ApiEventSchedule, Event> {
	private static Logger logger = Logger.getLogger(ApiEventScheduleResource.class);
	
	@Autowired private EventScheduleService eventScheduleService;
	@Autowired private PersistenceService persistenceService;
	@Autowired private AssetService assetService;
	@Autowired private ApiTriggerEventResource apiTriggerEventResource;	
	
	public List<ApiEventSchedule>  findAllSchedules(Long assetId, SyncDuration syncDuration) {
		List<ApiEventSchedule> apiEventSchedules = new ArrayList<ApiEventSchedule>();
		
		QueryBuilder<Event> query = createUserSecurityBuilder(Event.class)
		.addOrder("dueDate")
        .addWhere(WhereClauseFactory.create(Comparator.EQ, "workflowState", Event.WorkflowState.OPEN))
		.addWhere(WhereClauseFactory.create("asset.id", assetId));
		
		if(syncDuration != SyncDuration.ALL) {
			// If we have a specific syncduration, we should filter schedules that is bounded by start and end dates.
			// This is currently only used for taking assets offline.
			Date startDate = new LocalDate().toDate();
			Date endDate = ApiSynchronizationResource.getSyncEndDate(syncDuration, startDate);
			query.addWhere(WhereClauseFactory.create(Comparator.GE, "dueDate", startDate));
			query.addWhere(WhereClauseFactory.create(Comparator.LE, "dueDate", endDate));
		}
		
		List<Event> schedules = persistenceService.findAll(query);
		
		for (Event schedule: schedules) {
			apiEventSchedules.add(convertEntityToApiModel(schedule));
		}
		
		return apiEventSchedules;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveEventSchedule(ApiEventSchedule apiEventSchedule) {
		Event event = eventScheduleService.findByMobileId(apiEventSchedule.getSid());

		if (event == null) {
            event = converApiEventSchedule(apiEventSchedule);
			persistenceService.save(event);
			logger.info("Saved New Scheduled Event("  + event.getMobileGUID() + ") for " + 
					event.getEventType().getName() + " on Asset " + event.getAsset().getMobileGUID());
		} else if (event.getWorkflowState() == Event.WorkflowState.OPEN) {
			event.setDueDate(apiEventSchedule.getNextDate());
            event.setType(persistenceService.find(EventType.class, apiEventSchedule.getEventTypeId()));
            event.setAssignee(getAssigneeUser(apiEventSchedule));
			persistenceService.update(event);
			logger.warn("(Legacy Client Detected) Updated Scheduled Event for " + event.getEventType().getName() + " on Asset " + event.getMobileGUID());
		} else {
            logger.warn("(Legacy Client Detected) Failed Updating Completed Scheduled Event" + event.getId());
        }
	}

	@PUT
	@Path("multi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveMultipleEventSchedules(ApiMultiEventSchedule apiMultiEventSchedule) {
		ApiEventSchedule eventScheduleTemplate = apiMultiEventSchedule.getEventScheduleTemplate();
		for (ApiAssetLink assetLink: apiMultiEventSchedule.getEventSchedules()) {
			eventScheduleTemplate.setSid(assetLink.getSid());
			eventScheduleTemplate.setAssetId(assetLink.getAssetId());

			logger.info("Creating Event Schedule " + eventScheduleTemplate.getSid());
			saveEventSchedule(eventScheduleTemplate);
		}

		logger.info("Saved " + apiMultiEventSchedule.getEventSchedules().size() + " Schedules");
	}
	
	@DELETE
	@Path("{eventScheduleId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void deleteEventSchedule(@PathParam("eventScheduleId") String eventScheduleId) {
		logger.info("Attempting to archive Scheduled Event with mobileId " + eventScheduleId);
		Event event = eventScheduleService.findByMobileId(eventScheduleId, true);
		if(event != null) {
			if(event.isActive()) {
		        event.archiveEntity();
		        persistenceService.update(event);
				logger.info("Archived Scheduled Event for " + event.getEventType().getName() + " on Event " + event.getMobileGUID());
			} else {
				logger.warn("Failed Deleting Event Schedule. Inative Event " + eventScheduleId);
			}
		} else {
			logger.error("Failed Deleting Event Schedule. Unable to find Event " + eventScheduleId);
        	throw new NotFoundException("Event",eventScheduleId);
		}
	}

	@Override
	protected ApiEventSchedule convertEntityToApiModel(Event event) {
		ApiEventSchedule apiSchedule = new ApiEventSchedule();
        // For backward compatibility, we must still use the GUID of the Schedule, since all
        // existing schedules out there in mobile land will refer to schedule GUIDs.
		apiSchedule.setSid(event.getMobileGUID());
		apiSchedule.setActive(true);
		apiSchedule.setModified(event.getModified());
		apiSchedule.setOwnerId(event.getOwner().getId());
		apiSchedule.setAssetId(event.getAsset().getMobileGUID());
		apiSchedule.setAssetIdentifier(event.getAsset().getIdentifier());
		apiSchedule.setEventTypeId(event.getEventType().getId());
		apiSchedule.setEventTypeName(event.getEventType().getName());
		apiSchedule.setOwner(event.getOwner().getDisplayName());
		apiSchedule.setNextDate(event.getDueDate());
		if (event.getAssignee() != null) {
			apiSchedule.setAssigneeUserId(event.getAssignee().getId());
		}
		
		if (event.isAction()) {
			apiSchedule.setAction(true);
			apiSchedule.setPriorityId(event.getPriority().getId());
			apiSchedule.setNotes(event.getNotes());
			apiSchedule.setTriggerEventInfo(apiTriggerEventResource.convertEntityToApiModel(event));
		}
		
		return apiSchedule;
	}
	
	public Event converApiEventSchedule(ApiEventSchedule apiEventSchedule) {
		BaseOrg owner = persistenceService.findUsingTenantOnlySecurityWithArchived(BaseOrg.class, apiEventSchedule.getOwnerId());

        Asset asset = assetService.findByMobileId(apiEventSchedule.getAssetId(), true);

        Event event = new Event();
        event.setMobileGUID(apiEventSchedule.getSid());
        event.setDueDate(apiEventSchedule.getNextDate());
        event.setTenant(owner.getTenant());
        event.setAsset(asset);
        event.setType(persistenceService.find(EventType.class, apiEventSchedule.getEventTypeId()));
        event.setOwner(asset.getOwner());
        event.setAssignee(getAssigneeUser(apiEventSchedule));
        
        if(event.getEventType().getGroup().isAction()) {
        	event.setPriority(persistenceService.find(PriorityCode.class, apiEventSchedule.getPriorityId()));
        	event.setNotes(apiEventSchedule.getNotes());
        }
        
        if(asset.isArchived())
        	event.archiveEntity();

		return event;
	}
	
	private User getAssigneeUser(ApiEventSchedule apiEventSchedule) {
		if(apiEventSchedule.getAssigneeUserId() != null && apiEventSchedule.getAssigneeUserId() > 0) {
        	User assigneeUser = persistenceService.findUsingTenantOnlySecurityWithArchived(User.class, apiEventSchedule.getAssigneeUserId());
        	return assigneeUser;
        }
		
		return null;
	}
	
	@GET
	@Path("assignedList")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiEventSchedule> findAssignedOpenEvents(
			@QueryParam("startDate") Date startDate, 
			@QueryParam("endDate") Date endDate,
			@DefaultValue("0") @QueryParam("page") int page,
			@DefaultValue("25") @QueryParam("pageSize") int pageSize) {
		QueryBuilder<Event> query = createUserSecurityBuilder(Event.class)
		.addOrder("dueDate")
        .addWhere(WhereClauseFactory.create(Comparator.EQ, "workflowState", Event.WorkflowState.OPEN))
        .addWhere(WhereClauseFactory.create(Comparator.EQ, "assignee.id", getCurrentUser().getId()))
		.addWhere(WhereClauseFactory.create(Comparator.GE, "startDate", "dueDate", startDate))
		.addWhere(WhereClauseFactory.create(Comparator.LT, "endDate", "dueDate", endDate));	//excludes end date.
		
		List<Event> events = persistenceService.findAll(query, page, pageSize);
		Long total = persistenceService.count(query);
		List<ApiEventSchedule> apiSchedules = convertAllEntitiesToApiModels(events);
		ListResponse<ApiEventSchedule> response = new ListResponse<ApiEventSchedule>(apiSchedules, page, pageSize, total);
		
		logger.info("findAssignedOpenEvents: >= startDate: " + startDate + " < endDate: " + endDate);
		
		return response;
	}
	
	@GET
	@Path("assignedListCounts")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<Long> findAssignedOpenEventCounts(
			@QueryParam("year") int year,
			@QueryParam("month") int month) {
		List<Long> counts = new ArrayList<Long>();		
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 1);
		int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		for(int i = 1; i <= days; i++) {
			Date startDate = new DateTime(year, month + 1, i, 0, 0).toDate();
			Date endDate = new DateTime(year, month + 1, i, 0, 0).plusDays(1).toDate();
			
			QueryBuilder<Event> query = createUserSecurityBuilder(Event.class)
			.addOrder("dueDate")
	        .addWhere(WhereClauseFactory.create(Comparator.EQ, "workflowState", Event.WorkflowState.OPEN))
	        .addWhere(WhereClauseFactory.create(Comparator.EQ, "assignee.id", getCurrentUser().getId()))
			.addWhere(WhereClauseFactory.create(Comparator.GE, "startDate", "dueDate", startDate))
			.addWhere(WhereClauseFactory.create(Comparator.LT, "endDate", "dueDate", endDate));
			Long count = persistenceService.count(query);
			counts.add(count);
		}		
		return counts;
	}
}
