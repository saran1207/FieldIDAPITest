package com.n4systems.fieldid.ws.v1.resources.event;

import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.certificate.CertificateService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.service.event.PlaceEventCreationService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.ws.v1.resources.eventattachment.ApiEventAttachmentResource;
import com.n4systems.model.*;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.reporting.EventReportType;
import com.n4systems.util.ContentTypeUtil;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * This is the Place Event API Endpoint.  It allows users to GET and PUT PlaceEvents.
 *
 * Created by Jordan Heath on 2015-10-23.
 */
@Component
@Path("placeevent")
public class ApiPlaceEventResource extends FieldIdPersistenceService {
    private static final Logger logger = Logger.getLogger(ApiPlaceEventResource.class);

    @Autowired private EventService eventService;
    @Autowired private ApiEventFormResultResource apiEventFormResultResource;
    @Autowired private ApiExistingEventFormResultResource apiExistingEventFormResultResource;
    @Autowired private CertificateService certificateService;
    @Autowired private PlaceEventCreationService eventCreationService;
    @Autowired private ApiEventAttachmentResource apiAttachmentResource;
    @Autowired private UserService userService;
    @Autowired private OrgService orgService;
    @Autowired private EventTypeService eventTypeService;


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void saveEvent(ApiPlaceEvent apiEvent) {
        if(apiEvent.getSid() == null) {
            throw new NullPointerException("ApiPlaceEvent has null sid");
        }

        PlaceEvent existingEvent;
        if(apiEvent.getEventScheduleId() != null) {
            existingEvent = eventService.findPlaceEventByMobileId(apiEvent.getEventScheduleId(), true);
        } else {
            existingEvent = eventService.findPlaceEventByMobileId(apiEvent.getSid(), true);
        }

        if(existingEvent == null || existingEvent.getWorkflowState() == WorkflowState.OPEN) {
            createEvent(apiEvent, existingEvent);
        } else {
            updateEvent(apiEvent, existingEvent);
        }
    }

    @GET
    @Path("downloadReport")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional(readOnly = true)
    public Response downloadReport(@QueryParam("eventSid") String eventSid, @QueryParam("reportType") String reportType) throws Exception {
        QueryBuilder<Event> query = createUserSecurityBuilder(Event.class);
        query.addWhere(WhereClauseFactory.create("mobileGUID", eventSid));
        Event event = persistenceService.find(query);
        EventReportType eventReportType = EventReportType.valueOf(reportType);

        try {
            byte[] pdf = certificateService.generateEventCertificatePdf(eventReportType, event.getId());
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = eventReportType.getReportNamePrefix() + "-" + dateFormatter.format((event.getDate()) + ".pdf");
            String mediaType = ContentTypeUtil.getContentType(fileName);

            Response response = Response.ok(new ByteArrayInputStream(pdf), mediaType)
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .build();

            return response;
        } catch(NonPrintableEventType npe) {
            logger.warn("Cert was non-printable for event: " + event.getId());
            throw npe;
        } catch(Exception e) {
            logger.error("Unable to download event cert for event: " + event.getId());
            throw e;
        }
    }

    private void createEvent(ApiPlaceEvent apiEvent, PlaceEvent event) {
        if(event == null) {
            event = new PlaceEvent();
        }

        convertApiPlaceEvent(apiEvent, event, false);
        List<FileAttachment> uploadedFiles = apiAttachmentResource.convert(apiEvent.getAttachments(), event.getTenant(), event.getCreatedBy());
        eventCreationService.createEvent(event, 0L, null, uploadedFiles);
        logger.info("Created Place Event on Place " + apiEvent.getPlaceId());
        logger.info("PlaceEvent MobileGUID: " + event.getMobileGUID() + " with EventResult: " + event.getEventResult());
    }

    @Transactional //Not sure if this is necessary, since the calling method declares itself as Transactional...
    private void updateEvent(ApiPlaceEvent apiEvent, PlaceEvent existingEvent) {
        convertApiPlaceEvent(apiEvent, existingEvent, true);
        List<FileAttachment> existingAttachments = new ArrayList<>(existingEvent.getAttachments());
        List<FileAttachment> uploadedFiles = apiAttachmentResource.convert(apiEvent.getAttachments(), existingEvent.getTenant(), existingEvent.getCreatedBy());

        existingAttachments.addAll(uploadedFiles);
        eventCreationService.updateEvent(existingEvent, null, existingAttachments);
        logger.info("Update Event on Place " + apiEvent.getPlaceId());
        logger.info("Place Event MobileGUID: " + existingEvent.getMobileGUID() + " with EventResult: " + existingEvent.getEventResult());
    }

