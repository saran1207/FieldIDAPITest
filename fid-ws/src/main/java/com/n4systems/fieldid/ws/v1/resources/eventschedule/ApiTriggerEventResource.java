package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.Event;
import com.n4systems.model.criteriaresult.CriteriaResultImage;

public class ApiTriggerEventResource extends ApiResource<ApiTriggerEvent, Event> {
	private static Logger logger = Logger.getLogger(ApiTriggerEventResource.class);
	
	@Autowired private S3Service s3Service;
	
	@Override
	protected ApiTriggerEvent convertEntityToApiModel(Event event) {
		ApiTriggerEvent triggerEvent = new ApiTriggerEvent();
		triggerEvent.setName(event.getTriggerEvent().getType().getName());
		triggerEvent.setDate(event.getTriggerEvent().getDate());
		triggerEvent.setPerformedBy(event.getTriggerEvent().getPerformedBy().getFullName());
		triggerEvent.setImages(getImages(event));
		triggerEvent.setCriteria("Test"); // TODO Criteria
		
		return triggerEvent;
	}
	
	private List<byte[]> getImages(Event event) {
		List<byte[]> images = new ArrayList<byte[]>();
		List<CriteriaResultImage> criteriaResultImages = event.getSourceCriteriaResult().getCriteriaImages();
		for(CriteriaResultImage criteriaResultImage: criteriaResultImages) {
			try {
				byte[] image = s3Service.downloadCriteriaResultImageMedium(criteriaResultImage);
				images.add(image);
			} catch (IOException e) {
				logger.error("Error Fetching EventSchedule.images data for criteriaResultImage" 
						+ criteriaResultImage.getId(), e);
			}
		}
		
		return images;
	}
}