package com.n4systems.model.productstatus;

import javax.persistence.EntityManager;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductStatusFilteredLoader extends SecurityFilteredLoader<ProductStatusBean> {
	private Long id;

	public ProductStatusFilteredLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected ProductStatusBean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<ProductStatusBean> builder = new QueryBuilder<ProductStatusBean>(ProductStatusBean.class, filter.prepareFor(ProductStatusBean.class));
		builder.addSimpleWhere("uniqueID", id);
		
		ProductStatusBean productStatus = builder.getSingleResult(em);
	    return productStatus;
	}

	public ProductStatusFilteredLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
