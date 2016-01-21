package com.n4systems.fieldid.ws.v1.resources.savedEvent;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.event.ApiEventAttribute;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.PlaceEventType;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.orgs.BaseOrg;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This resource provides the same functionality that ApiSavedEventResource does, but for PlaceEvents instead of
 * ThingEvents.
 *
 * Created by Jordan Heath on 2015-10-22.
 */
public class ApiSavedPlaceEventResource extends ApiResource<ApiSavedPlaceEvent, PlaceEvent> {
    @Autowired
    private ApiSavedEventFormResource savedEventFormResource;

    @Autowired
    private EventService eventService;

    public List<ApiSavedPlaceEvent> findLastEventOfEachType(Long placeId) {
        List<PlaceEvent> events = eventService.getLastPlaceEventOfEachType(placeId);
        return convertAllEntitiesToApiModels(events);
    }

    public List<ApiSavedPlaceEvent> findAllOpenEvents(BaseOrg baseOrg) {
        List<PlaceEvent> events = eventService.getAllOpenPlaceEvents(baseOrg);
        return convertAllEntitiesToApiModels(events);
    }

    @Override
    protected ApiSavedPlaceEvent convertEntityToApiModel(PlaceEvent event) {
        ApiSavedPlaceEvent apiEvent = new ApiSavedPlaceEvent();

        convertAbstractEventToApiEvent(apiEvent, event);

        apiEvent.setDate(event.getDate());
        apiEvent.setOwnerId(event.getOwner().getId());

        if(event.getWorkflowState() == WorkflowState.COMPLETED) {
            apiEvent.setPerformedById(event.getPerformedBy().getId());
        }

        apiEvent.setPrintable(event.isPrintable());

        if(event.getAssignedTo() != null && event.getAssignedTo().getAssignedUser() != null) {
            apiEvent.setAssignedUserId(event.getAssignedTo().getAssignedUser().getId());
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

        return apiEvent;
    }

    private void convertAbstractEventToApiEvent(ApiSavedPlaceEvent apiEvent, AbstractEvent<PlaceEventType, BaseOrg> event) {
        apiEvent.setSid(event.getMobileGUID());
        apiEvent.setModified(event.getModified());
        apiEvent.setComments(event.getComments());
        apiEvent.setTypeId(event.getType().getId());
        apiEvent.setModifiedById(event.getModifiedBy() == null ? null : event.getModifiedBy().getId());
        apiEvent.setPlaceId(event.getTarget().getId());

        if(event.getEventStatus() != null) {
            apiEvent.setEventStatusId(event.getEventStatus().getId());
        }

        apiEvent.setAttributes(convertToApiEventAttributes(event.getInfoOptionMap()));
        apiEvent.setForm(savedEventFormResource.convertToApiEventForm(event));
    }

    private List<ApiEventAttribute> convertToApiEventAttributes(Map<String, String> infoOptionMap) {
        if(infoOptionMap.size() > 0) {
            //Stream power.  Pull out the entry set...
            return infoOptionMap.entrySet()
                                //...stream it...
                                .stream()
                                //...map it to the ApiEventAttribute class...
                                .map(entry -> {
                                        ApiEventAttribute attribute = new ApiEventAttribute();
                                        attribute.setName(entry.getKey());
                                        attribute.setValue(entry.getValue());
                                        return attribute;
                                })
                                //...then return the resulting List.
                                .collect(Collectors.toList());
        }


        return null;
    }
}
