package com.n4systems.services.product;

import java.util.List;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.LegacyProductSerial;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.model.Product;
import com.n4systems.model.product.ProductAttachment;
import com.n4systems.model.product.ProductAttachmentListLoader;
import com.n4systems.model.product.ProductAttachmentSaver;
import com.n4systems.util.SecurityFilter;

public class ProductSaveService {

	private final LegacyProductSerial productManager;
	private final UserBean user;
	private SecurityFilter filter;
	private Product product;
	private List<ProductAttachment> existingAttachments;
	private List<ProductAttachment> uploadedAttachments;

	public ProductSaveService(LegacyProductSerial productManager, UserBean user) {
		super();
		this.productManager = productManager;
		this.user = user;
		filter = new SecurityFilter(user);
		
	}

	public Product create() {
		try {
			createProduct();
			saveUploadedAttachments();
			return product;
		} catch (SubProductUniquenessException e) {
			throw new ProcessFailureException("could not save product", e);
		}
	}

	public Product update() {
		try {
			updateProduct();
			updateExistingAttachments();
			saveUploadedAttachments();
			return product;
		} catch (SubProductUniquenessException e) {
			throw new ProcessFailureException("could not save product", e);
		}
	}
	
	public void clear() {
		product = null;
		existingAttachments = null;
		uploadedAttachments = null;
	}

	private void createProduct() throws SubProductUniquenessException {
		saveRequirements();
		product = productManager.createWithHistory(product, user.getId());
	}

	private void updateProduct() throws SubProductUniquenessException {
		saveRequirements();
		product = productManager.update(product);
	}

	private void saveRequirements() {
		if (product == null) {
			throw new InvalidArgumentException("product must defined.");
		}
	}

	private void saveUploadedAttachments() {
		if (uploadedAttachments != null) {
			ProductAttachmentSaver saver = new ProductAttachmentSaver(user, product);
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
		List<ProductAttachment> loadedAttachments = new ProductAttachmentListLoader(filter).setProduct(product).load();
		for (ProductAttachment loadedAttachment : loadedAttachments) {
			if (existingAttachments.contains(loadedAttachment)) {
				ProductAttachment existingAttachment = existingAttachments.get(existingAttachments.indexOf(loadedAttachment));
				existingAttachment.setCreated(loadedAttachment.getCreated());
			}
		}
	}
	private void updateAttachments() {
		ProductAttachmentSaver saver = new ProductAttachmentSaver(user, product);
		for (ProductAttachment attachment : existingAttachments) {
			saver.update(attachment);
		}
	}
	
	private void removeAttachments() {
		List<ProductAttachment> loadedAttachments = new ProductAttachmentListLoader(filter).setProduct(product).load();
		ProductAttachmentSaver deleter = new ProductAttachmentSaver(product);
		for (ProductAttachment loadedAttachment : loadedAttachments) {
			if (!existingAttachments.contains(loadedAttachment)) {
				deleter.remove(loadedAttachment);
			}
		}
	}
	
	public Product getProduct() {
		return product;
	}

	public ProductSaveService setProduct(Product product) {
		this.product = product;
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
