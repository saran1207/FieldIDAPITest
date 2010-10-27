package com.n4systems.model.assettype;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.AssetType;
import com.n4systems.persistence.loaders.Loader;

/**
 *  Returns the AssetType id (or null if one was not found) for a FileAttachment.
 */
public class AssetTypeByAttachmentLoader extends Loader<Long> {

	private Long attachmentId;
	
	@Override
	protected Long load(EntityManager em) {
		StringBuilder jpql = new StringBuilder("SELECT DISTINCT pt.id FROM ").append(AssetType.class.getName()).append(" pt, IN (pt.attachments) a WHERE a.id = :attachmentId");
		
		Query query = em.createQuery(jpql.toString());
		query.setParameter("attachmentId", attachmentId);
		
		Long assetTypeId = null;
		try {
			assetTypeId = (Long)query.getSingleResult();
		} catch(RuntimeException re) {}
		
		return assetTypeId;
	}

	public AssetTypeByAttachmentLoader setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
		return this;
	}

}
