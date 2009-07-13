package com.n4systems.model.producttype;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.ProductType;
import com.n4systems.persistence.loaders.legacy.SecuredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

/**
 * @deprecated Use {@link FilteredIdLoader} instead.
 */
@Deprecated
public class ProductTypeFilteredLoader extends SecuredLoader<ProductType> {
	private Long id;
	
	public ProductTypeFilteredLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public ProductTypeFilteredLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected ProductType load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<ProductType> builder = new QueryBuilder<ProductType>(ProductType.class, filter.prepareFor(ProductType.class));
		
		builder.addSimpleWhere("id", id);
		
		builder.addPostFetchPaths("infoFields");
		ProductType productType = pm.find(builder);
		
	    return productType;
	}

	public ProductTypeFilteredLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
