package com.n4systems.model.productstatus;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.SecuredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

//TODO: Update this class to extend FilteredLoader
public class ProductStatusFilteredLoader extends SecuredLoader<ProductStatusBean> {
	private Long id;
	
	public ProductStatusFilteredLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public ProductStatusFilteredLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected ProductStatusBean load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<ProductStatusBean> builder = new QueryBuilder<ProductStatusBean>(ProductStatusBean.class, filter.prepareFor(ProductStatusBean.class));
		
		builder.addSimpleWhere("uniqueID", id);
		
		ProductStatusBean productStatus = pm.find(builder);
		
	    return productStatus;
	}

	public ProductStatusFilteredLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
