package com.n4systems.model.safetynetwork;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.Asset;
import com.n4systems.persistence.loaders.Loader;

/**
 * A loader to test that a AssetType attachment can be loaded from a given networkId.  The
 * attachment is considered loadable iff it is attached to one or more of the ProductTypes
 * of the Products with a given networkId. 
 */
public class ProductTypeFileAttachmentIdByProductNetworkIdExistsLoader extends Loader<Boolean> {
	private Long networkId;
	private Long attachmentId;
	
	@Override
	protected Boolean load(EntityManager em) {
		/*
		 * The following query counts the number of attachments whose attachment id is equal
		 * to attachmentId and who are attached to the asset types of products with a
		 * network id of networkId.
		 * Basically this count will return 1 if you can see this attachment from this network id
		 * or 0 if you can't.
		 */
		StringBuilder jpql = new StringBuilder("SELECT COUNT(*) FROM ");
		jpql.append(Asset.class.getName());
		jpql.append(" p, IN (p.type.attachments) a WHERE p.networkId = :networkId AND a.id = :attachmentId");
		
		Query query = em.createQuery(jpql.toString());
		query.setParameter("networkId", networkId);
		query.setParameter("attachmentId", attachmentId);
		
		Long count = (Long)query.getSingleResult();
		return (count > 0);
	}

	public ProductTypeFileAttachmentIdByProductNetworkIdExistsLoader setNetworkId(Long networkId) {
		this.networkId = networkId;
		return this;
	}

	public ProductTypeFileAttachmentIdByProductNetworkIdExistsLoader setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
		return this;
	}

}
