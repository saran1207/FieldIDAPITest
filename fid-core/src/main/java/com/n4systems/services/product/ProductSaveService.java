package com.n4systems.services.product;

import java.util.List;

import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.model.Asset;
import org.apache.log4j.Logger;


import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.model.product.ProductAttachment;
import com.n4systems.model.product.ProductAttachmentListLoader;
import com.n4systems.model.product.ProductAttachmentSaver;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;

public class ProductSaveService {
	private static Logger logger = Logger.getLogger(ProductSaveService.class);
	private final LegacyProductSerial productManager;
	private final User user;
	private SecurityFilter filter;
	private Asset asset;
	private List<ProductAttachment> existingAttachments;
	private List<ProductAttachment> uploadedAttachments;

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
			ProductAttachmentSaver saver = new ProductAttachmentSaver(user, asset);
			for (ProductAttachment attachment : uploadedAttachments) {
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
		List<ProductAttachment> loadedAttachments = new ProductAttachmentListLoader(filter).setProduct(asset).load();
		for (ProductAttachment loadedAttachment : loadedAttachments) {
			if (existingAttachments.contains(loadedAttachment)) {
				ProductAttachment existingAttachment = existingAttachments.get(existingAttachments.indexOf(loadedAttachment));
				existingAttachment.setCreated(loadedAttachment.getCreated());
			}
		}
	}
	private void updateAttachments() {
		ProductAttachmentSaver saver = new ProductAttachmentSaver(user, asset);
		for (ProductAttachment attachment : existingAttachments) {
			saver.update(attachment);
		}
	}
	
	private void removeAttachments() {
		List<ProductAttachment> loadedAttachments = new ProductAttachmentListLoader(filter).setProduct(asset).load();
		ProductAttachmentSaver deleter = new ProductAttachmentSaver(asset);
		for (ProductAttachment loadedAttachment : loadedAttachments) {
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

	public List<ProductAttachment> getExistingAttachments() {
		return existingAttachments;
	}

	public ProductSaveService setExistingAttachments(List<ProductAttachment> existingAttachments) {
		this.existingAttachments = existingAttachments;
		return this;
	}

	public List<ProductAttachment> getUploadedAttachments() {
		return uploadedAttachments;
	}

	public ProductSaveService setUploadedAttachments(List<ProductAttachment> uploadedAttachments) {
		this.uploadedAttachments = uploadedAttachments;
		return this;
	}

}
