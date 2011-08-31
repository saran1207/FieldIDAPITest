package com.n4systems.services.asset;

import java.util.List;

import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.model.Asset;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.asset.AssetAttachmentListLoader;
import com.n4systems.model.asset.AssetAttachmentSaver;
import com.n4systems.model.asset.AssetImageFileSaver;

import org.apache.log4j.Logger;


import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;

public class AssetSaveService {
	private static Logger logger = Logger.getLogger(AssetSaveService.class);
	private final LegacyAsset assetManager;
	private final User user;
	private SecurityFilter filter;
	private Asset asset;
	private List<AssetAttachment> existingAttachments;
	private List<AssetAttachment> uploadedAttachments;
	private boolean newAssetImage;
	private boolean removeAssetImage;
	private String newAssetImageName;

	public AssetSaveService(LegacyAsset assetManager, User user) {
		super();
		this.assetManager = assetManager;
		this.user = user;
		filter = new UserSecurityFilter(user);
		
	}

	public Asset create() {
		return create(true);
	}
	
	public Asset createWithoutHistory() {
		return create(false);
	}
	
	private Asset create(boolean withHistory) {
		try {
			createAsset(withHistory);
			saveUploadedAttachments();
			saveAssetImage();
			return asset;
		} catch (SubAssetUniquenessException e) {
			throw new ProcessFailureException("could not save asset", e);
		}
	}

	public Asset update() {
		try {
			updateAsset();
			updateExistingAttachments();
			saveUploadedAttachments();
			saveAssetImage();
			return asset;
		} catch (SubAssetUniquenessException e) {
			throw new ProcessFailureException("could not save asset", e);
		}
	}
	
	public void clear() {
		asset = null;
		existingAttachments = null;
		uploadedAttachments = null;
	}

	private void createAsset(boolean withHistory) throws SubAssetUniquenessException {
		saveRequirements();
		
		if (withHistory) {
			asset = assetManager.createWithHistory(asset, user);
		} else {
			asset = assetManager.create(asset, user);
		}
	}

	private void updateAsset() throws SubAssetUniquenessException {
		saveRequirements();
		asset = assetManager.update(asset, user);
	}

	private void saveRequirements() {
		if (asset == null) {
			throw new InvalidArgumentException("asset must defined.");
		}
		saveAssetImageName();
	}

	private void saveUploadedAttachments() {
		if (uploadedAttachments != null) {
			AssetAttachmentSaver saver = new AssetAttachmentSaver(user, asset);
			for (AssetAttachment attachment : uploadedAttachments) {
				saver.save(attachment);
			}
		}
	}


	public void updateExistingAttachments() {
		if (existingAttachments != null) {
			reattachAttachments();
			updateAttachments();
			removeAttachments();
		}
	}

	private void reattachAttachments() {
		List<AssetAttachment> loadedAttachments = new AssetAttachmentListLoader(filter).setAsset(asset).load();
		for (AssetAttachment loadedAttachment : loadedAttachments) {
			if (existingAttachments.contains(loadedAttachment)) {
				AssetAttachment existingAttachment = existingAttachments.get(existingAttachments.indexOf(loadedAttachment));
				existingAttachment.setCreated(loadedAttachment.getCreated());
			}
		}
	}
	private void updateAttachments() {
		AssetAttachmentSaver saver = new AssetAttachmentSaver(user, asset);
		for (AssetAttachment attachment : existingAttachments) {
			saver.update(attachment);
		}
	}
	
	private void removeAttachments() {
		List<AssetAttachment> loadedAttachments = new AssetAttachmentListLoader(filter).setAsset(asset).load();
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
	
	private void saveAssetImage() throws SubAssetUniquenessException {
		AssetImageFileSaver saver = new AssetImageFileSaver(asset, newAssetImageName);
		if(newAssetImage && newAssetImageName != null) {
			saver.save();
		}
		
		if(removeAssetImage) {
			saver.remove();
		}
	}

	private void saveAssetImageName() {
		newAssetImageName = asset.getImageName();
		if(newAssetImage && newAssetImageName != null) {
			asset.setImageName(newAssetImageName.split("/")[1]);
		}
		
		if(removeAssetImage) {
			asset.setImageName(null);
		}
	}

	
	public Asset getAsset() {
		return asset;
	}

	public AssetSaveService setAsset(Asset asset) {
		this.asset = asset;
		return this;
	}

	public List<AssetAttachment> getExistingAttachments() {
		return existingAttachments;
	}

	public AssetSaveService setExistingAttachments(List<AssetAttachment> existingAttachments) {
		this.existingAttachments = existingAttachments;
		return this;
	}

	public List<AssetAttachment> getUploadedAttachments() {
		return uploadedAttachments;
	}

	public AssetSaveService setUploadedAttachments(List<AssetAttachment> uploadedAttachments) {
		this.uploadedAttachments = uploadedAttachments;
		return this;
	}

	public boolean isNewAssetImage() {
		return newAssetImage;
	}

	public void setNewAssetImage(boolean newAssetImage) {
		this.newAssetImage = newAssetImage;
	}

	public boolean isRemoveAssetImage() {
		return removeAssetImage;
	}

	public void setRemoveAssetImage(boolean removeAssetImage) {
		this.removeAssetImage = removeAssetImage;
	}

}
