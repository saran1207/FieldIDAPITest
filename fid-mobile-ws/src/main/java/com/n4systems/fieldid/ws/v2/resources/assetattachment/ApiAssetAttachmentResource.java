package com.n4systems.fieldid.ws.v2.resources.assetattachment;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;
import com.n4systems.fieldid.ws.v2.resources.ApiResource;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
@Path("assetAttachment")
public class ApiAssetAttachmentResource extends ApiResource<ApiAssetAttachment, AssetAttachment> {

	@Autowired
	private S3Service s3Service;

	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> findAllForAsset(@QueryParam("assetId") List<String> assetIds, @QueryParam("id") List<String> attachmentIds) {
		QueryBuilder<ApiModelHeader> queryBuilder = new QueryBuilder<>(AssetAttachment.class, securityContext.getUserSecurityFilter());
		queryBuilder.setSelectArgument(new NewObjectSelect(ApiModelHeader.class, "mobileId", "modified"));

		if (attachmentIds != null && attachmentIds.size() > 0) {
			queryBuilder.addWhere(WhereClauseFactory.create(Comparator.IN, "mobileId", attachmentIds));
		} else {
			queryBuilder.addWhere(WhereClauseFactory.create(Comparator.IN, "asset.mobileGUID", assetIds));
		}

		return persistenceService.findAll(queryBuilder);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiAssetAttachment> findAllForAsset(@QueryParam("id") List<String> attachmentIds) {
		QueryBuilder<AssetAttachment> queryBuilder = createUserSecurityBuilder(AssetAttachment.class);
		queryBuilder.addWhere(WhereClauseFactory.create(Comparator.IN, "mobileId", attachmentIds));
		List<ApiAssetAttachment> apiAttachment = convertAllEntitiesToApiModels(persistenceService.findAll(queryBuilder));
		return apiAttachment;
	}
	
	@Override
	protected ApiAssetAttachment convertEntityToApiModel(AssetAttachment attachment) {
		String mimeType = s3Service.getAssetAttachmentContentType(attachment);
		if (mimeType == null) return null;

		ApiAssetAttachment apiAttachment = new ApiAssetAttachment();
		apiAttachment.setSid(attachment.getMobileId());
		apiAttachment.setActive(true);
		apiAttachment.setModified(attachment.getModified());
		apiAttachment.setAssetId(attachment.getAsset().getMobileGUID());
		apiAttachment.setComments(attachment.getComments());
        apiAttachment.setUrl(s3Service.getAssetAttachmentUrl(attachment));
		apiAttachment.setMimeType(mimeType);
		return apiAttachment;
	}

}

