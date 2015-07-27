package com.n4systems.fieldid.ws.v2.resources.customerdata.event;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.*;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.utils.ActionDescriptionUtil;
import com.n4systems.model.utils.AssetEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApiEventToModelConverter {
	
	@Autowired private ApiCriteriaResultConverter apiCriteriaResultConverter;
	@Autowired private S3Service s3Service;

	protected ApiEvent convertEntityToApiModel(ThingEvent event) {
		ApiEvent apiEvent = new ApiEvent();

		convertAbstractEventToApiEvent(apiEvent, event);

		apiEvent.setWorkflowState(event.getWorkflowState());
		apiEvent.setDueDate(event.getDueDate());
		apiEvent.setDate(event.getDate());
		apiEvent.setOwnerId(event.getOwner().getId());
		apiEvent.setPrintable(event.isPrintable());

		if (event.getPerformedBy() != null) {
			apiEvent.setPerformedById(event.getPerformedBy().getId());
		}

		if (event.getGpsLocation() != null && !event.getGpsLocation().isEmpty()) {
			apiEvent.setGpsLatitude(event.getGpsLocation().getLatitude());
			apiEvent.setGpsLongitude(event.getGpsLocation().getLongitude());
		}

		if(event.getAssignedTo() != null && event.getAssignedTo().getAssignedUser() != null) {
			apiEvent.setAssignedUserId(event.getAssignedTo().getAssignedUser().getId());
		}

		if (event.getAssignee() != null) {
			apiEvent.setAssignedUserId(event.getAssignee().getId());
		}

		if(event.getAssignedGroup() != null) {
			apiEvent.setAssignedUserGroupId(event.getAssignedGroup().getId());
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

		apiEvent.setSubEvents(convertToSubApiEvents(event.getSubEvents()));

		if (event.isAction()) {
			apiEvent.setAction(true);
			apiEvent.setPriorityId(event.getPriority().getId());
			apiEvent.setNotes(event.getNotes());
			apiEvent.setTriggerEventInfo(convertTriggerEventToApiModel(event));
		}

		return apiEvent;
	}

	private void convertAbstractEventToApiEvent(ApiEvent apiEvent, AbstractEvent<ThingEventType, Asset> event) {
		apiEvent.setSid(event.getMobileGUID());
		apiEvent.setModified(event.getModified());
		apiEvent.setActive(true);
		apiEvent.setComments(event.getComments());
		apiEvent.setTypeId(event.getType().getId());
		apiEvent.setAssetId(event.getTarget().getMobileGUID());

		if (event.getModifiedBy() != null) {
			apiEvent.setModifiedById(event.getModifiedBy().getId());
		}

		if (event instanceof AssetEvent && ((AssetEvent) event).getAssetStatus() != null) {
			apiEvent.setAssetStatusId(((AssetEvent) event).getAssetStatus().getId());
		}

		if(event.getEventStatus() != null) {
			apiEvent.setEventStatusId(event.getEventStatus().getId());
		}

		if (event.getEventForm() != null) {
			apiEvent.setFormId(event.getEventForm().getId());
		}

		apiEvent.setAttributes(convertToApiEventAttributes(event.getInfoOptionMap()));
		apiEvent.setResults(event.getResults().stream().map(r -> apiCriteriaResultConverter.convert(r, event.getId())).collect(Collectors.toList()));
	}

	private List<ApiEventAttribute> convertToApiEventAttributes(Map<String, String> infoOptionMap) {
		return infoOptionMap.entrySet().stream().map(e -> {
			ApiEventAttribute attribute = new ApiEventAttribute();
			attribute.setName(e.getKey());
			attribute.setValue(e.getValue());
			return attribute;
		}).collect(Collectors.toList());
	}

	private List<ApiEvent> convertToSubApiEvents(List<SubEvent> subEvents) {
		return subEvents.stream().map(e -> {
			ApiEvent apiSubEvent = new ApiEvent();
			convertAbstractEventToApiEvent(apiSubEvent, e);
			return apiSubEvent;
		}).collect(Collectors.toList());
	}

	private ApiTriggerEvent convertTriggerEventToApiModel(Event actionEvent) {
		ApiTriggerEvent triggerEvent = new ApiTriggerEvent();
		triggerEvent.setName(actionEvent.getTriggerEvent().getType().getName());
		triggerEvent.setDate(actionEvent.getTriggerEvent().getDate());
		triggerEvent.setPerformedBy(actionEvent.getTriggerEvent().getPerformedBy().getFullName());
		triggerEvent.setCriteria(ActionDescriptionUtil.getDescription(actionEvent.getTriggerEvent(), actionEvent.getSourceCriteriaResult()));

		List<CriteriaResultImage> criteriaImages = actionEvent.getSourceCriteriaResult().getCriteriaImages();
		triggerEvent.setImageUrls(criteriaImages.stream().map(s3Service::getCriteriaResultImageMediumURL).collect(Collectors.toList()));
		triggerEvent.setImageComments(criteriaImages.stream().map(CriteriaResultImage::getComments).collect(Collectors.toList()));
		return triggerEvent;
	}

}
