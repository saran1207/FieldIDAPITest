package com.n4systems.fieldid.ws.v1.resources.assetattachment;

import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.Asset;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("assetAttachment")
public class ApiAssetAttachmentResource extends ApiResource<ApiAssetAttachment, AssetAttachment> {
	private static Logger logger = Logger.getLogger(ApiAssetAttachmentResource.class);	
	
	@GET
	@Path("{attachmentId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Transactional(readOnly = true)
	public Response downloadAttachment(@PathParam("attachmentId") String attachmentId) {
        QueryBuilder<AssetAttachment> query = createUserSecurityBuilder(AssetAttachment.class);
        query.addWhere(WhereClauseFactory.create("mobileId", attachmentId));

		AssetAttachment attachment = persistenceService.find(query);
		if (attachment == null) {
			throw new NotFoundException("Asset Attachment", attachmentId);
		}
		
		File attachmentFile = PathHandler.getAssetAttachmentFile(attachment);
		if (!attachmentFile.exists()) {
			throw new NotFoundException("Attachment File", attachmentId);
		}

        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        mimetypesFileTypeMap.addMimeTypes("application/pdf       pdf PDF");

        String mediaType = mimetypesFileTypeMap.getContentType(attachmentFile);

		Response response = Response
				.ok(attachmentFile, mediaType)
				//.header("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"")
				.build();
		return response;
	}	
	
	public List<ApiAssetAttachment> findAllAttachments(String assetId) {
		List<AssetAttachment> attachments = findAllAssetAttachments(assetId);
		List<ApiAssetAttachment> apiAttachments = convertAllEntitiesToApiModels(attachments);
		
		for (ApiAssetAttachment apiAttachment : apiAttachments) {
			// If attachment is not an image, remove the data. User has to get that data on fly.
			if(!apiAttachment.isImage()) {
				apiAttachment.setData(null);
			}
		}
		
		return apiAttachments;
	}
	
	public List<AssetAttachment> findAllAssetAttachments(String assetId) {
		QueryBuilder<AssetAttachment> builder = createUserSecurityBuilder(AssetAttachment.class);
		builder.addWhere(WhereClauseFactory.create("asset.mobileGUID", assetId));
		//return builder.getResultList(getEntityManager());
		return persistenceService.findAll(builder);
	}
	
	@Override
	protected ApiAssetAttachment convertEntityToApiModel(AssetAttachment attachment) {
		ApiAssetAttachment apiAttachment = new ApiAssetAttachment();
		apiAttachment.setSid(attachment.getMobileId());
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
	
	public List<AssetAttachment> convertApiListToEntityList(List<ApiAssetAttachment> apiAttachments, Asset asset) {
		List<AssetAttachment> attachments = new ArrayList<AssetAttachment>();
		
		for(ApiAssetAttachment apiAttachment : apiAttachments) {
			AssetAttachment attachment = convertApiModelToEntity(apiAttachment, asset);
			attachments.add(attachment);
		}
		
		return attachments;
	}
	
	public AssetAttachment convertApiModelToEntity(ApiAssetAttachment apiAttachment, Asset asset) {		
		AssetAttachment attachment = findExistingAttachment(apiAttachment.getSid());
		
		if(attachment == null) {
			attachment = new AssetAttachment();		
			attachment.setMobileId(apiAttachment.getSid());
			attachment.setAsset(asset);
			attachment.setTenant(asset.getTenant());
		}
		
		attachment.setComments(apiAttachment.getComments());
		attachment.setFileName(apiAttachment.getFileName());
		
		try {
			File attachmentPath = new File(PathHandler.getTempRoot(), attachment.getFileName());
			FileUtils.writeByteArrayToFile(attachmentPath, apiAttachment.getData());
		} catch (IOException e) {
			logger.error("Error writing Asset Attachment", e);
		}
		
		return attachment;
	}
	
	public AssetAttachment findExistingAttachment(String mobileId) {
		QueryBuilder<AssetAttachment> builder = createUserSecurityBuilder(AssetAttachment.class);
		builder.addWhere(WhereClauseFactory.create("mobileId", mobileId));
		List<AssetAttachment> attachments =  persistenceService.findAll(builder);
		
		return attachments.size() == 1
			? attachments.get(0)
			: null;
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
