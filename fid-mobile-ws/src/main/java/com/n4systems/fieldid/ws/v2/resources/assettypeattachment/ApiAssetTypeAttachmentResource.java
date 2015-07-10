package com.n4systems.fieldid.ws.v2.resources.assettypeattachment;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.ws.v2.resources.ApiModelHeader;
import com.n4systems.fieldid.ws.v2.resources.ApiResource;
import com.n4systems.model.AssetType;
import com.n4systems.model.FileAttachment;
import com.n4systems.util.persistence.JoinClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("assetTypeAttachment")
public class ApiAssetTypeAttachmentResource extends ApiResource<ApiAssetTypeAttachment, FileAttachment> {
	private static final Logger logger = Logger.getLogger(ApiAssetTypeAttachmentResource.class);

	@Autowired
	private S3Service s3Service;

	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> query(@QueryParam("id") List<String> attachmentIds) {
		if (attachmentIds.isEmpty()) return new ArrayList<>();
		List<ApiModelHeader> headers = convertAllEntitiesToApiModels(findAttachmentsByMobileIds(attachmentIds), a -> new ApiModelHeader(a.getMobileId(), a.getModified()));
		return headers;
	}

	@GET
	@Path("query/assetType")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiModelHeader> queryAssetType(@QueryParam("assetTypeIds") List<Long> assetTypeIds) {
		if (assetTypeIds.isEmpty()) return new ArrayList<>();
		List<ApiModelHeader> headers = convertAllEntitiesToApiModels(findAttachmentsByAssetTypes(assetTypeIds), a -> new ApiModelHeader(a.getMobileId(), a.getModified()));
		return headers;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<ApiAssetTypeAttachment> findAll(@QueryParam("id") List<String> attachmentIds) {
		if (attachmentIds.isEmpty()) return new ArrayList<>();

		List<FileAttachment> attachments = findAttachmentsByMobileIds(attachmentIds);
		List<ApiAssetTypeAttachment> apiAttachments = convertAllEntitiesToApiModels(attachments, att -> {
			ApiAssetTypeAttachment apiAtt = convertEntityToApiModel(att);
			if (apiAtt == null) return null;

			apiAtt.setAssetTypeId(findAssetTypeIdForAttachment(att));
			return apiAtt;
		});
		return apiAttachments;
	}

	private List<FileAttachment> findAttachmentsByAssetTypes(List<Long> assetTypeIds) {
		QueryBuilder<AssetType> query = createUserSecurityBuilder(AssetType.class, true).addWhere(WhereClauseFactory.create(Comparator.IN, "id", assetTypeIds));
		List<FileAttachment> attachments = persistenceService.findAll(query)
				.stream()
				.map(AssetType::getAttachments)
				.flatMap(List::stream)
				.collect(Collectors.toList());
		return attachments;
	}

	private List<FileAttachment> findAttachmentsByMobileIds(List<String> ids) {
		QueryBuilder<FileAttachment> query = createUserSecurityBuilder(FileAttachment.class).addWhere(WhereClauseFactory.create(Comparator.IN, "mobileId", ids));
		List<FileAttachment> attachments = persistenceService.findAll(query);
		return attachments;
	}

	private Long findAssetTypeIdForAttachment(FileAttachment attachment) {
		QueryBuilder<AssetType> query = createUserSecurityBuilder(AssetType.class, true);
		query.addJoin(new JoinClause(JoinClause.JoinType.INNER, "attachments", "att", false));
		query.addWhere(WhereClauseFactory.createPassthru("att = :attachment", attachment));
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

