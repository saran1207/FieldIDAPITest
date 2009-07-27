package com.n4systems.model.product;

import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.ProductSerialExtensionBean;

import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductSerialExtensionListLoader extends ListLoader<ProductSerialExtensionBean> {

	public ProductSerialExtensionListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<ProductSerialExtensionBean> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<ProductSerialExtensionBean> builder = new QueryBuilder<ProductSerialExtensionBean>(ProductSerialExtensionBean.class, filter.prepareFor(ProductSerialExtensionBean.class));
		builder.setOrder("extensionLabel");
		
		List<ProductSerialExtensionBean> extensions = builder.getResultList(em);
		return extensions;
	}

}
