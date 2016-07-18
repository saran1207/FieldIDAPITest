package com.n4systems.fieldid.ws.v1.resources.event.criteria;


import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.FileTypeMap;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("criteriaImage")
public class ApiCriteriaImagesResource extends FieldIdPersistenceService {
	private static Logger logger = Logger.getLogger(ApiCriteriaImagesResource.class);
	@Autowired private S3Service s3Service;

	@DELETE
	@Path("{sid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void deleteCriteriaImage(@PathParam("sid") String sid) {
		QueryBuilder<CriteriaResultImage> builder = createTenantSecurityBuilder(CriteriaResultImage.class, true);
		builder.addWhere(WhereClauseFactory.create("mobileGUID", sid));
		CriteriaResultImage criteriaResultImage = persistenceService.find(builder);

		//Remove it from S3
		s3Service.deleteCriteriaResultImage(criteriaResultImage);

		//Remove it's link from the Criteria Result Object
		CriteriaResult result = criteriaResultImage.getCriteriaResult();
		result.getCriteriaImages().remove(criteriaResultImage);
		persistenceService.update(result);

		//Remove it completely
		persistenceService.remove(criteriaResultImage);

		logger.info("Criteria Image for CriteriaResult: " + criteriaResultImage.getCriteriaResult().getID() + " has been deleted.");
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveCriteriaImage(ApiCriteriaImage apiCriteriaImage) {
		QueryBuilder<CriteriaResult> builder = createTenantSecurityBuilder(CriteriaResult.class, true);
		builder.addWhere(WhereClauseFactory.create("mobileId", apiCriteriaImage.getCriteriaResultSid()));

        String imageMd5sum = DigestUtils.md5Hex(apiCriteriaImage.getImage());

		CriteriaResult criteriaResult = persistenceService.find(builder);

        for (CriteriaResultImage criteriaResultImage : criteriaResult.getCriteriaImages()) {
            if (imageMd5sum.equals(criteriaResultImage.getMd5sum())) {
                logger.warn("Duplicate criteria image detected: " + apiCriteriaImage.getCriteriaResultSid());
                return;
            }
        }

		CriteriaResultImage criteriaResultImage = new CriteriaResultImage();
		criteriaResultImage.setCriteriaResult(criteriaResult);
        criteriaResultImage.setMd5sum(imageMd5sum);
		criteriaResultImage.setFileName(apiCriteriaImage.getFileName());
		criteriaResultImage.setContentType(FileTypeMap.getDefaultFileTypeMap().getContentType(apiCriteriaImage.getFileName()));
		criteriaResultImage.setComments(apiCriteriaImage.getComments());
		criteriaResult.getCriteriaImages().add(criteriaResultImage);		

		persistenceService.update(criteriaResult);
		s3Service.uploadCriteriaResultImage(criteriaResultImage, apiCriteriaImage.getImage());
		
		logger.info("Saved Criteria Image for CriteriaResult: " + apiCriteriaImage.getCriteriaResultSid());
	}

	@PUT
	@Path("multi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveMultiAddEventCriteriaImage(ApiMultiEventCriteriaImage multiEventCriteriaImage) {
		ApiCriteriaImage apiCriteriaImage = multiEventCriteriaImage.getCriteriaImageTemplate();
		for(String criteriaResultId : multiEventCriteriaImage.getCriteriaResultIds()) {
			apiCriteriaImage.setCriteriaResultSid(criteriaResultId);
			saveCriteriaImage(apiCriteriaImage);
		}
		logger.info("Saved Multi Event Attachment for Events: " + multiEventCriteriaImage.getCriteriaResultIds().size());
	}
}
