package com.n4systems.model.product;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Product;
import com.n4systems.persistence.loaders.legacy.ListLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductAttachmentListLoader extends ListLoader<ProductAttachment> {

	private Product product;
	
	public ProductAttachmentListLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	@Override
	protected List<ProductAttachment> load(PersistenceManager pm, SecurityFilter filter) {
		if (product == null) {
			throw new InvalidArgumentException("you must have a product to load product attachments");
		}
		ProductAttachment.prepareFilter(filter);
		QueryBuilder<ProductAttachment> builder = new QueryBuilder<ProductAttachment>(ProductAttachment.class, filter);
		builder.addSimpleWhere("product", product).addOrder("id");
		
		return pm.findAll(builder);
		
	}

	public ProductAttachmentListLoader setProduct(Product product) {
		this.product = product;
		return this;
	}
}
