package com.n4systems.model.product;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Product;
import com.n4systems.persistence.savers.legacy.EntitySaver;

public class ProductAttachmentSaver extends EntitySaver<ProductAttachment> {

	private final Product product;
	
	
	public ProductAttachmentSaver(Product product, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.product = product;
	}


	@Override
	protected void save(PersistenceManager pm, ProductAttachment entity) {
		fillInConnectionFields(entity);
		entity = saveAttachment(pm, entity);
		moveAttachedFile(pm, entity);
		
	}


	private ProductAttachment saveAttachment(PersistenceManager pm, ProductAttachment entity) {
		entity = pm.update(entity);
		return entity;
	}


	private void moveAttachedFile(PersistenceManager pm, ProductAttachment entity) {
		ProductAttachmentFileSaver fileSaver = new ProductAttachmentFileSaver();
		fileSaver.setAttachment(entity).save();
		entity = pm.update(entity);
	}


	@Override
	protected ProductAttachment update(PersistenceManager pm, ProductAttachment entity) {
		fillInConnectionFields(entity);
		return pm.update(entity);
	}

	private void fillInConnectionFields(ProductAttachment entity) {
		entity.setProduct(product);
		entity.setTenant(product.getTenant());
	}

}
