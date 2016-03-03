package com.n4systems.fieldid.ws.v1.resources.savedEvent;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.event.ApiEventAttribute;
import com.n4systems.model.*;
import com.n4systems.model.utils.AssetEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiSavedEventResource extends ApiResource<ApiSavedEvent, ThingEvent> {
	@Autowired private ApiSavedEventFormResource apiSavedEventFormResource;
	@Autowired private EventService eventService;
	
	public List<ApiSavedEvent> findLastEventOfEachType(Long assetId) {
		List<ThingEvent> events = eventService.getLastEventOfEachType(assetId);
		return convertAllEntitiesToApiModels(events);
	}
	
	@Override
	protected ApiSavedEvent convertEntityToApiModel(ThingEvent event) {
		ApiSavedEvent apiEvent = new ApiSavedEvent();
		
		convertAbstractEventToApiEvent(apiEvent, event);
		
		apiEvent.setDate(event.getDate());
		apiEvent.setOwnerId(event.getOwner().getId());
		apiEvent.setPerformedById(event.getPerformedBy().getId());
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
		
		apiEvent.setSubEvents(convertToSubApiEvents(event.getSubEvents(), event.getMobileGUID()));
		
		return apiEvent;
	}
	
	private void convertAbstractEventToApiEvent(ApiSavedEvent apiEvent, AbstractEvent<ThingEventType,Asset> event) {
		apiEvent.setSid(event.getMobileGUID());
		apiEvent.setModified(event.getModified());		
		apiEvent.setComments(event.getComments());
		apiEvent.setTypeId(event.getType().getId());
		apiEvent.setAssetId(event.getTarget().getMobileGUID());
		apiEvent.setModifiedById(event.getModifiedBy() == null ? null : event.getModifiedBy().getId());
		
		if (event instanceof AssetEvent && ((AssetEvent)event).getAssetStatus() != null) {
			apiEvent.setAssetStatusId(((AssetEvent)event).getAssetStatus().getId());
		}
		
		if(event.getEventStatus() != null) {
			apiEvent.setEventStatusId(event.getEventStatus().getId());
		}		
		
		apiEvent.setAttributes(convertToApiEventAttributes(event.getInfoOptionMap()));
		apiEvent.setForm(apiSavedEventFormResource.convertToApiEventForm(event));
	}
	
	private List<ApiEventAttribute> convertToApiEventAttributes(Map<String, String> infoOptionMap) {
		if(infoOptionMap.size() > 0) {
			List<ApiEventAttribute> attributes = new ArrayList<ApiEventAttribute>();
			
			for(Map.Entry<String, String> entry : infoOptionMap.entrySet()) {
				ApiEventAttribute attribute = new ApiEventAttribute();
				attribute.setName(entry.getKey());
				attribute.setValue(entry.getValue());
				attributes.add(attribute);
			}
			
			return attributes;
		}
		
		return null;
	}
	
	private List<ApiSavedEvent> convertToSubApiEvents(List<SubEvent> subEvents, String masterSid) {
		if(subEvents != null && subEvents.size() > 0) {
			List<ApiSavedEvent> apiSubEvents = new ArrayList<ApiSavedEvent>();
			
			for(SubEvent subEvent : subEvents) {
				ApiSavedEvent apiSubEvent = new ApiSavedEvent();
				apiSubEvent.setMasterEventSid(masterSid);
				convertAbstractEventToApiEvent(apiSubEvent, subEvent);
				apiSubEvents.add(apiSubEvent);
			}
			
			return apiSubEvents;
		}
		
		return null;
	}
}
