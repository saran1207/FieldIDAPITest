package com.n4systems.fieldid.ws.v2.resources.assettypeattachment;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;
import com.n4systems.fieldid.ws.v2.resources.ApiResource;
import com.n4systems.fieldid.ws.v2.resources.assetattachment.ApiAssetAttachment;
import com.n4systems.model.AssetType;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.util.persistence.JoinClause;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.commons.collections.ListUtils;
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
@Path("assetTypeAttachment")
public class ApiAssetTypeAttachmentResource extends ApiResource<ApiAssetTypeAttachment, FileAttachment> {

	@Autowired
	private S3Service s3Service;

	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> findAllForAsset(@QueryParam("assetTypeIds") List<String> assetTypeIds, @QueryParam("id") List<String> attachmentIds) {
		QueryBuilder<ApiModelHeader> queryBuilder = new QueryBuilder<>(AssetType.class, securityContext.getUserSecurityFilter());
		queryBuilder.setSelectArgument(new NewObjectSelect(ApiModelHeader.class, "mobileId", "modified"));

//		queryBuilder.addJoin(new JoinClause(JoinClause.JoinType.INNER, ))
//
//		if (attachmentIds != null && attachmentIds.size() > 0) {
//			queryBuilder.addWhere(WhereClauseFactory.create(Comparator.IN, "mobileId", attachmentIds));
//		} else {
//			queryBuilder.addWhere(WhereClauseFactory.create(Comparator.IN, "asset.mobileGUID", assetIds));
//		}
//
//
//		List<AssetAttachment> assetAttachments = assetService.findAssetAttachments(asset);
//
//		List<FileAttachment> typeAttachments = asset.getType().getAttachments();
//		List<ApiAssetAttachment> apiAssetAttachments = apiAttachmentResource.convertAllAssetAttachments(assetAttachments);
//		List<ApiAssetAttachment> apiFileAttachments = apiAttachmentResource.convertAllFileAttachments(typeAttachments);
//		apiAsset.setAttachments(ListUtils.union(apiAssetAttachments, apiFileAttachments));

		return persistenceService.findAll(queryBuilder);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiAssetTypeAttachment> findAllForAsset(@QueryParam("id") List<String> attachmentIds) {
		QueryBuilder<FileAttachment> queryBuilder = createUserSecurityBuilder(FileAttachment.class);
		queryBuilder.addWhere(WhereClauseFactory.create(Comparator.IN, "mobileId", attachmentIds));
		List<ApiAssetTypeAttachment> apiAttachment = convertAllEntitiesToApiModels(persistenceService.findAll(queryBuilder));
		return apiAttachment;
	}

	@Override
	protected ApiAssetTypeAttachment convertEntityToApiModel(FileAttachment attachment) {
		String mimeType = s3Service.getFileAttachmentContentType(attachment);
		if (mimeType == null) return null;

		ApiAssetTypeAttachment apiAttachment = new ApiAssetTypeAttachment();
		apiAttachment.setSid(attachment.getMobileId());
		apiAttachment.setActive(true);
		apiAttachment.setModified(attachment.getModified());
//		apiAttachment.setAssetTypeId(attachment.getAsset().getMobileGUID());
		apiAttachment.setComments(attachment.getComments());
		apiAttachment.setUrl(s3Service.getFileAttachmentUrl(attachment));
		apiAttachment.setMimeType(mimeType);
		return apiAttachment;
	}
}

