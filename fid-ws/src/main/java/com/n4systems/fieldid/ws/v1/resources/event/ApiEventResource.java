package com.n4systems.fieldid.ws.v1.resources.event;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.service.event.EventCreationService;
import com.n4systems.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.ejb.parameters.CreateEventParameterBuilder;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.ws.v1.resources.eventattachment.ApiEventAttachmentResource;
import com.n4systems.handlers.creator.events.factory.ProductionEventPersistenceFactory;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

@Component
@Path("event")
public class ApiEventResource extends FieldIdPersistenceService {
	private static Logger logger = Logger.getLogger(ApiEventResource.class);
	
	@Autowired private AssetService assetService;
	@Autowired private ApiEventAttachmentResource apiAttachmentResource;
	@Autowired private ApiEventFormResultResource apiEventFormResultResource;
	@Autowired private EventScheduleService eventScheduleService;
    @Autowired private EventCreationService eventCreationService;
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveEvent(ApiEvent apiEvent) {

        if (apiEvent.getSid() != null && eventExists(apiEvent.getSid())) {
            logger.warn("Event with SID [" + apiEvent.getSid() + "] already exists");
            return;
        }

		Event event = convertApiEvent(apiEvent);

        List<FileAttachment> uploadedFiles = apiAttachmentResource.convert(apiEvent.getAttachments(), event.getTenant(), event.getCreatedBy());

        eventCreationService.createEvent(event, 0L, null, uploadedFiles);
		logger.info("Saved Event on Asset " + apiEvent.getAssetId());
	}

	private Event convertApiEvent(ApiEvent apiEvent) {
		Event event = new Event();

        if (apiEvent.getEventScheduleId() != null) {
            EventSchedule schedule = eventScheduleService.findByMobileId(apiEvent.getEventScheduleId());
            event = schedule.getEvent();
        }

        event.setEventState(Event.EventState.COMPLETED);
		
		// Step 1: Convert abstract-event fields first.
		convertApiEventForAbstractEvent(apiEvent, event);
		
		// Step 2: Convert the non-abstract-event fields
		event.setDate(apiEvent.getDate());
		event.setPrintable(apiEvent.isPrintable());
		event.setOwner(persistenceService.find(BaseOrg.class, apiEvent.getOwnerId()));
		event.setPerformedBy(persistenceService.find(User.class, apiEvent.getPerformedById()));
		
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
			event.setBook(findEventBook(apiEvent.getEventBookId()));
		}
		
		if (apiEvent.getPredefinedLocationId() != null) {
			event.getAdvancedLocation().setPredefinedLocation(persistenceService.find(PredefinedLocation.class, apiEvent.getPredefinedLocationId()));
		}
		
		if (apiEvent.getFreeformLocation() != null) {
			event.getAdvancedLocation().setFreeformLocation(apiEvent.getFreeformLocation());
		}
		
		if (apiEvent.getGpsLatitude() != null && apiEvent.getGpsLongitude() != null) {
            if (event.getGpsLocation() == null) event.setGpsLocation(new GpsLocation());
			event.getGpsLocation().setLatitude(apiEvent.getGpsLatitude());
			event.getGpsLocation().setLongitude(apiEvent.getGpsLongitude());
		}
		
		if(apiEvent.getSubEvents() != null && apiEvent.getSubEvents().size() > 0) {
			List<SubEvent> subEvents = new ArrayList<SubEvent>();
			
			for(ApiEvent apiSubEvent: apiEvent.getSubEvents()) {
				SubEvent subEvent = convertApiEventIntoSubEvent(apiSubEvent);
				subEvents.add(subEvent);
			}
			
			event.setSubEvents(subEvents);
			
			logger.info("Added " + subEvents.size() + " SubEvents for Event on Asset " + apiEvent.getAssetId());
		}
		
		return event;
	}
	
	private SubEvent convertApiEventIntoSubEvent(ApiEvent apiEvent) {
		SubEvent subEvent = new SubEvent();
		convertApiEventForAbstractEvent(apiEvent, subEvent);
		return subEvent;
	}
	
	private void convertApiEventForAbstractEvent(ApiEvent apiEvent, AbstractEvent event) {
		event.setTenant(getCurrentTenant());
		event.setMobileGUID(apiEvent.getSid());
		event.setCreated(apiEvent.getModified());
		event.setModified(apiEvent.getModified());		
		event.setComments(apiEvent.getComments());		
		event.setType(persistenceService.find(EventType.class, apiEvent.getTypeId()));
		event.setAsset(assetService.findByMobileId(apiEvent.getAssetId()));
		event.setModifiedBy(persistenceService.find(User.class, apiEvent.getModifiedById()));
		
		if (apiEvent.getAssetStatusId() != null) {
			event.setAssetStatus(persistenceService.find(AssetStatus.class, apiEvent.getAssetStatusId()));
		}
		
		if(apiEvent.getEventStatusId() != null) {
			event.setEventStatus(persistenceService.find(EventStatus.class, apiEvent.getEventStatusId()));
		}
		
		if (apiEvent.getForm() != null) {
			EventForm form = persistenceService.find(EventForm.class, apiEvent.getForm().getFormId());
			event.setEventForm(form);
			
			List<CriteriaResult> results = apiEventFormResultResource.convertApiEventFormResults(apiEvent.getForm(), form, event);
			event.getResults().addAll(results);
		}
		
		if(apiEvent.getAttributes() != null) {
			for(ApiEventAttribute attribute : apiEvent.getAttributes()) {
				event.getInfoOptionMap().put(attribute.getName(), attribute.getValue());
			}
		}
	}

    private boolean eventExists(String sid) {
        QueryBuilder<Event> query = createTenantSecurityBuilder(Event.class);
        query.addWhere(WhereClauseFactory.create("mobileGUID", sid));

        boolean eventExists = persistenceService.exists(query);
        return eventExists;
    }

	private EventBook findEventBook(String eventBookId) {
		QueryBuilder<EventBook> query = createUserSecurityBuilder(EventBook.class);
		query.addWhere(WhereClauseFactory.create("mobileId", eventBookId));
		
		EventBook book = persistenceService.find(query);
		return book;
	}
}
