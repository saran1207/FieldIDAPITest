package com.n4systems.fieldid.api.mobile.resources.assetattachment;

import com.n4systems.fieldid.api.mobile.exceptions.NotFoundException;
import com.n4systems.fieldid.api.mobile.resources.ApiResource;
import com.n4systems.fieldid.api.mobile.resources.asset.ApiAssetLink;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.Asset;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.asset.AssetAttachmentSaver;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.net.URL;
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
		AssetAttachment attachment = findAttachment(attachmentId);
		if (attachment == null) {
			throw new NotFoundException("Asset Attachment", attachmentId);
		}


        S3Service s3Service = ServiceLocator.getS3Service();
        File assetAttachmentFile = s3Service.downloadAssetAttachment(attachment);
        if (assetAttachmentFile == null || !assetAttachmentFile.exists()) {
            assetAttachmentFile = PathHandler.getAssetAttachmentFile(attachment);
            if (!assetAttachmentFile.exists()) {
                throw new NotFoundException("Attachment File", attachmentId);
            }
        }

        // TODO: Why is MimetypesFileTypeMap ignoring the locations it's supposed to look? META-INF/mime.types exists,
        // but entries are not being respected.. Remove these lines when we get that working.
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        mimetypesFileTypeMap.addMimeTypes("application/pdf       pdf PDF");
        mimetypesFileTypeMap.addMimeTypes("application/msword   doc DOC");
        mimetypesFileTypeMap.addMimeTypes("image/png   png PNG");
        mimetypesFileTypeMap.addMimeTypes("application/vnd.ms-excel   xls XLS");

        String mediaType = mimetypesFileTypeMap.getContentType(assetAttachmentFile);

		Response response = Response
				.ok(assetAttachmentFile, mediaType)
				//.header("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"")
				.build();
		return response;
	}

	private AssetAttachment findAttachment(String attachmentId) {
		QueryBuilder<AssetAttachment> query = createUserSecurityBuilder(AssetAttachment.class);
		query.addWhere(WhereClauseFactory.create("mobileId", attachmentId));
		return persistenceService.find(query);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveAssetAttachment(ApiAssetAttachment apiAttachment) {
		AssetAttachment attachment = convertApiModelToEntity(apiAttachment);
		AssetAttachmentSaver saver = new AssetAttachmentSaver(getCurrentUser(), attachment.getAsset());
		saver.saveOrUpdate(getEntityManager(), attachment);
	}
	
	@DELETE
	@Path("{attachmentId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void deleteAssetAttachment(@PathParam("attachmentId") String attachmentId) {
		QueryBuilder<AssetAttachment> query = createTenantSecurityBuilder(AssetAttachment.class, true);
		query.addWhere(WhereClauseFactory.create("mobileId", attachmentId));
		AssetAttachment attachment = persistenceService.find(query);
		if(attachment != null) {
			AssetAttachmentSaver saver = new AssetAttachmentSaver(getCurrentUser(), attachment.getAsset());
			saver.remove(attachment);
			logger.info("Deleted Attachment for Asset: " + attachment.getAsset().getIdentifier());
		} else {
			logger.warn("Failed Deleting Asset Attachment for mobileId: " + attachmentId);
		}
	}

	@PUT
	@Path("multi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void multiAddAsset(ApiMultiAssetAttachment multiAssetAttachment) {
		ApiAssetAttachment assetAttachmentTemplate = multiAssetAttachment.getAssetAttachmentTemplate();
		for (ApiAssetLink assetLink: multiAssetAttachment.getAttachments()) {
			assetAttachmentTemplate.setSid(assetLink.getSid());
			assetAttachmentTemplate.setAssetId(assetLink.getAssetId());

			logger.info("Creating Asset Attachment " + assetAttachmentTemplate.getSid());
			saveAssetAttachment(assetAttachmentTemplate);
		}

		logger.info("Saved " + multiAssetAttachment.getAttachments().size() + " Asset Attachments");
	}
	
	/*public List<ApiAssetAttachment> findAllAttachments(String assetId) {
		List<AssetAttachment> attachments = findAllAssetAttachments(assetId);
		List<ApiAssetAttachment> apiAttachments = convertAllEntitiesToApiModels(attachments);
		
		for (ApiAssetAttachment apiAttachment : apiAttachments) {
			// If attachment is not an image, remove the data. User has to get that data on fly.
			if(!apiAttachment.isImage()) {
				apiAttachment.setData(null);
			}
		}
		
		return apiAttachments;
	}*/

    public List<ApiAssetAttachment> convertAllAssetAttachments(List<AssetAttachment> assetAttachments) {
        List<ApiAssetAttachment> apiAttachments = convertAllEntitiesToApiModels(assetAttachments);

        for (ApiAssetAttachment apiAttachment : apiAttachments) {
            // If attachment is not an image, remove the data. User has to get that data on fly.
            /*if(!apiAttachment.isImage()) {
                apiAttachment.setData(null);
            }*/
            Assert.isNull(apiAttachment.getData());
        }

        return apiAttachments;
    }


    public List<ApiAssetAttachment> convertAllFileAttachments(List<FileAttachment> fileAttachments) {
        List<ApiAssetAttachment> apiAttachments = new ArrayList<ApiAssetAttachment>();
        for (FileAttachment fileAttachment: fileAttachments) {
            apiAttachments.add(convertEntityToApiModel(fileAttachment));
        }

        for (ApiAssetAttachment apiAttachment : apiAttachments) {
            // If attachment is not an image, remove the data. User has to get that data on fly.
            /*if(!apiAttachment.isImage()) {
                apiAttachment.setData(null);
            }*/
            Assert.isNull(apiAttachment.getData());
        }

        return apiAttachments;
    }
	
	/*public List<AssetAttachment> findAllAssetAttachments(String assetId) {
		QueryBuilder<AssetAttachment> builder = createUserSecurityBuilder(AssetAttachment.class);
		builder.addWhere(WhereClauseFactory.create("asset.mobileGUID", assetId));
		//return builder.getResultList(getEntityManager());
		return persistenceService.findAll(builder);
	}*/
	
	@Override
	protected ApiAssetAttachment convertEntityToApiModel(AssetAttachment attachment) {
		ApiAssetAttachment apiAttachment = new ApiAssetAttachment();
		apiAttachment.setSid(attachment.getMobileId());
		apiAttachment.setActive(true);
		apiAttachment.setModified(attachment.getModified());
		apiAttachment.setAssetId(attachment.getAsset().getMobileGUID());
		apiAttachment.setComments(attachment.getComments());
		apiAttachment.setFileName(attachment.getFileName().substring(attachment.getFileName().lastIndexOf('/') + 1));
        apiAttachment.setFilePath(attachment.getFileName());
		apiAttachment.setImage(attachment.isImage());
        apiAttachment.setUrl(getAttachmentUrl(attachment));
		return apiAttachment;
	}

    protected ApiAssetAttachment convertEntityToApiModel(FileAttachment attachment) {
        ApiAssetAttachment apiAttachment = new ApiAssetAttachment();
        apiAttachment.setSid(attachment.getMobileId());
        apiAttachment.setActive(true);
        apiAttachment.setModified(attachment.getModified());
        //apiAttachment.setAssetId(attachment.getAsset().getMobileGUID());
        apiAttachment.setComments(attachment.getComments());
        apiAttachment.setFileName(attachment.getFileName().substring(attachment.getFileName().lastIndexOf('/') + 1));
        apiAttachment.setFilePath(attachment.getFileName());
        apiAttachment.setImage(attachment.isImage());
        apiAttachment.setUrl(getAttachmentUrl(attachment));
        return apiAttachment;
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
		attachment.setFileName(apiAttachment.getFilePath() != null ? apiAttachment.getFilePath() : apiAttachment.getFileName());
		attachment.setData(apiAttachment.getData());
		return attachment;
	}

	public AssetAttachment convertApiModelToEntity(ApiAssetAttachment apiAttachment) {
		QueryBuilder<Asset> assetQuery = createTenantSecurityBuilder(Asset.class, true);
		assetQuery.addWhere(WhereClauseFactory.create("mobileGUID", apiAttachment.getAssetId()));
		Asset asset = persistenceService.find(assetQuery);
		if (asset == null) {
			throw new NotFoundException("Asset", apiAttachment.getAssetId());
		}
		return convertApiModelToEntity(apiAttachment, asset);
	}
	
	public AssetAttachment findExistingAttachment(String mobileId) {
		QueryBuilder<AssetAttachment> builder = createUserSecurityBuilder(AssetAttachment.class);
		builder.addWhere(WhereClauseFactory.create("mobileId", mobileId));
		List<AssetAttachment> attachments =  persistenceService.findAll(builder);
		
		return attachments.size() == 1
			? attachments.get(0)
			: null;
	}

    public void loadAttachmentData(ApiAssetAttachment apiAssetAttachment, Asset asset){
        AssetAttachment attachment = convertApiModelToEntity(apiAssetAttachment, asset);
        apiAssetAttachment.setData(loadAttachmentData(attachment));
    }
	
	private byte[] loadAttachmentData(AssetAttachment attachment) {
        S3Service s3Service = ServiceLocator.getS3Service();
		byte[] data = null;
        String fileName = attachment.getFileName().substring(attachment.getFileName().lastIndexOf('/') + 1);
        if(s3Service.assetAttachmentExists(attachment)){
            try {
                data = s3Service.downloadAssetAttachmentBytes(attachment);
            } catch(Exception e) {
                String assetAttachmentUrl = attachment.getFileName();
                logger.warn("Unable to load remote asset attachment at: " + assetAttachmentUrl, e);
            }
        }
        //check to see if a file attachment exists
        else if(s3Service.fileAttachmentExists(attachment.getMobileId(), fileName)){
            try {
                data = s3Service.downloadFileAttachmentBytes(attachment.getMobileId(), fileName);
            } catch(Exception e) {
                String assetAttachmentUrl = attachment.getFileName();
                logger.warn("Unable to load remote file attachment at: " + assetAttachmentUrl, e);
            }
        }

        if(data == null && attachment.getId() != null){
            File attachmentFile = PathHandler.getAssetAttachmentFile(attachment);
            if (attachmentFile.exists()) {
                try {
                    data = FileUtils.readFileToByteArray(attachmentFile);
                } catch(Exception e) {
                    logger.warn("Unable to load local asset attachment at: " + attachmentFile, e);
                }
            }
        }
		return data;
	}

    private URL getAttachmentUrl(AssetAttachment attachment){
        S3Service s3Service = ServiceLocator.getS3Service();
        URL url = null;
        if(s3Service.assetAttachmentExists(attachment)){
            url = s3Service.getAssetAttachmentUrl(attachment);
        }
        return url;
    }

    private URL getAttachmentUrl(FileAttachment attachment){
        S3Service s3Service = ServiceLocator.getS3Service();
        URL url = null;
        if(s3Service.fileAttachmentExists(attachment)){
            url = s3Service.getFileAttachmentUrl(attachment);
        }
        return url;
    }
}
