package com.n4systems.model.asset;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.Asset;
import com.n4systems.model.user.User;
import com.n4systems.persistence.savers.ModifiedBySaver;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ServiceLocator;
import org.apache.commons.io.FileUtils;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;

public class AssetAttachmentSaver extends ModifiedBySaver<AssetAttachment> {
	private final Asset asset;
    private S3Service s3Service;
	
	public AssetAttachmentSaver(Asset asset) {
        this((User)null, asset, ServiceLocator.getS3Service());
	}

    public AssetAttachmentSaver(Asset asset, S3Service s3Service) {
        this((User)null, asset, s3Service);
    }

	public AssetAttachmentSaver(User modifiedBy, Asset asset) {
        this(modifiedBy, asset, ServiceLocator.getS3Service());
	}

    public AssetAttachmentSaver(User modifiedBy, Asset asset, S3Service s3Service) {
        super(modifiedBy);
        this.asset = asset;
        this.s3Service = s3Service;
    }

	public void saveOrUpdate(EntityManager em, AssetAttachment entity) {
		if (entity.isNew()) {
			save(em, entity);
		} else {
			update(em, entity);
		}
	}

	@Override
	public void save(EntityManager em, AssetAttachment entity) {
		fillInConnectionFields(entity);

		// this must be captured prior to merge as data is a transient field
		byte[] attachmentData = new byte[0];
        if(!entity.isRemote()){
            attachmentData = entity.getData();
        }

        entity = em.merge(entity);

        saveAttachmentData(entity, attachmentData);
	}
	
	@Override
	public AssetAttachment update(EntityManager em, AssetAttachment entity) {
		fillInConnectionFields(entity);
        byte[] attachmentData = new byte[0];
        if(!entity.isRemote()){
            attachmentData = entity.getData();
        }

		AssetAttachment attachment =  em.merge(entity);

        if(attachmentData != null && attachmentData.length > 0){
            writeAttachmentDataToFileSystem(attachment, attachmentData);
        }

		return attachment;
	}

	@Override
	public void remove(EntityManager em, AssetAttachment entity) {
		if (entity == null || entity.isNew()) {
			throw new InvalidArgumentException("you need an attachment that has been persisted.");
		}
		
		em.remove(entity);
		deleteFile(entity);
	}

	private void saveAttachmentData(AssetAttachment attachment, byte[] attachmentData) {
        if (attachmentData != null) {
            writeAttachmentDataToFileSystem(attachment, attachmentData);
        } else {
            moveAttachmentFromTempDir(attachment);
        }
	}

	private void moveAttachmentFromTempDir(AssetAttachment attachment) {
        //if attachment is already on S3, then we don't need to copy it there
        if(!s3Service.assetAttachmentExists(attachment)){
            try {
                File tmpDirectory = PathHandler.getTempRoot();

                File tmpAttachment = new File(tmpDirectory, attachment.getFileName());
                Assert.hasLength(attachment.getMobileId());
                s3Service.uploadAssetAttachment(tmpAttachment, attachment);
                attachment.setFileName(s3Service.getAssetAttachmentPath(attachment));
            } catch (Exception e) {
                throw new FileAttachmentException("Failed to upload attachment data [" + attachment.getFileName() + "]",e);
            }
        }
	}

	private void writeAttachmentDataToFileSystem(AssetAttachment attachment, byte[] attachmentData) {
        //if attachment is already on S3, then we don't need to copy it there
        if(!s3Service.assetAttachmentExists(attachment)){
            try {
                //determine whether its local file or remote (if file does not exist, then it should be on s3)
                File attachmentFile = PathHandler.getAssetAttachmentFile(attachment);
                if(attachmentFile.exists()){
                    FileUtils.writeByteArrayToFile(attachmentFile, attachmentData);
                }
                else {
                    Assert.hasLength(attachment.getMobileId());
                    s3Service.uploadAssetAttachmentData(attachmentData, attachment);
                    attachment.setFileName(s3Service.getAssetAttachmentPath(attachment));
                }
            } catch (IOException e) {
                throw new FileAttachmentException(e);
            }
        }
	}

	private void fillInConnectionFields(AssetAttachment entity) {
		entity.setAsset(asset);
		entity.setTenant(asset.getTenant());
	}
	
	private void deleteFile(AssetAttachment attachment) {
		File attachedFile = PathHandler.getAssetAttachmentFile(attachment);
		
		if (attachedFile.exists()) {
			attachedFile.delete();
		}
		
	}
}
