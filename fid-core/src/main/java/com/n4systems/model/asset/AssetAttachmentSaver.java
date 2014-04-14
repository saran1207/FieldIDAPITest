package com.n4systems.model.asset;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Asset;
import com.n4systems.model.user.User;
import com.n4systems.persistence.savers.ModifiedBySaver;
//arezafar: import com.n4systems.reporting.PathHandler;
import org.apache.commons.io.FileUtils;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;

public class AssetAttachmentSaver extends ModifiedBySaver<AssetAttachment> {
	private final Asset asset;
	
	public AssetAttachmentSaver(Asset asset) {
		super();
		this.asset = asset;
	}

	public AssetAttachmentSaver(User modifiedBy, Asset asset) {
		super(modifiedBy);
		this.asset = asset;
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
		//arezafar: byte[] attachmentData = entity.getData();
        entity = em.merge(entity);
        assert(entity.isRemote());
        //arezafar: saveAttachmentData(entity, attachmentData);
	}
	
	@Override
	public AssetAttachment update(EntityManager em, AssetAttachment entity) {
		fillInConnectionFields(entity);
        //arezafar: byte[] attachmentData = entity.getData();
		AssetAttachment attachment =  em.merge(entity);
        assert(entity.isRemote());
        /*arezafar: if (attachmentData != null) {
			writeAttachmentDataToFileSystem(attachment, attachmentData);
		} */
		return attachment;
	}

	@Override
	public void remove(EntityManager em, AssetAttachment entity) {
		if (entity == null || entity.isNew()) {
			throw new InvalidArgumentException("you need an attachment that has been persisted.");
		}
		
		em.remove(entity);
		//arezafar: deleteFile(entity);
	}

	/*arezafar: private void saveAttachmentData(AssetAttachment attachment, byte[] attachmentData) {
		if (attachmentData != null) {
			writeAttachmentDataToFileSystem(attachment, attachmentData);
		} else {
			moveAttachmentFromTempDir(attachment);
		}
	}*/

	/*arezafar: private void moveAttachmentFromTempDir(AssetAttachment entity) {
		try {
			File attachmentDir = PathHandler.getAssetAttachmentDir(entity);
			File tmpDirectory = PathHandler.getTempRoot();

			File tmpAttachment = new File(tmpDirectory, entity.getFileName());
			FileUtils.copyFileToDirectory(tmpAttachment, attachmentDir);
			entity.setFileName(tmpAttachment.getName());
		} catch (IOException e) {
			throw new FileAttachmentException(e);
		}
	}*/

	/*arezafar: private void writeAttachmentDataToFileSystem(AssetAttachment entity, byte[] attachmentData) {
		try {
			File attachmentFile = PathHandler.getAssetAttachmentFile(entity);
			FileUtils.writeByteArrayToFile(attachmentFile, attachmentData);
		} catch (IOException e) {
			throw new FileAttachmentException(e);
		}
	}*/

	private void fillInConnectionFields(AssetAttachment entity) {
		entity.setAsset(asset);
		entity.setTenant(asset.getTenant());
	}
	
	//TODO move to a file Deleter.
	/*arezafar: private void deleteFile(AssetAttachment attachment) {
		File attachedFile = PathHandler.getAssetAttachmentFile(attachment);
		
		if (attachedFile.exists()) {
			attachedFile.delete();
		}
		
	}*/
}
