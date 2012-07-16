package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.n4systems.model.Event;
import com.n4systems.model.EventGroup;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.orgs.BaseOrg;

@Component
@Path("eventSchedule")
public class ApiEventScheduleResource extends ApiResource<ApiEventSchedule, Event> {
	private static Logger logger = Logger.getLogger(ApiEventScheduleResource.class);
	
	@Autowired private EventScheduleService eventScheduleService;
	@Autowired private PersistenceService persistenceService;
	@Autowired private AssetService assetService;
	
	public List<ApiEventSchedule>  findAllSchedules(Long assetId) {
		List<ApiEventSchedule> apiEventSchedules = new ArrayList<ApiEventSchedule>();		
		List<Event> schedules = eventScheduleService.getIncompleteSchedules(assetId);
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
		return apiSchedule;
	}
	
	private Event converApiEventSchedule(ApiEventSchedule apiEventSchedule) {
		EventSchedule eventSchedule = new EventSchedule();
		BaseOrg owner = persistenceService.find(BaseOrg.class, apiEventSchedule.getOwnerId());

        EventGroup eventGroup = new EventGroup();
        persistenceService.save(eventGroup);

        Event event = new Event();
        event.setNextDate(apiEventSchedule.getNextDate());
        event.setTenant(owner.getTenant());
        event.setAsset(assetService.findByMobileId(apiEventSchedule.getAssetId()));
        event.setType(persistenceService.find(EventType.class, apiEventSchedule.getEventTypeId()));
        event.setGroup(eventGroup);

		eventSchedule.setMobileGUID(apiEventSchedule.getSid());
		eventSchedule.setNextDate(apiEventSchedule.getNextDate());
		eventSchedule.setTenant(owner.getTenant());
		eventSchedule.setAsset(assetService.findByMobileId(apiEventSchedule.getAssetId()));
		eventSchedule.setEventType(persistenceService.find(EventType.class, apiEventSchedule.getEventTypeId()));

        event.setSchedule(eventSchedule);
		
		return event;
	}
}
