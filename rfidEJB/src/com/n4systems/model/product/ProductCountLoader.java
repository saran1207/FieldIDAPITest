package com.n4systems.model.product;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductCountLoader extends Loader<Long> {
	private Long tenantId;
	
	public ProductCountLoader() {}

	@Override
	protected Long load(EntityManager em) {
		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);
		QueryBuilder<Long> builder = new QueryBuilder<Long>(Product.class, filter);
		
		Long productCount = builder.getCount(em);
		return productCount;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
