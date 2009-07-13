package com.n4systems.model.productstatus;

import java.util.List;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.ListLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

//TODO: Update this class to extend FilteredListLoader
public class ProductStatusListLoader extends ListLoader<ProductStatusBean> {

	public ProductStatusListLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public ProductStatusListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<ProductStatusBean> load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<ProductStatusBean> builder = new QueryBuilder<ProductStatusBean>(ProductStatusBean.class, filter.prepareFor(ProductStatusBean.class));
		builder.addOrder("name");
		
		List<ProductStatusBean> productStati = pm.findAll(builder);
		return productStati;
	}

}
