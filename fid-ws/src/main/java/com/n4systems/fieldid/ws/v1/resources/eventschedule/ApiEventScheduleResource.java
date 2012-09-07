package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.event.criteria.ApiCriteriaImage;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.fieldid.ws.v1.resources.synchronization.ApiSynchronizationResource;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.offlineprofile.OfflineProfile.SyncDuration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.log4j.Logger;
import org.hibernate.impl.CriteriaImpl;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.net.URL;
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
	@Autowired private S3Service s3Service;
	
	public List<ApiEventSchedule>  findAllSchedules(Long assetId, SyncDuration syncDuration) {
		List<ApiEventSchedule> apiEventSchedules = new ArrayList<ApiEventSchedule>();
		
		QueryBuilder<Event> query = createUserSecurityBuilder(Event.class)
		.addOrder("nextDate")
        .addWhere(WhereClauseFactory.create(Comparator.EQ, "eventState", Event.EventState.OPEN))
		.addWhere(WhereClauseFactory.create("asset.id", assetId));
		
		if(syncDuration != SyncDuration.ALL) {
			// If we have a specific syncduration, we should filter schedules that is bounded by start and end dates.
			// This is currently only used for taking assets offline.
			Date startDate = new LocalDate().toDate();
			Date endDate = ApiSynchronizationResource.getSyncEndDate(syncDuration, startDate);
			query.addWhere(WhereClauseFactory.create(Comparator.GE, "nextDate", startDate));
			query.addWhere(WhereClauseFactory.create(Comparator.LE, "nextDate", endDate));
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
		EventSchedule eventSchedule = eventScheduleService.findByMobileId(apiEventSchedule.getSid());

		if (eventSchedule == null) {
            Event event = converApiEventSchedule(apiEventSchedule);
			persistenceService.save(event);
			logger.info("Saved EventSchedule for " + event.getEventType().getName() + " on Asset " + event.getAsset().getMobileGUID());
		} else if (eventSchedule.getEvent().getEventState() == Event.EventState.OPEN) {

            EventType eventType = persistenceService.find(EventType.class, apiEventSchedule.getEventTypeId());

			eventSchedule.setNextDate(apiEventSchedule.getNextDate());
            eventSchedule.setEventType(eventType);
            eventSchedule.getEvent().setNextDate(apiEventSchedule.getNextDate());
            eventSchedule.getEvent().setType(eventType);            
            eventSchedule.getEvent().setAssignee(getAssigneeUser(apiEventSchedule));

            persistenceService.update(eventSchedule.getEvent());
			persistenceService.update(eventSchedule);
			logger.info("Updated EventSchedule for " + eventSchedule.getEventType().getName() + " on Asset " + eventSchedule.getMobileGUID());
		} else {
            logger.warn("Could not update EventSchedule due to event being already completed: " + eventSchedule.getId());
        }
	}
	
	@DELETE
	@Path("{eventScheduleId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void DeleteEventSchedule(@PathParam("eventScheduleId") String eventScheduleId) {
		EventSchedule eventSchedule = eventScheduleService.findByMobileId(eventScheduleId);	
		persistenceService.delete(eventSchedule.getEvent());
        persistenceService.delete(eventSchedule);
		logger.info("deleted schedule for " + eventSchedule.getEventType().getName() + " on asset " + eventSchedule.getMobileGUID());
	}

	@Override
	protected ApiEventSchedule convertEntityToApiModel(Event event) {
		ApiEventSchedule apiSchedule = new ApiEventSchedule();
        // For backward compatibility, we must still use the GUID of the Schedule, since all
        // existing schedules out there in mobile land will refer to schedule GUIDs.
		apiSchedule.setSid(event.getSchedule().getMobileGUID());
		apiSchedule.setActive(true);
		apiSchedule.setModified(event.getModified());
		apiSchedule.setOwnerId(event.getOwner().getId());
		apiSchedule.setAssetId(event.getAsset().getMobileGUID());
		apiSchedule.setEventTypeId(event.getEventType().getId());
		apiSchedule.setEventTypeName(event.getEventType().getName());
		apiSchedule.setNextDate(event.getNextDate());
		if(event.getAssignee() != null) {
			apiSchedule.setAssigneeUserId(event.getAssignee().getId());
		}
		
		if(event.getEventType().getGroup().isAction()) {
			apiSchedule.setAction(true);
			apiSchedule.setPriorityId(event.getPriority().getId());
			apiSchedule.setNotes(event.getNotes());
			
			List<byte[]> images = new ArrayList<byte[]>();
			List<CriteriaResultImage> criteriaResultImages = event.getSourceCriteriaResult().getCriteriaImages();
			for(CriteriaResultImage criteriaResultImage: criteriaResultImages) {
				try {
					byte[] image = s3Service.downloadCriteriaResultImageMedium(criteriaResultImage);
					images.add(image);
				} catch (IOException e) {
					logger.error("Error Fetching EventSchedule.images data for criteriaResultImage" 
							+ criteriaResultImage.getId(), e);
				}
			}
			apiSchedule.setImages(images);
		}
		
		return apiSchedule;
	}
	
	public Event converApiEventSchedule(ApiEventSchedule apiEventSchedule) {
		EventSchedule eventSchedule = new EventSchedule();
		BaseOrg owner = persistenceService.find(BaseOrg.class, apiEventSchedule.getOwnerId());

        Asset asset = assetService.findByMobileId(apiEventSchedule.getAssetId());

        Event event = new Event();
        event.setNextDate(apiEventSchedule.getNextDate());
        event.setTenant(owner.getTenant());
        event.setAsset(asset);
        event.setType(persistenceService.find(EventType.class, apiEventSchedule.getEventTypeId()));
        event.setOwner(asset.getOwner());
        event.setAssignee(getAssigneeUser(apiEventSchedule));

		eventSchedule.setMobileGUID(apiEventSchedule.getSid());
		eventSchedule.setNextDate(apiEventSchedule.getNextDate());
		eventSchedule.setTenant(owner.getTenant());
		eventSchedule.setAsset(asset);
		eventSchedule.setEventType(persistenceService.find(EventType.class, apiEventSchedule.getEventTypeId()));
        eventSchedule.setOwner(asset.getOwner());

        event.setSchedule(eventSchedule);
		
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
		.addOrder("nextDate")
        .addWhere(WhereClauseFactory.create(Comparator.EQ, "eventState", Event.EventState.OPEN))
        .addWhere(WhereClauseFactory.create(Comparator.EQ, "assignee.id", getCurrentUser().getId()))
		.addWhere(WhereClauseFactory.create(Comparator.GE, "startDate", "nextDate", startDate))
		.addWhere(WhereClauseFactory.create(Comparator.LE, "endDate", "nextDate", endDate));		
		
		List<Event> events = persistenceService.findAll(query, page, pageSize);
		Long total = persistenceService.count(query);
		List<ApiEventSchedule> apiSchedules = convertAllEntitiesToApiModels(events);
		ListResponse<ApiEventSchedule> response = new ListResponse<ApiEventSchedule>(apiSchedules, page, pageSize, total);
		
		//Debug log for testing on dev, stage etc.
		logger.info("findAssignedOpenEvents");
		logger.info("startDate: " + startDate);
		logger.info("endDate: " + endDate);
		logger.info("page: " + page);
		logger.info("pageSize: " + pageSize);
		logger.info("Total: " + total);
		logger.info("Total for current page: " + apiSchedules.size());
		
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
			.addOrder("nextDate")
	        .addWhere(WhereClauseFactory.create(Comparator.EQ, "eventState", Event.EventState.OPEN))
	        .addWhere(WhereClauseFactory.create(Comparator.EQ, "assignee.id", getCurrentUser().getId()))
			.addWhere(WhereClauseFactory.create(Comparator.GE, "startDate", "nextDate", startDate))
			.addWhere(WhereClauseFactory.create(Comparator.LT, "endDate", "nextDate", endDate));
			Long count = persistenceService.count(query);
			counts.add(count);
		}		
		return counts;
	}
}
