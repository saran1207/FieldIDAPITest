package com.n4systems.fieldid.ws.v1.resources.savedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.event.ApiEventAttribute;
import com.n4systems.model.Event;
import com.n4systems.model.SubEvent;

public class ApiSavedEventResource extends ApiResource<ApiSavedEvent, Event> {
	@Autowired private ApiSavedEventFormResource apiSavedEventFormResource;
	@Autowired private EventService eventService;
	
	public List<ApiSavedEvent> findLastEventOfEachType(Long assetId) {
		List<Event> events = eventService.getLastEventOfEachType(assetId);		
		return convertAllEntitiesToApiModels(events);
	}
	
	@Override
	protected ApiSavedEvent convertEntityToApiModel(Event event) {
		ApiSavedEvent apiEvent = new ApiSavedEvent();
		
		apiEvent.setSid(event.getMobileGUID());
		apiEvent.setModified(event.getModified());
		apiEvent.setDate(event.getDate());
		apiEvent.setComments(event.getComments());
		apiEvent.setTypeId(event.getType().getId());
		apiEvent.setAssetId(event.getAsset().getMobileGUID());
		apiEvent.setOwnerId(event.getOwner().getId());		
		apiEvent.setModifiedById(event.getModifiedBy().getId());
		apiEvent.setPerformedById(event.getPerformedBy().getId());
		apiEvent.setPrintable(event.isPrintable());
		
		if(event.getAssignedTo() != null) {
			apiEvent.setAssignedUserId(event.getAssignedTo().getAssignedUser().getId());
		}
		
		if(event.getBook() != null) {
			apiEvent.setEventBookId(event.getBook().getMobileId());
		}
		
		if(event.getAssetStatus() != null) {
			apiEvent.setAssetStatusId(event.getAssetStatus().getId());
		}
		
		if(event.getEventStatus() != null) {
			apiEvent.setEventStatusId(event.getEventStatus().getId());
		}
		
		if(event.getStatus() != null) {
			apiEvent.setStatus(event.getStatus().getDisplayName());
		}
		
		if(event.getAdvancedLocation() != null) {
			apiEvent.setFreeformLocation(event.getAdvancedLocation().getFreeformLocation());
		}
		
		apiEvent.setForm(apiSavedEventFormResource.convertEntityToApiModel(event.getEventForm()));
		apiEvent.setAttributes(convertToApiEventAttributes(event.getInfoOptionMap()));
		apiEvent.setSubEvents(convertToSubApiEvents(event.getSubEvents()));
		
		return apiEvent;
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
	
	private List<ApiSavedEvent> convertToSubApiEvents(List<SubEvent> subEvents) {
		return null;
	}
}
