package com.n4systems.fieldid.ws.v1.resources.event;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.certificate.CertificateService;
import com.n4systems.fieldid.service.event.EventCreationService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.ws.v1.resources.eventattachment.ApiEventAttachmentResource;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventStatus;
import com.n4systems.model.EventType;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.Status;
import com.n4systems.model.SubEvent;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.reporting.EventReportType;
import com.n4systems.util.ContentTypeUtil;
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
    @Autowired private CertificateService certificateService;
	
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
	
	@GET
	@Path("downloadReport")
	@Consumes(MediaType.TEXT_PLAIN)
	@Transactional(readOnly = true)
	public Response downloadReport(@QueryParam("eventSid") String eventSid, @QueryParam("eventSid") String reportType) {
		//Get the cert file like from https://n4.fieldid.com/fieldid/file/downloadEventCert.action?uniqueID=725135&reportType=INSPECTION_CERT
		Response response = null;
		QueryBuilder<Event> query = createUserSecurityBuilder(Event.class);
		query.addWhere(WhereClauseFactory.create("mobileGUID", eventSid)); 
		Event event = persistenceService.find(query);
		
		EventReportType eventReportType = EventReportType.valueOf(reportType);
		
		String fileName = null;
		byte[] pdf = null;
		
		try {
			pdf = certificateService.generateEventCertificatePdf(eventReportType, event.getId());	
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			fileName = eventReportType.getReportNamePrefix() + "-" + dateFormatter.format(event.getDate()) + ".pdf";			
			String mediaType = ContentTypeUtil.getContentType(fileName);
			
			response = Response
			.ok(new ByteArrayInputStream(pdf), mediaType)
			.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
			.build();

		} catch(NonPrintableEventType npe) {
			logger.warn("Cert was non-printable", npe);
		} catch(Exception e) {
			logger.error("Unable to download event cert", e);
		}
		
		if(response == null) {
			response = Response.serverError().build();
		}
		
		return response;
	}

	private Event convertApiEvent(ApiEvent apiEvent) {
		Event event = new Event();

        if (apiEvent.getEventScheduleId() != null) {
            // We find the open event, and use this event rather than the updated one. UNLESS it's archived
            EventSchedule schedule = eventScheduleService.findByMobileId(apiEvent.getEventScheduleId());
            if (schedule != null) {
                Event loadedEvent = schedule.getEvent();
                if (loadedEvent.getState() == Archivable.EntityState.ACTIVE && loadedEvent.getEventState() == Event.EventState.OPEN) {
                    event = loadedEvent;
                }
            }
        }

        event.setEventState(Event.EventState.COMPLETED);
		
		// Step 1: Convert abstract-event fields first.
		convertApiEventForAbstractEvent(apiEvent, event);
		
		// Step 2: Convert the non-abstract-event fields
		event.setDate(apiEvent.getDate());
		event.setPrintable(apiEvent.isPrintable());
		event.setOwner(persistenceService.findUsingTenantOnlySecurityWithArchived(BaseOrg.class, apiEvent.getOwnerId()));
		event.setPerformedBy(persistenceService.findUsingTenantOnlySecurityWithArchived(User.class, apiEvent.getPerformedById()));
		
		if (apiEvent.getStatus() != null) {
			event.setStatus(Status.valueOf(apiEvent.getStatus()));
		} else {
			event.setStatus(null);
		}
		
		if (apiEvent.getAssignedUserId() != null) {
			if (apiEvent.getAssignedUserId() < 0) {
				event.setAssignedTo(AssignedToUpdate.unassignAsset());
			} else {
				event.setAssignedTo(AssignedToUpdate.assignAssetToUser(persistenceService.findUsingTenantOnlySecurityWithArchived(User.class, apiEvent.getAssignedUserId())));
			}
		}
		
		if (apiEvent.getEventBookId() != null) {
			event.setBook(findEventBook(apiEvent.getEventBookId()));
		}
		
		if (apiEvent.getPredefinedLocationId() != null) {
			event.getAdvancedLocation().setPredefinedLocation(persistenceService.findUsingTenantOnlySecurityWithArchived(PredefinedLocation.class, apiEvent.getPredefinedLocationId()));
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
		event.setModifiedBy(persistenceService.findUsingTenantOnlySecurityWithArchived(User.class, apiEvent.getModifiedById()));
		
		if (apiEvent.getAssetStatusId() != null) {
			event.setAssetStatus(persistenceService.findUsingTenantOnlySecurityWithArchived(AssetStatus.class, apiEvent.getAssetStatusId()));
		}
		
		if(apiEvent.getEventStatusId() != null) {
			event.setEventStatus(persistenceService.findUsingTenantOnlySecurityWithArchived(EventStatus.class, apiEvent.getEventStatusId()));
		}
		
		if (apiEvent.getForm() != null) {
			EventForm form = persistenceService.findUsingTenantOnlySecurityWithArchived(EventForm.class, apiEvent.getForm().getFormId());
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
        QueryBuilder<Event> query = createTenantSecurityBuilder(Event.class, true);
        query.addWhere(WhereClauseFactory.create("mobileGUID", sid));

        boolean eventExists = persistenceService.exists(query);
        return eventExists;
    }

	private EventBook findEventBook(String eventBookId) {
		QueryBuilder<EventBook> query = createTenantSecurityBuilder(EventBook.class, true);
		query.addWhere(WhereClauseFactory.create("mobileId", eventBookId));
		
		EventBook book = persistenceService.find(query);
		return book;
	}
}
