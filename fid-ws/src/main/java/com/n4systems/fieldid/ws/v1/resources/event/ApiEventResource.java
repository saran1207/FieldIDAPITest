package com.n4systems.fieldid.ws.v1.resources.event;

import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.certificate.CertificateService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.ThingEventCreationService;
import com.n4systems.fieldid.ws.v1.resources.eventattachment.ApiEventAttachmentResource;
import com.n4systems.fieldid.ws.v1.resources.eventschedule.ApiEventSchedule;
import com.n4systems.model.*;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.AssetEvent;
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

@Component
@Path("event")
public class ApiEventResource extends FieldIdPersistenceService {
	private static Logger logger = Logger.getLogger(ApiEventResource.class);
	
	@Autowired private AssetService assetService;
	@Autowired private ApiEventAttachmentResource apiAttachmentResource;
	@Autowired private ApiEventFormResultResource apiEventFormResultResource;
	@Autowired private ApiExistingEventFormResultResource apiExistingEventFormResultResource;	
    @Autowired private ThingEventCreationService eventCreationService;
    @Autowired private EventService eventService;
    @Autowired private CertificateService certificateService;
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveEvent(ApiEvent apiEvent) {
		if(apiEvent.getSid() == null) {
			throw new NullPointerException("ApiEvent has null sid");
		}

        ThingEvent existingEvent;
        if (apiEvent.getEventScheduleId() != null) {
            existingEvent = eventService.findByMobileId(apiEvent.getEventScheduleId(), true);
        } else {
            existingEvent = eventService.findByMobileId(apiEvent.getSid(), true);
        }

        if (existingEvent == null || existingEvent.getWorkflowState() == WorkflowState.OPEN) {
        	createEvent(apiEvent, existingEvent);
        } else {
        	updateEvent(apiEvent, existingEvent);
        }		
	}	
	
	@PUT
	@Path("multi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void multiAddEvent(ApiMultiAddEvent multiAddEvent) {
		ApiEvent apiEvent = multiAddEvent.getEventTemplate();
		for(ApiMultiAddEventItem eventItem : multiAddEvent.getItems()) {
			apiEvent.setSid(eventItem.getEventId());
			apiEvent.setAssetId(eventItem.getAssetId());
			
			ThingEvent event = null;
			if(eventItem.isScheduled()) {
				apiEvent.setEventScheduleId(eventItem.getEventId());
				event = eventService.findByMobileId(eventItem.getEventId(), true);
			} else {
				apiEvent.setEventScheduleId(null);
			}
			
			if(event == null) {
				event = new ThingEvent();
			}
			
			event.setAsset(assetService.findByMobileId(apiEvent.getAssetId(), true));
			
			if(multiAddEvent.isCopyAssignedTo()) {
				User user = event.getAsset().getAssignedUser();
				apiEvent.setAssignedUserId(user != null ? user.getId() : null);
			}
			
			if(multiAddEvent.isCopyOwner()) {
				BaseOrg owner = event.getAsset().getOwner();
				apiEvent.setOwnerId(owner != null ? owner.getId() : null);
			}

            if(multiAddEvent.isCopyAssetStatus()) {
                //Set the AssetStatus on the event to the AssetStatus from the Asset.
                apiEvent.setAssetStatusId(event.getAssetStatus().getId());
            }
			
			if(multiAddEvent.isCopyLocation()) {
				Location location = event.getAsset().getAdvancedLocation();
				if(location != null) {
					apiEvent.setPredefinedLocationId(location.getPredefinedLocation() != null ? location.getPredefinedLocation().getId() : null);
					apiEvent.setFreeformLocation(location.getFreeformLocation());
				} else {
					apiEvent.setPredefinedLocationId(null);
					apiEvent.setFreeformLocation(null);
				}
			}
			
			// Update CriteriaResult, Actions if event had a form.
			if(apiEvent.getForm() != null) {
                // Set sid to eventItem's results and remove it from the list.
                apiEvent.getForm()
                        .getSections()
                        .stream()
                        .filter(sectionResult -> !sectionResult.isHidden())
                        .forEach(sectionResult -> {
                            for (ApiCriteriaResult criteriaResult : sectionResult.getCriteria()) {
                                // Set sid to eventItem's results and remove it from the list.
                                // These sids are sent by client so it can be used to look up when sending criteria images later.
                                criteriaResult.setSid(eventItem.getResults().remove(0).getSid());

                                // Set actions' assetId to eventItem's assetId.
                                for (ApiEventSchedule action : criteriaResult.getActions()) {
                                    action.setAssetId(eventItem.getAssetId());

                                    if (multiAddEvent.isCopyOwner()) {
                                        action.setOwnerId(apiEvent.getOwnerId());
                                    }
                                }

                                // NOTE: In the future we can set recommendations, deficiencies collection's sids too if we want.
                            }
                        });
			}
			
			createEvent(apiEvent, event);
		}
	}
	
