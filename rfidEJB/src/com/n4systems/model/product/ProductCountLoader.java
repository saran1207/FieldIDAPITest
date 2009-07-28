package com.n4systems.model.product;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductCountLoader extends Loader<Long> {
	private Long tenantId;
	
	public ProductCountLoader() {}

	@Override
	protected Long load(EntityManager em) {
		SecurityFilter filter = new SecurityFilter(tenantId);
		QueryBuilder<Long> builder = new QueryBuilder<Long>(Product.class, filter.prepareFor(Product.class));
		
		Long productCount = builder.getCount(em);
		return productCount;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
