package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

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
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

@Component
@Path("eventSchedule")
public class ApiEventScheduleResource extends ApiResource<ApiEventSchedule, EventSchedule> {
	private static Logger logger = Logger.getLogger(ApiEventScheduleResource.class);
	
	@Autowired private EventScheduleService eventScheduleService;
	@Autowired private PersistenceService persistenceService;
	@Autowired private AssetService assetService;
	
	public List<ApiEventSchedule>  findAllSchedules(Long assetId) {
		List<ApiEventSchedule> apiEventSchedules = new ArrayList<ApiEventSchedule>();		
		List<EventSchedule> schedules = eventScheduleService.getIncompleteSchedules(assetId);
		for (EventSchedule schedule: schedules) {
			apiEventSchedules.add(convertEntityToApiModel(schedule));
		}
		
		return apiEventSchedules;
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveEventSchedule(ApiEventSchedule apiEventSchedule) {
		EventSchedule eventSchedule = findEventScheduleByMobileId(apiEventSchedule.getSid());
		
		if (eventSchedule == null) {
			eventSchedule = converApiEventSchedule(apiEventSchedule);
			persistenceService.save(eventSchedule);
			logger.info("Saved EventSchedule for " + eventSchedule.getEventType().getName() + " on Asset " + eventSchedule.getMobileGUID());
		} else {
			eventSchedule.setNextDate(apiEventSchedule.getNextDate());
			eventSchedule.setEventType(persistenceService.find(EventType.class, apiEventSchedule.getEventTypeId()));
			persistenceService.update(eventSchedule);
			logger.info("Updated EventSchedule for " + eventSchedule.getEventType().getName() + " on Asset " + eventSchedule.getMobileGUID());
		}
	}
	
	@DELETE
	@Path("{eventScheduleId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void DeleteEventSchedule(@PathParam("eventScheduleId") String eventScheduleId) {
		EventSchedule eventSchedule = findEventScheduleByMobileId(eventScheduleId);	
		persistenceService.delete(eventSchedule);
		logger.info("deleted schedule for " + eventSchedule.getEventType().getName() + " on asset " + eventSchedule.getMobileGUID());
	}

	protected EventSchedule findEventScheduleByMobileId(String eventScheduleId) {
		QueryBuilder<EventSchedule> query = createUserSecurityBuilder(EventSchedule.class);
		query.addWhere(WhereClauseFactory.create("mobileGUID", eventScheduleId));		
		EventSchedule eventSchedule = persistenceService.find(query);
		return eventSchedule;
	}

	@Override
	protected ApiEventSchedule convertEntityToApiModel(EventSchedule schedule) {
		ApiEventSchedule apiSchedule = new ApiEventSchedule();
		apiSchedule.setSid(schedule.getMobileGUID());
		apiSchedule.setActive(true);
		apiSchedule.setModified(schedule.getModified());
		apiSchedule.setOwnerId(schedule.getOwner().getId());
		apiSchedule.setAssetId(schedule.getAsset().getMobileGUID());
		apiSchedule.setEventTypeId(schedule.getEventType().getId());
		apiSchedule.setEventTypeName(schedule.getEventType().getName());
		apiSchedule.setNextDate(schedule.getNextDate());
		return apiSchedule;
	}
	
	private EventSchedule converApiEventSchedule(ApiEventSchedule apiEventSchedule) {
		EventSchedule eventSchedule = new EventSchedule();
		BaseOrg owner = persistenceService.find(BaseOrg.class, apiEventSchedule.getOwnerId());
		
		eventSchedule.setMobileGUID(apiEventSchedule.getSid());
		eventSchedule.setNextDate(apiEventSchedule.getNextDate());
		eventSchedule.setTenant(owner.getTenant());
		eventSchedule.setAsset(assetService.findByMobileId(apiEventSchedule.getAssetId()));
		eventSchedule.setEventType(persistenceService.find(EventType.class, apiEventSchedule.getEventTypeId()));		
		
		return eventSchedule;
	}
}