    private void convertApiPlaceEvent(ApiPlaceEvent apiEvent, PlaceEvent event, boolean isUpdate) {
        event.setWorkflowState(WorkflowState.COMPLETED);

        convertApiPlaceEventForAbstractEvent(apiEvent, event, isUpdate);

        event.setPlace(orgService.findById(apiEvent.getPlaceId()));

        if(event.getPlace().isArchived()) {
            event.archiveEntity();
        } else {
            event.activateEntity();
        }

        event.setDate(apiEvent.getDate());
        event.setPrintable(apiEvent.isPrintable());
        event.setOwner(persistenceService.findUsingTenantOnlySecurityWithArchived(BaseOrg.class, apiEvent.getOwnerId()));
        event.setPerformedBy(userService.findById(apiEvent.getPerformedById()));

        if(apiEvent.getStatus() != null) {
            event.setEventResult(EventResult.valueOf(apiEvent.getStatus()));
        } else {
            event.setEventResult(EventResult.VOID);
        }

        if(apiEvent.getEventBookId() != null) {
            event.setBook(findEventBook(apiEvent.getEventBookId()));
        }

        if(apiEvent.getPredefinedLocationId() != null && apiEvent.getPredefinedLocationId() > 0) {
            event.getAdvancedLocation().setPredefinedLocation(persistenceService.findUsingTenantOnlySecurityWithArchived(PredefinedLocation.class, apiEvent.getPredefinedLocationId()));
        }

        if(apiEvent.getFreeformLocation() != null) {
            event.getAdvancedLocation().setFreeformLocation(apiEvent.getFreeformLocation());
        }

        if(apiEvent.getGpsLatitude() != null && apiEvent.getGpsLongitude() != null) {
            if(event.getGpsLocation() == null) {
                event.setGpsLocation(new GpsLocation());
            }
            event.getGpsLocation().setLatitude(apiEvent.getGpsLatitude());
            event.getGpsLocation().setLongitude(apiEvent.getGpsLongitude());
        }
    }

    @SuppressWarnings("unchecked") //Because I hate warnings.
    private void convertApiPlaceEventForAbstractEvent(ApiPlaceEvent apiEvent, AbstractEvent<PlaceEventType, BaseOrg> event, boolean isUpdate) {
        event.setTenant(getCurrentTenant());
        event.setMobileGUID(apiEvent.getSid());
        event.setType(eventTypeService.getEventType(apiEvent.getTypeId()));

        event.setModified(apiEvent.getModified());
        if(apiEvent.getModifiedById() != null) {
            event.setModifiedBy(userService.findById(apiEvent.getModifiedById()));
        }

        if(apiEvent.getEventStatusId() != null) {
            event.setEventStatus(persistenceService.findUsingTenantOnlySecurityWithArchived(EventStatus.class, apiEvent.getEventStatusId()));
        }

        if(apiEvent.getForm() != null) {
            EventForm form = persistenceService.findUsingTenantOnlySecurityWithArchived(EventForm.class, apiEvent.getForm().getFormId());
            event.setEventForm(form);

            if(isUpdate) {
                apiExistingEventFormResultResource.convertApiEventFormResults(apiEvent.getForm(), event);
            } else {
                List<CriteriaResult> results = apiEventFormResultResource.convertApiEventFormResults(apiEvent.getForm(), form, event);
                event.getResults().addAll(results);
            }
        }

        if(apiEvent.getAttachments() != null) {
            apiEvent.getAttributes().forEach(attribute -> event.getInfoOptionMap().put(attribute.getName(), attribute.getValue()));
        }
    }

    private EventBook findEventBook(String eventBookId) {
        QueryBuilder<EventBook> query = createTenantSecurityBuilder(EventBook.class, true);
        query.addWhere(WhereClauseFactory.create("mobileId", eventBookId));

        EventBook book = persistenceService.find(query);
        return book;
    }
}
