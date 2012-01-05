package com.n4systems.fieldid.ws.v1.resources.assetattachment;

import java.io.File;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.model.ListResponse;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

@Path("assetAttachment")
public class ApiAssetAttachmentResource extends ApiResource<ApiAssetAttachment, AssetAttachment> {
	private static Logger logger = Logger.getLogger(ApiAssetAttachmentResource.class);
	
	@GET
	@Path("{assetId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiAssetAttachment> findAttachments(@PathParam("assetId") String assetId) {
		QueryBuilder<AssetAttachment> builder = createUserSecurityBuilder(AssetAttachment.class);
		builder.addWhere(WhereClauseFactory.create("asset.mobileGUID", assetId));
		
		List<AssetAttachment> attachments = persistenceService.findAll(builder);
		List<ApiAssetAttachment> apiAttachments = convertAllEntitiesToApiModels(attachments);
		
		ListResponse<ApiAssetAttachment> response = new ListResponse<ApiAssetAttachment>(apiAttachments, 0, 0, apiAttachments.size());
		return response;
	}
	
	@Override
	protected ApiAssetAttachment convertEntityToApiModel(AssetAttachment attachment) {
		ApiAssetAttachment assetAttachment = new ApiAssetAttachment();
		assetAttachment.setAssetId(attachment.getAsset().getMobileGUID());
		assetAttachment.setComments(attachment.getComments());
		assetAttachment.setFileName(attachment.getFileName());
		assetAttachment.setImage(attachment.isImage());
		assetAttachment.setData(loadAttachmentData(attachment));
		return assetAttachment;
	}
	
	private byte[] loadAttachmentData(AssetAttachment attachment) {
		byte[] data = null;
		File attachmentFile = PathHandler.getAssetAttachmentFile(attachment);
		if (attachmentFile.exists()) {
			try {
				data = FileUtils.readFileToByteArray(attachmentFile);
			} catch(Exception e) {
				logger.warn("Unable to load asset attachment at: " + attachmentFile, e);
			}
		}
		return data;
	}

}
