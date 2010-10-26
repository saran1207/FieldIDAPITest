package com.n4systems.services.product;

import java.util.List;

import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.model.Asset;
import com.n4systems.model.product.AssetAttachment;
import com.n4systems.model.product.AssetAttachmentListLoader;
import com.n4systems.model.product.AssetAttachmentSaver;
import org.apache.log4j.Logger;


import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;

public class ProductSaveService {
	private static Logger logger = Logger.getLogger(ProductSaveService.class);
	private final LegacyProductSerial productManager;
	private final User user;
	private SecurityFilter filter;
	private Asset asset;
	private List<AssetAttachment> existingAttachments;
	private List<AssetAttachment> uploadedAttachments;

	public ProductSaveService(LegacyProductSerial productManager, User user) {
		super();
		this.productManager = productManager;
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
			createProduct(withHistory);
			saveUploadedAttachments();
			return asset;
		} catch (SubAssetUniquenessException e) {
			throw new ProcessFailureException("could not save asset", e);
		}
	}

	public Asset update() {
		try {
			updateProduct();
			updateExistingAttachments();
			saveUploadedAttachments();
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

	private void createProduct(boolean withHistory) throws SubAssetUniquenessException {
		saveRequirements();
		
		if (withHistory) {
			asset = productManager.createWithHistory(asset, user);
		} else {
			asset = productManager.create(asset, user);
		}
	}

	private void updateProduct() throws SubAssetUniquenessException {
		saveRequirements();
		asset = productManager.update(asset, user);
	}

	private void saveRequirements() {
		if (asset == null) {
			throw new InvalidArgumentException("asset must defined.");
		}
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
		List<AssetAttachment> loadedAttachments = new AssetAttachmentListLoader(filter).setProduct(asset).load();
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
		List<AssetAttachment> loadedAttachments = new AssetAttachmentListLoader(filter).setProduct(asset).load();
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
	
	public Asset getProduct() {
		return asset;
	}

	public ProductSaveService setProduct(Asset product) {
		this.asset = product;
		return this;
	}

	public List<AssetAttachment> getExistingAttachments() {
		return existingAttachments;
	}

	public ProductSaveService setExistingAttachments(List<AssetAttachment> existingAttachments) {
		this.existingAttachments = existingAttachments;
		return this;
	}

	public List<AssetAttachment> getUploadedAttachments() {
		return uploadedAttachments;
	}

	public ProductSaveService setUploadedAttachments(List<AssetAttachment> uploadedAttachments) {
		this.uploadedAttachments = uploadedAttachments;
		return this;
	}

}
