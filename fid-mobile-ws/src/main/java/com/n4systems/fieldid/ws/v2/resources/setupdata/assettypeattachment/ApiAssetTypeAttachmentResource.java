package com.n4systems.fieldid.ws.v2.resources.setupdata.assettypeattachment;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadWrite;
import com.n4systems.model.AssetType;
import com.n4systems.model.FileAttachment;
import com.n4systems.util.persistence.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.List;

@Component
@Path("assetTypeAttachment")
public class ApiAssetTypeAttachmentResource extends SetupDataResourceReadWrite<ApiAssetTypeAttachment, FileAttachment> {
	private static final Logger logger = Logger.getLogger(ApiAssetTypeAttachmentResource.class);

	@Autowired
	private S3Service s3Service;

	public ApiAssetTypeAttachmentResource() {
		super("mobileId", FileAttachment.class, false);
	}

	@Override
	protected void addTermsToLatestQuery(QueryBuilder<?> query) {
		query.addWhere(new SubSelectInClause("id",
						createUserSecurityBuilder(AssetType.class, true)
								.setSelectArgument(new SimpleSelect("att.id", true))
								.addJoin(new JoinClause(JoinClause.JoinType.INNER, "attachments", "att", false)))
		);
	}

	@Override
	protected List<ApiAssetTypeAttachment> postConvertAllEntitiesToApiModels(List<ApiAssetTypeAttachment> apiModels) {
		apiModels.forEach(apiAtt -> apiAtt.setAssetTypeId(findAssetTypeIdForAttachment(apiAtt.getSid())));
		return apiModels;
	}

	private Long findAssetTypeIdForAttachment(String mobileId) {
		QueryBuilder<AssetType> query = createUserSecurityBuilder(AssetType.class, true);
		query.addJoin(new JoinClause(JoinClause.JoinType.INNER, "attachments", "att", false));
		query.addWhere(WhereClauseFactory.createPassthru("att.mobileId = :attachment", mobileId));
		AssetType assetType = persistenceService.find(query);
		return (assetType != null) ? assetType.getId() : null;
	}

	@Override
	protected ApiAssetTypeAttachment convertEntityToApiModel(FileAttachment attachment) {
		try {
			ApiAssetTypeAttachment apiAttachment = new ApiAssetTypeAttachment();
			apiAttachment.setSid(attachment.getMobileId());
			apiAttachment.setActive(true);
			apiAttachment.setModified(attachment.getModified());
			apiAttachment.setComments(attachment.getComments());
			apiAttachment.setUrl(s3Service.getFileAttachmentUrl(attachment));
			apiAttachment.setMimeType(s3Service.getFileAttachmentContentType(attachment));
			return apiAttachment;
		} catch (AmazonS3Exception ex) {
			logger.warn("Unable to load asset type attachment information", ex);
			return null;
		}
	}
}

