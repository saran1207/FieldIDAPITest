package com.n4systems.services.asset;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.fieldid.LegacyMethod;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.asset.AssetAttachmentListLoader;
import com.n4systems.model.asset.AssetAttachmentSaver;
import com.n4systems.reporting.PathHandler;

/**
 * 
 * This class is currently used by new web services (IOS+) to save assets.
 * 1. Sets asset imageName field.
 * 2. Saves asset.
 * 3. Saves asset attachments.
 * 4. Saves asset image.
 * If we are using this in web site or elsewhere, it may need few fixes. E.g.: mobileGuid needs to be set.
 * This class was created because mixing Spring and Legacy services caused MySQL locks.
 * This class also uses some legacy methods.
 * 
 */
@Transactional
public class AssetSaveServiceSpring extends FieldIdPersistenceService {
	
	private static Logger logger = Logger.getLogger(AssetSaveServiceSpring.class);

	@Autowired
	private AssetService assetService;
	
	@LegacyMethod
	public Asset create(Asset asset, List<AssetAttachment> uploadedAttachments, byte[] imageData) {
		try {
			setAssetImageName(asset, imageData != null);
			asset = assetService.createWithHistory(asset, getCurrentUser());
			saveUploadedAttachments(asset, uploadedAttachments);
			saveAssetImage(asset, imageData);
			return asset;
		} catch (SubAssetUniquenessException e) {
			throw new ProcessFailureException("could not save asset", e);
		}
	}

	@LegacyMethod
	public Asset update(Asset asset, List<AssetAttachment> existingAttachments, List<AssetAttachment> uploadedAttachments, byte[] imageData) {
		try {
			setAssetImageName(asset, imageData != null);
			asset = assetService.update(asset, getCurrentUser());
			updateExistingAttachments(asset, existingAttachments);
			saveUploadedAttachments(asset, uploadedAttachments);
			saveAssetImage(asset, imageData);
			return asset;
		} catch (SubAssetUniquenessException e) {
			throw new ProcessFailureException("could not save asset", e);
		}
	}

	@LegacyMethod
	private void saveUploadedAttachments(Asset asset, List<AssetAttachment> uploadedAttachments) {
		if (uploadedAttachments != null) {
			AssetAttachmentSaver saver = new AssetAttachmentSaver(getCurrentUser(), asset);
			for (AssetAttachment attachment : uploadedAttachments) {
				saver.save(getEntityManager(), attachment);
			}
		}
	}

	public void updateExistingAttachments(Asset asset, List<AssetAttachment> existingAttachments) {
		if (existingAttachments != null) {
			reattachAttachments(asset, existingAttachments);
			updateAttachments(asset, existingAttachments);
			removeAttachments(asset, existingAttachments);
		}
	}

	private void reattachAttachments(Asset asset, List<AssetAttachment> existingAttachments) {
		List<AssetAttachment> loadedAttachments = new AssetAttachmentListLoader(securityContext.getUserSecurityFilter()).setAsset(asset).load(getEntityManager());
		for (AssetAttachment loadedAttachment : loadedAttachments) {
			if (existingAttachments.contains(loadedAttachment)) {
				AssetAttachment existingAttachment = existingAttachments.get(existingAttachments.indexOf(loadedAttachment));
				existingAttachment.setCreated(loadedAttachment.getCreated());
			}
		}
	}
	
	@LegacyMethod
	private void updateAttachments(Asset asset, List<AssetAttachment> existingAttachments) {
		AssetAttachmentSaver saver = new AssetAttachmentSaver(getCurrentUser(), asset);
		for (AssetAttachment attachment : existingAttachments) {
			saver.update(getEntityManager(), attachment);
		}
	}
	
	private void removeAttachments(Asset asset, List<AssetAttachment> existingAttachments) {
		List<AssetAttachment> loadedAttachments = new AssetAttachmentListLoader(securityContext.getUserSecurityFilter()).setAsset(asset).load();
		AssetAttachmentSaver deleter = new AssetAttachmentSaver(asset);
		for (AssetAttachment loadedAttachment : loadedAttachments) {
			if (!existingAttachments.contains(loadedAttachment)) {
				try {
					deleter.remove(loadedAttachment);
				} catch (EntityStillReferencedException e) {
					logger.error("Could not delete attachment", e);
				}
			}
		}
	}
	
	private void setAssetImageName(Asset asset, boolean hasImage) {
		String imageName = hasImage ? ("asset-" + asset.getMobileGUID() + ".jpg") : null;
		asset.setImageName(imageName);
	}
	
	private void saveAssetImage(Asset asset, byte[] imageData) {
		File assetImageFile = PathHandler.getAssetImageFile(asset);
		
		if(imageData != null) {
			logger.info("Writing Asset Image " + asset.getIdentifier());
			try {
				
				FileUtils.writeByteArrayToFile(assetImageFile, imageData);
			} catch (IOException e) {
				logger.error("Error copying Asset Image", e);
			}
		} else {
			//Remove the file if it exists.
			if(assetImageFile.exists()) {
				assetImageFile.delete();
			}
		}
	}
}
