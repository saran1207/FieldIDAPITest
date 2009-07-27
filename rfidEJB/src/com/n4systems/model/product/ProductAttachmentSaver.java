package com.n4systems.model.product;

import java.io.File;

import javax.persistence.EntityManager;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Product;
import com.n4systems.persistence.savers.ModifiedBySaver;
import com.n4systems.reporting.PathHandler;

public class ProductAttachmentSaver extends ModifiedBySaver<ProductAttachment> {
	private final Product product;
	
	public ProductAttachmentSaver(Product product) {
		super();
		this.product = product;
	}
	
	public ProductAttachmentSaver(Long modifiedBy, Product product) {
		super(modifiedBy);
		this.product = product;
	}

	public ProductAttachmentSaver(UserBean modifiedBy, Product product) {
		super(modifiedBy);
		this.product = product;
	}

	@Override
	protected void save(EntityManager em, ProductAttachment entity) {
		fillInConnectionFields(entity);
		entity = saveAttachment(em, entity);
		moveAttachedFile(em, entity);
	}
	
	@Override
	protected ProductAttachment update(EntityManager em, ProductAttachment entity) {
		fillInConnectionFields(entity);
		return em.merge(entity);
	}

	@Override
	protected void remove(EntityManager em, ProductAttachment entity) {
		if (entity == null || entity.isNew()) {
			throw new InvalidArgumentException("you need an attachment that has been persisted.");
		}
		
		em.remove(entity);
		deleteFile(entity);
	}

	private ProductAttachment saveAttachment(EntityManager em, ProductAttachment entity) {
		entity = em.merge(entity);
		return entity;
	}

	private void moveAttachedFile(EntityManager em, ProductAttachment entity) {
		ProductAttachmentFileSaver fileSaver = new ProductAttachmentFileSaver();
		fileSaver.setAttachment(entity).save();
		entity = em.merge(entity);
	}

	private void fillInConnectionFields(ProductAttachment entity) {
		entity.setProduct(product);
		entity.setTenant(product.getTenant());
	}
	
	//TODO move to a file Deleter.
	private void deleteFile(ProductAttachment attachment) {
		File attachmentDirectory = PathHandler.getAttachmentFile(attachment);
		File attachedFile = new File(attachmentDirectory, attachment.getFileName());
		
		if (attachedFile.exists()) {
			attachedFile.delete();
		}
		
	}
}
