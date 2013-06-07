package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.Event;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.utils.ActionDescriptionUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiTriggerEventResource extends ApiResource<ApiTriggerEvent, Event> {
	private static Logger logger = Logger.getLogger(ApiTriggerEventResource.class);
	
	@Autowired private S3Service s3Service;
	
	@Override
	protected ApiTriggerEvent convertEntityToApiModel(Event actionEvent) {
		ApiTriggerEvent triggerEvent = new ApiTriggerEvent();
		triggerEvent.setName(actionEvent.getTriggerEvent().getType().getName());
		triggerEvent.setDate(actionEvent.getTriggerEvent().getDate());
		triggerEvent.setPerformedBy(actionEvent.getTriggerEvent().getPerformedBy().getFullName());
        List<CriteriaResultImage> criteriaImages = actionEvent.getSourceCriteriaResult().getCriteriaImages();
		triggerEvent.setImages(getImages(criteriaImages));
        triggerEvent.setImageComments(getImageComments(criteriaImages));
		triggerEvent.setCriteria(ActionDescriptionUtil.getDescription(actionEvent.getTriggerEvent(), actionEvent.getSourceCriteriaResult()));
		
		return triggerEvent;
	}

    private List<String> getImageComments(List<CriteriaResultImage> criteriaImages) {
        List<String> imageComments = new ArrayList<String>();

        for (CriteriaResultImage criteriaImage : criteriaImages) {
            imageComments.add(criteriaImage.getComments());
        }

        return imageComments;
    }

    private List<byte[]> getImages(List<CriteriaResultImage> criteriaImages) {
		List<byte[]> images = new ArrayList<byte[]>();

        List<CriteriaResultImage> criteriaResultImages = criteriaImages;
		for(CriteriaResultImage criteriaResultImage: criteriaResultImages) {
			try {
				byte[] image = s3Service.downloadCriteriaResultImageMedium(criteriaResultImage);
				if(image != null) {
					images.add(image);
				}
			} catch (IOException e) {
				logger.error("Error Fetching EventSchedule.images data for criteriaResultImage" 
						+ criteriaResultImage.getId(), e);
			}
		}
		
		return images;
	}
}