	@GET
	@Path("downloadReport")
	@Consumes(MediaType.TEXT_PLAIN)
	@Transactional(readOnly = true)
	public Response downloadReport(@QueryParam("eventSid") String eventSid, @QueryParam("reportType") String reportType) throws Exception {
		QueryBuilder<Event> query = createUserSecurityBuilder(Event.class);
		query.addWhere(WhereClauseFactory.create("mobileGUID", eventSid)); 
		Event event = persistenceService.find(query);		
		EventReportType eventReportType = EventReportType.valueOf(reportType);
		
		try {
			byte[] pdf = certificateService.generateEventCertificatePdf(eventReportType, event.getId());	
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			String fileName = eventReportType.getReportNamePrefix() + "-" + dateFormatter.format(event.getDate()) + ".pdf";			
			String mediaType = ContentTypeUtil.getContentType(fileName);
			
			Response response = Response
			.ok(new ByteArrayInputStream(pdf), mediaType)
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
	
	private void createEvent(ApiEvent apiEvent, ThingEvent event) { // event is either open event or null.
		if(event == null) {
			event = new ThingEvent();
		}
		convertApiEvent(apiEvent, event, false);
        List<FileAttachment> uploadedFiles = apiAttachmentResource.convert(apiEvent.getAttachments(), event.getTenant(), event.getCreatedBy());
        eventCreationService.createEvent(event, 0L, null, uploadedFiles);
		logger.info("Created Event on Asset " + apiEvent.getAssetId());
		logger.info("Event MobileGUID: " + event.getMobileGUID() + " with EventResult: " + event.getEventResult());
	}
	
	private void updateEvent(ApiEvent apiEvent, ThingEvent existingEvent) {
		convertApiEvent(apiEvent, existingEvent, true);
		List<FileAttachment> existingAttachments = new ArrayList<FileAttachment>(existingEvent.getAttachments());
		List<FileAttachment> uploadedFiles = apiAttachmentResource.convert(apiEvent.getAttachments(), existingEvent.getTenant(), existingEvent.getCreatedBy());
		existingAttachments.addAll(uploadedFiles);
		eventCreationService.updateEvent(existingEvent, null, existingAttachments);
		logger.info("Update Event on Asset " + apiEvent.getAssetId());
		logger.info("Event MobileGUID: " + existingEvent.getMobileGUID() + " with EventResult: " + existingEvent.getEventResult());
	}

	private void convertApiEvent(ApiEvent apiEvent, ThingEvent event, boolean isUpdate) {
        event.setWorkflowState(WorkflowState.COMPLETED);
		
		// Step 1: Convert abstract-event fields first.
		convertApiEventForAbstractEvent(apiEvent, event, isUpdate);
		
		//If asset is archived, Archive event also.
		if(event.getAsset().isArchived())
			event.archiveEntity();
		
		// Step 2: Convert the non-abstract-event fields
		event.setDate(apiEvent.getDate());
		event.setPrintable(apiEvent.isPrintable());
		event.setOwner(persistenceService.findUsingTenantOnlySecurityWithArchived(BaseOrg.class, apiEvent.getOwnerId()));
		event.setPerformedBy(persistenceService.findUsingTenantOnlySecurityWithArchived(User.class, apiEvent.getPerformedById()));
		
		
		if (apiEvent.getStatus() != null) {
			event.setEventResult(EventResult.valueOf(apiEvent.getStatus()));
		} else {
			event.setEventResult(EventResult.VOID);
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
		
		if (apiEvent.getPredefinedLocationId() != null && apiEvent.getPredefinedLocationId() > 0) {
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
		
		//TODO Need to refactor the block below once it is functional.
		if(apiEvent.getSubEvents() != null && apiEvent.getSubEvents().size() > 0) {			
			if(!isUpdate) {
				List<SubEvent> subEvents = new ArrayList<SubEvent>();				
				for(ApiEvent apiSubEvent: apiEvent.getSubEvents()) {
					SubEvent subEvent = new SubEvent();
					convertApiEventForAbstractEvent(apiSubEvent, subEvent, isUpdate);
					subEvents.add(subEvent);
				}				
				event.setSubEvents(subEvents);
			} else {
				for(ApiEvent apiSubEvent : apiEvent.getSubEvents()) {
					SubEvent subEvent = null;
					for(SubEvent existingSubEvent : event.getSubEvents()) {
						if(existingSubEvent.getMobileGUID().equals(apiSubEvent.getSid())) {
							subEvent = existingSubEvent;
							break;
						}
					}
					
					// If we did not find subevent, add a new one.
					if(subEvent == null) {
						subEvent = new SubEvent();
						convertApiEventForAbstractEvent(apiSubEvent, subEvent, isUpdate);
						event.getSubEvents().add(subEvent);
					} else {
						convertApiEventForAbstractEvent(apiSubEvent, subEvent, isUpdate);
					}
				}
			}
		}
	}
	
	private void convertApiEventForAbstractEvent(ApiEvent apiEvent, AbstractEvent<ThingEventType,Asset> event, boolean isUpdate) {
		event.setTenant(getCurrentTenant());
		event.setMobileGUID(apiEvent.getSid());
		event.setModified(apiEvent.getModified());
		event.setComments(apiEvent.getComments());		
		event.setType(persistenceService.find(EventType.class, apiEvent.getTypeId()));
		event.setModifiedBy(persistenceService.findUsingTenantOnlySecurityWithArchived(User.class, apiEvent.getModifiedById()));
		
		if(event.getTarget() == null) { // In the case of MultiEvent, we set event.asset much early on.
			event.setTarget(assetService.findByMobileId(apiEvent.getAssetId(), true));
		}

		if (apiEvent.getAssetStatusId() != null) {
            ((AssetEvent)event).setAssetStatus(persistenceService.findUsingTenantOnlySecurityWithArchived(AssetStatus.class, apiEvent.getAssetStatusId()));
		}
		
		if(apiEvent.getEventStatusId() != null) {
			event.setEventStatus(persistenceService.findUsingTenantOnlySecurityWithArchived(EventStatus.class, apiEvent.getEventStatusId()));
		}
		
		if (apiEvent.getForm() != null) {
			EventForm form = persistenceService.findUsingTenantOnlySecurityWithArchived(EventForm.class, apiEvent.getForm().getFormId());
			event.setEventForm(form);			
			
			if(!isUpdate) {
				List<CriteriaResult> results = apiEventFormResultResource.convertApiEventFormResults(apiEvent.getForm(), form, event);
				event.getResults().addAll(results);
			} else {
				apiExistingEventFormResultResource.convertApiEventFormResults(apiEvent.getForm(), event);
			}
		}
		
		if(apiEvent.getAttributes() != null) {
			for(ApiEventAttribute attribute : apiEvent.getAttributes()) {
				event.getInfoOptionMap().put(attribute.getName(), attribute.getValue());
			}
		}
	}

	private EventBook findEventBook(String eventBookId) {
		QueryBuilder<EventBook> query = createTenantSecurityBuilder(EventBook.class, true);
		query.addWhere(WhereClauseFactory.create("mobileId", eventBookId));
		
		EventBook book = persistenceService.find(query);
		return book;
	}
}
