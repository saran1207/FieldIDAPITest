package com.n4systems.model.product;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Product;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductAttachmentListLoader extends ListLoader<ProductAttachment> {
	private Product product;

	public ProductAttachmentListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<ProductAttachment> load(EntityManager em, SecurityFilter filter) {
		if (product == null) {
			throw new InvalidArgumentException("you must have a product to load product attachments");
		}
		QueryBuilder<ProductAttachment> builder = new QueryBuilder<ProductAttachment>(ProductAttachment.class, filter);
		builder.addSimpleWhere("product", product).addOrder("id");
		
		return builder.getResultList(em);
		
	}

	public ProductAttachmentListLoader setProduct(Product product) {
		this.product = product;
		return this;
	}
}
