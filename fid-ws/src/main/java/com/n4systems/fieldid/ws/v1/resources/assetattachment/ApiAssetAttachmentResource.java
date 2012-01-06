package com.n4systems.fieldid.ws.v1.resources.assetattachment;

import java.io.File;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
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
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public ListResponse<ApiAssetAttachment> findAttachments(@QueryParam("assetId") String assetId) {
		QueryBuilder<AssetAttachment> builder = createUserSecurityBuilder(AssetAttachment.class);
		builder.addWhere(WhereClauseFactory.create("asset.mobileGUID", assetId));
		
		List<AssetAttachment> attachments = persistenceService.findAll(builder);
		List<ApiAssetAttachment> apiAttachments = convertAllEntitiesToApiModels(attachments);
		
		ListResponse<ApiAssetAttachment> response = new ListResponse<ApiAssetAttachment>(apiAttachments, 0, 0, apiAttachments.size());
		return response;
	}
	
	@GET
	@Path("{attachmentId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Transactional(readOnly = true)
	public Response downloadAttachment(@PathParam("attachmentId") Long attachmentId) {
		AssetAttachment attachment = persistenceService.find(AssetAttachment.class, attachmentId);
		if (attachment == null) {
			throw new NotFoundException("Asset Attachment", attachmentId);
		}
		
		File attachmentFile = PathHandler.getAssetAttachmentFile(attachment);
		if (!attachmentFile.exists()) {
			throw new NotFoundException("Attachment File", attachmentId);
		}
		
		String mediaType = new MimetypesFileTypeMap().getContentType(attachmentFile);
		
		Response response = Response
				.ok(attachmentFile, mediaType)
				.header("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"")
				.build();
		return response;
	}
	
	@Override
	protected ApiAssetAttachment convertEntityToApiModel(AssetAttachment attachment) {
		ApiAssetAttachment apiAttachment = new ApiAssetAttachment();
		apiAttachment.setSid(attachment.getId());
		apiAttachment.setActive(true);
		apiAttachment.setModified(attachment.getModified());
		apiAttachment.setAssetId(attachment.getAsset().getMobileGUID());
		apiAttachment.setComments(attachment.getComments());
		apiAttachment.setFileName(attachment.getFileName());
		apiAttachment.setImage(attachment.isImage());
		if (attachment.isImage()) {
			apiAttachment.setData(loadAttachmentData(attachment));
		}
		return apiAttachment;
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
