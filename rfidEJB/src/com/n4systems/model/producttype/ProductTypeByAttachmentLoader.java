package com.n4systems.model.producttype;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.ProductType;
import com.n4systems.persistence.loaders.Loader;

/**
 *  Returns the ProductType id (or null if one was not found) for a FileAttachment.
 */
public class ProductTypeByAttachmentLoader extends Loader<Long> {

	private Long attachmentId;
	
	@Override
	protected Long load(EntityManager em) {
		StringBuilder jpql = new StringBuilder("SELECT DISTINCT pt.id FROM ").append(ProductType.class.getName()).append(" pt, IN (pt.attachments) a WHERE a.id = :attachmentId");
		
		Query query = em.createQuery(jpql.toString());
		query.setParameter("attachmentId", attachmentId);
		
		Long productTypeId = null;
		try {
			productTypeId = (Long)query.getSingleResult();
		} catch(RuntimeException re) {}
		
		return productTypeId;
	}

	public ProductTypeByAttachmentLoader setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
		return this;
	}

}
