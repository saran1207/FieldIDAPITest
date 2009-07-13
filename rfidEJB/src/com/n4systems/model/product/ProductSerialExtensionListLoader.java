package com.n4systems.model.product;

import java.util.List;

import rfid.ejb.entity.ProductSerialExtensionBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.ListLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

//TODO: Update this class to extend FilteredListLoader
public class ProductSerialExtensionListLoader extends ListLoader<ProductSerialExtensionBean> {

	public ProductSerialExtensionListLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public ProductSerialExtensionListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<ProductSerialExtensionBean> load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<ProductSerialExtensionBean> builder = new QueryBuilder<ProductSerialExtensionBean>(ProductSerialExtensionBean.class, filter.prepareFor(ProductSerialExtensionBean.class));
		builder.setOrder("extensionLabel");
		
		List<ProductSerialExtensionBean> extensions = pm.findAll(builder);
		return extensions;
	}

}
